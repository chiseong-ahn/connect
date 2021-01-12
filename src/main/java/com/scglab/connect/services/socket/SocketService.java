package com.scglab.connect.services.socket;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.scglab.connect.base.interceptor.CommonInterceptor;
import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.automessage.AutoMessage;
import com.scglab.connect.services.automessage.AutoMessageDao;
import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.JwtService;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.common.service.PushService;
import com.scglab.connect.services.company.external.ICompany;
import com.scglab.connect.services.customer.Customer;
import com.scglab.connect.services.customer.CustomerDao;
import com.scglab.connect.services.customer.VCustomer;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.login.Profile;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.services.message.Message;
import com.scglab.connect.services.message.MessageDao;
import com.scglab.connect.services.review.ReviewDao;
import com.scglab.connect.services.room.Room;
import com.scglab.connect.services.room.RoomDao;
import com.scglab.connect.utils.DataUtils;

import edu.emory.mathcs.backport.java.util.Collections;
import lombok.RequiredArgsConstructor;

@SuppressWarnings({"rawtypes", "unchecked"})
@RequiredArgsConstructor
@Service
public class SocketService {
	
	Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);
	
	private final ChatRoomRepository chatRoomRepository;
	
	@Autowired private CommonService commonService;
	@Autowired private LoginService loginService;
	@Autowired private PushService pushService;
	@Autowired private RoomDao roomDao;
	@Autowired private MessageDao messageDao;
	@Autowired private MessageHandler messageHandler;
	@Autowired private AutoMessageDao autoMessageDao;
	@Autowired private JwtService jwtService;
	@Autowired private CustomerDao customerDao;
	@Autowired private ReviewDao reviewDao;
	@Autowired private SocketMessageHandler socketMessageHandler;
	
	
	// 이벤트메세지 유형.
	public enum EventName {
		ERROR,
		READ_MESSAGE,
		ROOM_DETAIL,
		ASSIGN, ASSIGNED, 
		LOGINED, UNAUTHORIZED,
		JOIN, JOINED,
		AUTO_MESSAGE,
		WELCOME_MESSAGE, START_MESSAGE,
		MESSAGE, MESSAGE_LIST,
		SAVE_HISTORY,
		DELETE_MESSAGE,
		LEAVE,
		REVIEW,
		END,
		RELOAD_READY, ONLINE, OFFLINE
	}
	
	// 메세지 발신대상.
	public enum Target{
		BROADCAST,	// 룸 전체 발신.
		LOBBY,		// 대기룸에 발신.
		SELF,		// 본인에게 발신.
		CUSTOMER,	// 고객에게 발신.
		MEMBER		// 멤버에게 발신.
	}
	
	// 연결처리.
	public void connect(SessionConnectedEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		MessageHeaders headers = headerAccessor.getMessageHeaders();
		String sessionId = headerAccessor.getSessionId();
		
		this.logger.info("[headerAccessor] - " + headerAccessor.toString());
		this.logger.info("[Socket sessionId] - " + sessionId);
		
		Profile profile = null;
		
		Map<String, Object> nativeHeaders = null;
		if (headers.containsKey("simpConnectMessage")) {
			GenericMessage simpConnectMessage = (GenericMessage) headers.get("simpConnectMessage");
			Map<String, Object> subHeaders = simpConnectMessage.getHeaders();
			nativeHeaders = (Map<String, Object>) subHeaders.get("nativeHeaders");

			if (nativeHeaders.containsKey("Authorization")) {
				// 멤버(관리자) 로그인.
				String token = ((List<String>) nativeHeaders.get("Authorization")).get(0);
				profile = loginMember(token);
				
			}else if(nativeHeaders.containsKey("companyId") && nativeHeaders.containsKey("gasappMemberNumber")){
				// 고객 로그인
				String companyId = ((List<String>) nativeHeaders.get("companyId")).get(0);
				String gasappMemberNumber = ((List<String>) nativeHeaders.get("gasappMemberNumber")).get(0);
				String secretKey = ((List<String>) nativeHeaders.get("secretKey")).get(0);
				
				profile = loginCustomer(companyId, gasappMemberNumber, secretKey);
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("companyId", companyId);
				params.put("gasappMemberNumber", gasappMemberNumber);
				VCustomer customer = this.customerDao.findByGassappMemberNumber(params);
				this.logger.debug("customer : " + customer);
				
				profile.setRoomId(customer.getRoomId() + "");
				this.logger.info("connect customer : " + profile.toString());
			}
		}
		
		if(profile == null) {
			throw new RuntimeException("login fail");
		}
		profile.setSessionId(sessionId);
		
		this.logger.info("Connection profile : " + profile.toString());
		// [Redis] 연결된 세션ID와 Token을 저장.
		this.chatRoomRepository.setProfileBySessionId(sessionId, profile);
	}
	
	// 구독처리.
	public void subscribe(SessionSubscribeEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		this.logger.info("[Subscribe] headerAccessor : " + headerAccessor.toString());
		String destination = headerAccessor.getDestination();
		
		//MessageHeaders headers = headerAccessor.getMessageHeaders();
		//String destination = (String) headers.get("simpDestination");
		String roomId = destination.replaceAll(Constant.SOCKET_SIMPLE_BROKER + Constant.SOCKET_ROOM_PREFIX + "/", "");
		//String sessionId = (String) headers.get("simpSessionId");
		String sessionId = headerAccessor.getSessionId(); 
		this.logger.info("sessionId : " + sessionId);
		this.logger.info("roomId : " + roomId);
		
		// [Redis] SessionId에 대한 토큰 추출
		Profile profile = this.chatRoomRepository.getProfileBySessionId(sessionId);
		this.logger.debug("subscribe profile - " + profile);
		
		
		// 개인룸 및 대기룸 조인일 경우.
		if(destination.equals(Constant.SOCKET_PRIVATE_ROOM) || destination.indexOf(Constant.SOCKET_LOBBY_ROOM) > -1) {
			
			if(profile.getIsCustomer() == 1) {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("profile", profile);
				
				if(profile.isAuthenticated()) {
					this.socketMessageHandler.sendMessageToSelf(EventName.LOGINED, profile, data);
				}else {
					profile.setRoomId(null);
					this.socketMessageHandler.sendMessageToSelf(EventName.UNAUTHORIZED, profile, data);
				}
			}
			
			
			return;
		}
		
		profile.setRoomId(roomId);
		this.chatRoomRepository.setProfileBySessionId(sessionId, profile);

		// [Redis] 채팅방에 들어온 클라이언트 sessionId를 roomId와 매핑.
		this.chatRoomRepository.setUserJoinInfo(sessionId, roomId);

		// [Redis] 해당 채팅방의 참여인원수 1증가.
		this.chatRoomRepository.plusUserCount(roomId);
		 
		if(profile.getIsCustomer() == 1) {
			// 고객 조인.
			this.chatRoomRepository.setCustomerJoin(roomId, sessionId);
			
		}else {
			// 멤버 조인.
			this.chatRoomRepository.setMemberJoin(roomId, sessionId);
		}
		
		SocketData payload = new SocketData();
		payload.setEventName(EventName.JOIN);
		payload.setRoomId(roomId);
		payload.setCompanyId(profile.getCompanyId());
		
		// 조인처리.
		join(payload, profile);
	}
	
	
	
	
	// 방 상세 조회
	public void roomDetail(Profile profile, SocketData payload) {
		
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		// [DB] 방 상세정보 조회
		params = new HashMap<String, Object>();
		params.put("id", payload.getRoomId());
		Room room = this.roomDao.getDetail(params);
		
		if(room != null) {
			// [Socket] 방 상세 정보 전송.
			sendData = new HashMap<String, Object>();
			sendData.put("room", room);
			this.socketMessageHandler.sendMessageToSelf(EventName.ROOM_DETAIL, profile, sendData);
			
		}else {
			// [Socket] 오류 알림 메세지 전송. - 일치하는 채팅상담이 존재하지 않음.
			this.socketMessageHandler.sendErrorMessage(profile, this.messageHandler.getMessage("error.room.room-detail"));
		}
	}
	
	public Member getProfile(String token) {
		this.logger.debug("token : " + token);
		Member member = null;
		Map<String, Object> claims = this.jwtService.getJwtData(token);
		this.logger.debug("claims" + claims.toString());
		
		if(claims.containsKey("isCustomer")) {
			return this.loginService.getMember(token);
			
		}
		
		Customer customer = this.loginService.getCustomer(token);
		member = new Member();
		member.setCompanyId(customer.getCompanyId());
		member.setIsAdmin(0);
		member.setIsCustomer(1);
		member.setSpeakerId(customer.getSpeakerId());
		member.setName(customer.getName());
		
		return member;
	}
	
	// 상담사 배정.
	public void assign(Profile profile, SocketData payload) {
		this.logger.debug("profile : " + profile.toString());
		
		Map<String, Object> sendData = null;
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> data = payload.getData();
		
		params.put("memberId", profile.getId());
		params.put("id", DataUtils.getInt(data, "roomId", 0));
		params.put("roomId", DataUtils.getInt(data, "roomId", 0));
		
		Room room = this.roomDao.getDetail(params);
		if(room.getMemberId() > 0) {
			// "이미 상담사가 배정되어 있는 경우" Exception 발생시킴.
			throw new RuntimeException(this.messageHandler.getMessage("error.room.assigned"));
		}
		
		// 매칭처리.
		this.roomDao.matchRoom(params);
		
		// 매칭된 방 정보조회.
		room = this.roomDao.getDetail(params);
		
		// [Socket] > 상담사 배정메시지 전송.
		sendData = new HashMap<String, Object>();
		sendData.put("room", room);
		this.socketMessageHandler.sendMessageToLobby(EventName.RELOAD_READY, profile, null);
	}

	// 방 조인
	public void join(SocketData payload, Profile profile) {
		
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		// 로비에 조인일 경우 - 조인만 시키고 아무일도 없음.
		if(payload.getRoomId().indexOf(Constant.SOCKET_LOBBY_ROOM) > -1) {
			this.logger.debug("Lobby join : " + payload.toString());
			return;
		}
		
		// [DB] 채팅방 정보 조회
		params = new HashMap<String, Object>();
		params.put("id", payload.getRoomId());
		Room room = this.roomDao.getDetail(params);
		this.logger.debug("room : " + room);
		
		if(profile.getIsMember() == 1 && room.getMemberId() != profile.getId()) {
			// 본인의 상담이 아닐경우.  별도처리하지 않음.
			return;
		}
		
		// [DB] 채팅방에 조인 처리.
		params = new HashMap<String, Object>();
		params.put("roomId", payload.getRoomId());
		params.put("speakerId", profile.getSpeakerId());
		this.roomDao.joinRoom(params);
		
		// [Socket] > 채팅방 참여자들에게 조인완료 메세지 전송.
		sendData = new HashMap<String, Object>();
		sendData.put("profile", profile);
		
		// 본인에게 조인성공 알림메시지 발송.
		this.socketMessageHandler.sendMessageToSelf(EventName.JOINED, profile, sendData);
		
		int readLastMessageId = DataUtils.getInt(params, "readLastMessageId", 0);	// 마지막 읽음메세지id
		int maxMessageId = DataUtils.getInt(params, "maxMessageId", 0);	// 마지막 메세지id
		
		// 마지막 메세지가 존재하고, 마지막 메세지와 마지막 읽음메세지가 같지 않을경우 Read 메세지 전송.
		this.logger.debug("maxMessageId : " + maxMessageId);
		this.logger.debug("readLastMessageId : " + readLastMessageId);
		
		if(maxMessageId > 0 && (maxMessageId != readLastMessageId)) {
			
			// [Socket] > 읽음 메세지 전송.
			sendData = new HashMap<String, Object>();
			sendData.put("roomId", payload.getRoomId());
			sendData.put("speakerId", profile.getSpeakerId());
			sendData.put("startId", readLastMessageId);
			sendData.put("endId", maxMessageId);
			
			// 나를 제외하고 메시지 발송.
			this.socketMessageHandler.sendMessageToBroadcast(EventName.READ_MESSAGE, profile, sendData);
		}
		
		// 고객여부 판단.
		if(profile.getIsCustomer() == 1) {
			// 고객일 경우.
			
			// [DB] 채팅방을 온라인상태로 변경.
			params = new HashMap<String, Object>();
			params.put("id", payload.getRoomId());
			params.put("isOnline", 1);
			this.roomDao.updateOnline(params);
			
			sendData = new HashMap<String, Object>();
			sendData.put("profile", profile);
			this.socketMessageHandler.sendMessageToLobby(EventName.ONLINE, profile, sendData);
			
			// 채팅방의 상태가 최초이거나 종료일 경우 시작메세지 준비.
			if(room.getState() == 8) {	
				
				// 각 회사의 서비스 클래스 가져오기.
				ICompany company = this.commonService.getCompany(payload.getCompanyId());
				
				// 시작메세지 유형
				int messageType = 0;
				
				// 회사별 근무상태 조회. (1-근무 중, 2-근무 외 시간, 3-점심시간.)
				int isWorkType = company.getWorkCalendar();
				
				if(isWorkType == 1) {	// 근무중일 경우.(1)
					
					// 로비에 상담가능한 상담사 카운트 조회. 
					Long readyMemberCount = this.chatRoomRepository.getUserCount(getLobbyRoom(payload.getCompanyId()));
					
					Map<String, Object> msgParams = new HashMap<String, Object>();
					msgParams.put("companyId", payload.getCompanyId());
					if(readyMemberCount == 0) {
						// 상담가능한 상담사가 존재하지 않을경우.
						messageType = 1;	// 상담가능한 상담사가 없을경우.
					}
					
				} else if(isWorkType == 2) {	// 근무 외 시간
					messageType = 2;	
					
				} else if(isWorkType == 3) {	// 점심시간
					messageType = 3;
					
				}
				this.logger.debug("messageType : " + messageType);
				
				// 시작메세지 조회
				String startMessage = this.messageHandler.getMessage("socket.startmessage.type" + messageType);
				
				// [DB] 신규 메세지 생성.
				params = new HashMap<String, Object>();
				params.put("companyId", payload.getCompanyId());
				params.put("roomId", payload.getRoomId());
				params.put("speakerId", null);
				params.put("messageType", 0);		// 메세지 유형 (0-일반, 1-이미지, 2-동영상, 3-첨부, 4-링크, 5-이모티콘)
				params.put("isSystemMessage", 1);
				params.put("message", startMessage);
				params.put("messageAdminType", 0);	// 시스템 메세지의 다른 유형. (0-일반 메세지, 1-시스템 메세지)
				params.put("isEmployee", 1);
				params.put("messageDetail", "");
				params.put("templateId", null);
				Message newMessage = this.messageDao.create(params);
				
				// [Socket] 시작메시지 전송.
				sendData = new HashMap<String, Object>();
				sendData.put("message", newMessage);
				
				// 룸 전체에 시작메시지 발송.
//				this.socketMessageHandler.sendMessageToSelf(EventName.START_MESSAGE, profile, sendData);
				
			}
			
			// [DB] 상담사 배정지연 안내메세지 조회
			params = new HashMap<String, Object>();
			params.put("type", 1);
			params.put("companyId", payload.getCompanyId());
			AutoMessage autoMessage1 = this.autoMessageDao.getAutoMessageByMatchWait(params);
			
			// [DB] 답변지연 안내메세지 조회
			params = new HashMap<String, Object>();
			params.put("type", 2);
			params.put("companyId", payload.getCompanyId());
			AutoMessage autoMessage2 = this.autoMessageDao.getAutoMessageByMatchWait(params);
			
			// [Socket] 상담사 배정지연 및 답변지연 안내 메세지 전송. - 메세지를 미리 보내주고 알맞는 시기에 사용할 수 있도록 한다.
			sendData = new HashMap<String, Object>();
			sendData.put("autoMessage1", autoMessage1);
			sendData.put("autoMessage2", autoMessage2);
			
			// 본인(고객)에게 메시지 발송.
			this.socketMessageHandler.sendMessageToSelf(EventName.WELCOME_MESSAGE, profile, sendData);
		}
		
		// [DB] 대화내용 조회
		params = new HashMap<String, Object>();
		params.put("roomId", payload.getRoomId());
		params.put("speakerId", profile.getSpeakerId());
		params.put("intervalDay", Constant.DEFAULT_MESSAGE_INTERVAL_DAY);
		params.put("pageSize", Constant.DEFAULT_MESSAGE_MORE_PAGE_SIZE);
		List<Message> messages = this.messageDao.findByRoomIdToSpeaker(params);
		
		// 정렬순서 뒤집기
		Collections.reverse(messages);
		
		if(messages != null && messages.size() > 0) {		// 이전 메세지가 존재할 경우.
			
			// [Socket] 이전 대화내용 전송.
			sendData = new HashMap<String, Object>();
			sendData.put("messages", messages);
			
			// 본인에게 메시지 발송.
			this.socketMessageHandler.sendMessageToSelf(EventName.MESSAGE_LIST, profile, sendData);
		}else {
			this.logger.info("이전 대화 없음.");
		}
		
		// [Socket] 상담목록 갱신요청 메세지 전송.
		// this.socketMessageHandler.sendMessageToLobby(EventName.RELOAD, profile);
	}
	
	// 메세지 전송
	public void message(Profile profile, SocketData payload) {
		Map<String, Object> data = payload.getData();
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		// [DB] 채팅방 정보 조회
		params = new HashMap<String, Object>();
		params.put("id", profile.getRoomId());
		Room room = this.roomDao.getDetail(params);
		
		String templateId = DataUtils.getString(data, "templateId", "");
		
		// [DB] 신규 메세지 생성.
		params = new HashMap<String, Object>();
		params.put("companyId", profile.getCompanyId());
		params.put("roomId", profile.getRoomId());
		params.put("speakerId", profile.getSpeakerId());
		params.put("messageType", DataUtils.getInt(data, "messageType", 0));		// 메세지 유형 (0-일반, 1-이미지, 2-동영상, 3-첨부, 4-링크, 5-이모티콘)
		params.put("isSystemMessage", DataUtils.getInt(data, "isSystemMessage", 0));
		params.put("message", DataUtils.getString(data, "message",""));
		params.put("messageAdminType", DataUtils.getInt(data, "messageAdminType",0));	// 시스템 메세지의 다른 유형. (0-일반 메세지, 1-시스템 메세지)
		params.put("isEmployee", profile.getIsCustomer() == 1 ? 0 : 1);
		params.put("messageDetail", DataUtils.getString(data, "messageDetail",""));
		params.put("templateId", templateId.equals("") ? null : templateId);
		
		Message newMessage = this.messageDao.create(params);
		
		if(newMessage != null) {
			
			// [Socket] 생성된 메세지 전송.
			sendData = new HashMap<String, Object>();
			sendData.put("message", newMessage);

			// 메시지를 모두에게 발송.
			this.socketMessageHandler.sendMessageToBroadcast(EventName.MESSAGE, profile, sendData);
			
			// 고객이 작성한 메세지일 경우.
			if(profile.getIsCustomer() == 1) {
				
				// 조인 메세지id와 마지막 생성된 메세지가 동일한 경우.
				if(newMessage.getJoinMessageId() == newMessage.getId()) {
					sendData = new HashMap<String, Object>();
					this.socketMessageHandler.sendMessageToLobby(EventName.RELOAD_READY, profile, null);
				}
				
				
			}else {	// 상담사의 메세지일 경우.
				
				// 채팅방의 상태가 오프라인일 경우.
				if(room.getIsOnline() == 0) {
					this.pushService.sendPush(profile.getId(), this.messageHandler.getMessage("socket.push"));
					
				}
			}
		}
	}
	
	
	// 챗봇 이력저장
	public void saveHistory(Profile profile, SocketData payload) {
		Map<String, Object> data = payload.getData();
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		String roomId = payload.getRoomId();
		//String history = DataUtils.getString(data, "history", "");
		List history = data.containsKey("history") ? (List) data.get("history") : null;
		String historyString = JSONArray.toJSONString(history);
		
		// [DB] 이력저장.
		params = new HashMap<String, Object>();
		params.put("roomId", roomId);
		params.put("history", historyString);
		int result = this.roomDao.updateJoinHistory(params);
		
		// [Socket] 이력저장 완료메세지 전송.
		sendData = new HashMap<String, Object>();
		sendData.put("success", result > 0 ? true : false);
		
		// 본인에게 메시지 발송.
		// this.socketMessageHandler.sendMessageToSelf(EventName.SAVE_HISTORY, profile, sendData);
	}
	
	// 리뷰 처리.
	public void review(Profile profile, SocketData payload) {
		Map<String, Object> data = payload.getData();
		Map<String, Object> params = null;
		
		params = new HashMap<String, Object>();
		params.put("companyId", profile.getCompanyId());
		//params.put("gasappMemberNumber", DataUtils.getInt(data, "gasappMemberNumber", 0));
		params.put("gasappMemberNumber", profile.getId());
		params.put("reviewScore", DataUtils.getInt(data, "reviewScore", 0));
		
		this.reviewDao.regist(params);
	}
	
	
	// 메세지 삭제
	public void deleteMessage(Profile profile, SocketData payload) {
		Map<String, Object> data = payload.getData();
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		// 메시지 id
		int id = DataUtils.getInt(data, "id", 0);
		
		if(id > 0) {
			params = new HashMap<String, Object>();
			params.put("id", id);
			
			// [DB] 메세지 읽음 카운트 조회.
			int readCount = this.messageDao.getReadCountByMessageId(params);
			if(readCount <= 0) {
				// [Socket] 메세지를 이미 읽은상태이기에 삭제불가 알림 전송.
				this.socketMessageHandler.sendErrorMessage(profile, this.messageHandler.getMessage("error.message.delete"));
			
			}else {
				// [DB] message_read 삭제처리.
				int result = this.messageDao.deleteMessageRead(params);
				
				if(result > 0) {
					// chat message 삭제.
					this.messageDao.delete(params);
				}
				
				//[Socket] 메세지 삭제완료 알림 전송.
				sendData = new HashMap<String, Object>();
				sendData.put("id", id);
				sendData.put("success", result > 0 ? true : false);
				
				// 전체에게 메시지 발송.
				this.socketMessageHandler.sendMessageToSelf(EventName.DELETE_MESSAGE, profile, sendData);
			}
			
		}else {
			String[] errorParam = new String[1];
			errorParam[0] = "id";
			
			// 필수 파라미터 누락에 대한 에러메시지 전송.
			this.socketMessageHandler.sendErrorMessage(profile, this.messageHandler.getMessage("error.params.type1"));
		}
	}
	
	
	// 메시지 읽음
	public void readMessage(Profile profile, SocketData payload) {
		Map<String, Object> params = null;
		Map<String, Object> data = payload.getData();
		
		// [DB] 메세지 읽음 처리.
		params = new HashMap<String, Object>();
		params.put("roomId", payload.getRoomId());
		params.put("speakerId", profile.getSpeakerId());
		params.put("startId", DataUtils.getInt(data, "startId", 0));
		params.put("endId", DataUtils.getInt(data, "endId", 0));
		this.logger.info("payload : " + payload.toString());
		this.logger.info("params : " + params.toString());
		this.messageDao.readMessage(params);
		
		// [DB] 메세지 읽음처리 완료알림 메세지 전송.
		// data.put("success", true);
		
		// 룸 전체에 메시지 발송. 
		this.socketMessageHandler.sendMessageToBroadcast(EventName.READ_MESSAGE, profile, params);
	}
	
	
	// 상담 종료.
	public void end(Profile profile, SocketData payload) {
		if(profile.getIsCustomer() == 1) {
			endByCustomer(profile, payload);
		}else {
			endByMember(profile, payload);
		}
	}
	
	
	// 고객의 상담 종료
	public void endByCustomer(Profile profile, SocketData payload) {
		Map<String, Object> params = null;
		
		Room room = null;
		
		// [DB] 채팅상담 종료처리.
		params = new HashMap<String, Object>();
		params.put("roomId", payload.getRoomId());
		params.put("loginId", null);
		int result = this.roomDao.closeRoom(params);
		if(result > 0) {
			// [DB] 종료된 룸 정보 조회.
			room = this.roomDao.getDetail(params);
			//sendData.put("room", room);
		}
		
		Map<String, Object> sendData = new HashMap<String, Object>();
		sendData.put("success", true);
		sendData.put("isCustomer", true);
		
		// [Socket] 상담종료 메세지 전송.
		this.socketMessageHandler.sendMessageToBroadcast(EventName.END, profile, sendData);
		//this.socketMessageHandler.sendMessageToLobby(EventName.RELOAD, profile);
	}
	
	
	// 상담사 상담 종료
	public void endByMember(Profile profile, SocketData payload) {
		Map<String, Object> params = null;
		Map<String, Object> data = payload.getData();
		Map<String, Object> sendData = payload.getData();
		
		Room room = null;
		
		// [DB] 룸 종료처리.
		params = new HashMap<String, Object>();
		params.put("roomId", DataUtils.getInt(data, "roomId", 0));
		params.put("id", DataUtils.getInt(data, "roomId", 0));
		params.put("loginId", profile.getId());
		int result = this.roomDao.closeRoom(params);
		if(result > 0) {
			// [DB] 종료된 룸 정보 조회.
			room = this.roomDao.getDetail(params);
			sendData.put("room", room);
		}
		
		// [Socket] 상담종료 메세지 전송.
		this.socketMessageHandler.sendMessageToBroadcast(EventName.END, profile, sendData);
		//this.socketMessageHandler.sendMessageToLobby(EventName.RELOAD, profile);
	}
	
	
	// 메세지 더보기
	public void messageList(Profile profile, SocketData payload) {
				
		Map<String, Object> data = payload.getData();
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		// [DB] 메시지 조회
		params = new HashMap<String, Object>();
		params.put("roomId", payload.getRoomId());
		params.put("speakerId", profile.getSpeakerId());
		params.put("messageAdminType", DataUtils.getInt(data, "messageAdminType", 0));
		params.put("startId", DataUtils.getLong(data, "startId", 0));
		params.put("intervalDay", Constant.DEFAULT_MESSAGE_INTERVAL_DAY);
		params.put("pageSize", Constant.DEFAULT_MESSAGE_MORE_PAGE_SIZE);
		List<Message> messages = this.messageDao.findByRoomIdToSpeaker(params);
		
		if(messages != null && messages.size() > 0) {
			
			// [Socket] 메세지 전송.
			sendData = new HashMap<String, Object>();
			sendData.put("messages", messages);
			
			// 본인에게 메시지 발송.
			this.socketMessageHandler.sendMessageToSelf(EventName.MESSAGE_LIST, profile, sendData);
		}
	}

	
	// 상담사 채팅방 나가기.
	public void leave(SocketData payload) {
		// STOMP에서 leave는 사용하지 않고 해당 채팅방과의 Socket 연결을 해제(disconnected)한다.
	}
	
	// 고객 인증키 유효여부 확인.
	private boolean isValidSecretKey(String secretKey) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		
		String today = year + "";
		today += month < 10 ? "0" + month : "" + month;
		today += day < 10 ? "0" + day : "" + day;
		
		String compareWord = Constant.CUSTOMER_KEY + "_" + today;
		this.logger.debug("compareWord : " + compareWord);
		this.logger.debug("secretKey : " + secretKey);
		
		if(compareWord.equals(secretKey)) {
			return true;
		}
		return false;
	}
	
	// 고객 로그인
	public Profile loginCustomer(String companyId, String gasappMemberNumber, String secretKey) {
		// 기간계 서버에서 고객정보 조회.
		Map<String, Object> data = this.commonService.getCompany(companyId).getProfile(gasappMemberNumber);
		if(data == null) {
			return null;
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("companyId", companyId);
		params.put("gasappMemberNumber", gasappMemberNumber);
		params.put("name", DataUtils.getString(data, "name", ""));		//  이름
		params.put("telNumber", DataUtils.getString(data, "handphone", "").replaceAll("-", ""));	// 휴대폰번호
		
		// 고객정보 등록 - 이미 등록되어 있더라도 해당 프로시저를 실행하여야 함 - (기타 작업 존재)
		this.customerDao.regist(params);
		
		// 고객정보 조회.
		VCustomer customer = this.customerDao.findByGassappMemberNumber(params);
		this.logger.debug("customer : " + customer);
		
		Profile profile = null;
		if(customer != null) {
			profile = new Profile();
			profile.setId(Integer.parseInt(gasappMemberNumber));
			profile.setCompanyId(companyId);
			profile.setIsAdmin(0);
			profile.setIsMember(0);
			profile.setIsCustomer(1);
			profile.setLoginName(customer.getGasappMemberNumber());
			profile.setName(customer.getName());
			profile.setSpeakerId(customer.getSpeakerId());
			profile.setAuthenticated(isValidSecretKey(secretKey));
			profile.setNoReadCount(customer.getNoReadCount());
			profile.setEndDays(customer.getEndDays());
			profile.setState(customer.getState());
			profile.setRoomState(customer.getRoomState());

		}
		
		return profile;
	}
	
	
	// 회원 로그인
	public Profile loginMember(String token) {
		Member member = this.loginService.getMember(token);
		
		Profile profile = null;
		if(member != null) {
			profile = new Profile();
			profile.setId(member.getId());
			profile.setCompanyId(member.getCompanyId());
			profile.setIsAdmin(0);
			profile.setIsMember(1);
			profile.setIsCustomer(0);
			profile.setName(member.getName());
			profile.setLoginName(member.getLoginName());
			profile.setSpeakerId(member.getSpeakerId());
		}
		
		return profile;
	}
	
	public void unsubscribe(SessionUnsubscribeEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		this.logger.info("[Unsubscribe] headerAccessor : " + headerAccessor.toString());

		//MessageHeaders headers = headerAccessor.getMessageHeaders();
		//this.logger.info("unsubscribe headers : " + headers.toString());
		
		//String sessionId = (String) headers.get("simpSessionId");
		String sessionId = headerAccessor.getSessionId();
		String roomId = this.chatRoomRepository.getUserJoinRoomId(sessionId);
		Profile profile = this.chatRoomRepository.getProfileBySessionId(sessionId);
		
		this.logger.info("roomId : " + roomId);
		this.logger.info("sessionId : " + sessionId);
		
		if (roomId != null) {
			
			if(this.chatRoomRepository.getUserCount(roomId) > 0) {
				// [Redis] 채팅방의 인원수 -1.
				this.chatRoomRepository.minusUserCount(roomId);
			}
			
			// [Redis] 채팅에 참여중인 인원 확인.
			this.logger.info("usercount : " + this.chatRoomRepository.getUserCount(roomId));
			if(this.chatRoomRepository.getUserCount(roomId) <= 0) {
				
				// [Redis] 채팅방에 조인된 사람이 없다면 데이터 삭제 - Redis에 데이터 누적 방지.
				// this.chatRoomRepository.deleteChatRoom(roomId);
				// this.chatRoomRepository.deleteUserCount(roomId);
			}
				
			if(profile.getIsCustomer() == 1) {
				// 고객 조인.
				//this.chatRoomRepository.removeCustomerJoin(roomId);
				
			}else {
				// 멤버 조인.
				//this.chatRoomRepository.removeMemberJoin(roomId);
			}
			
			// [Redis] 퇴장한 클라이언트의 roomId 매핑정보 삭제.
			this.chatRoomRepository.removeUserJoinInfo(sessionId);
		}
		
	}
	
	
	// 연결해제.
	public void disconnect(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		MessageHeaders headers = headerAccessor.getMessageHeaders();
		String sessionId = (String) headers.get("simpSessionId");
		
		this.logger.info("Disconnected : " + sessionId);
		if (this.chatRoomRepository.getUserJoinRoomId(sessionId) != null) {
			String roomId = this.chatRoomRepository.getUserJoinRoomId(sessionId).replaceAll(Constant.SOCKET_ROOM_PREFIX, "");
			Profile profile = this.chatRoomRepository.getProfileBySessionId(sessionId);
			
			if (profile.getIsCustomer() == 1) {
				// [DB] 채팅방을 오프라인상태로 변경.
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", roomId);
				params.put("isOnline", 0);
				this.roomDao.updateOnline(params);
				
				// [Socket] 상담목록 갱신요청 메세지 전송.
				Map<String ,Object> sendData = new HashMap<String, Object>(); 
				sendData.put("profile", profile);
				this.socketMessageHandler.sendMessageToLobby(EventName.OFFLINE, profile, sendData);
			}
			
			if(this.chatRoomRepository.getUserCount(roomId) > 0) {
			
				// [Redis] 채팅방의 인원수 -1.
				this.chatRoomRepository.minusUserCount(roomId);
			}
				
			// [Redis] 채팅에 참여중인 인원 확인.
			if(this.chatRoomRepository.getUserCount(roomId) <= 0) {
				
				// [Redis] 채팅방에 조인된 사람이 없다면 데이터 삭제 - Redis에 데이터 누적 방지.
				this.chatRoomRepository.deleteChatRoom(roomId);
				this.chatRoomRepository.deleteUserCount(roomId);
			}
			
			if(profile.getIsCustomer() == 1) {
				// 고객 조인.
				this.chatRoomRepository.removeCustomerJoin(roomId);
				
			}else {
				// 멤버 조인.
				this.chatRoomRepository.removeMemberJoin(roomId);
			}
			
			// [Redis] 퇴장한 클라이언트의 roomId 매핑정보 삭제.
			this.chatRoomRepository.removeUserJoinInfo(sessionId);
			
			// [Redis] 커네션한 유저 프로필삭제.
			this.chatRoomRepository.dropProfileBySessionId(sessionId);
		}
	}
	
	
	// 로비룸 조회
	public String getLobbyRoom(String companyId) {
		return Constant.SOCKET_LOBBY_ROOM + companyId;
	}
	
	// 운영중인 룸 정보 조회
	public List<ChatRoom> findRooms(HttpServletRequest request, HttpServletResponse response){
		List<ChatRoom> rooms = this.chatRoomRepository.findAllRoom();
		return rooms;
	}
	
	public String getMemberSessionId(String roomId) {
		return this.chatRoomRepository.getMemberSessionIdByRoomId(roomId);
	}
	
	public String getCustomerSessionId(String roomId) {
		return this.chatRoomRepository.getCustomerSessionIdByRoomId(roomId);
	}
	
	public Profile getMemberWithSettings(SimpMessageHeaderAccessor accessor, SocketData socketData) {
		String sessionId = accessor.getSessionId();
		
		String roomId = this.chatRoomRepository.getUserJoinRoomId(sessionId);
		Profile profile = this.chatRoomRepository.getProfileBySessionId(sessionId);
		
		this.logger.info("sessionId : " + sessionId);
		this.logger.info("roomId : " + roomId);
		this.logger.info("profile : " + profile);
		
		return profile;
	}
	
}