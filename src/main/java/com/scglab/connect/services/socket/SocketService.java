package com.scglab.connect.services.socket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.MessageHeaders;
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
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.services.message.Message;
import com.scglab.connect.services.message.MessageDao;
import com.scglab.connect.services.room.Room;
import com.scglab.connect.services.room.RoomDao;
import com.scglab.connect.utils.DataUtils;

import lombok.RequiredArgsConstructor;

@SuppressWarnings({"rawtypes", "unchecked"})
@RequiredArgsConstructor
@Service
public class SocketService {
	
	Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);
	
	private final ChannelTopic channelTopic;
	private final RedisTemplate redisTemplate;
	private final ChatRoomRepository chatRoomRepository;
	
	@Autowired private CommonService commonService;
	@Autowired private LoginService loginService;
	@Autowired private PushService pushService;
	@Autowired private RoomDao roomDao;
	@Autowired private MessageDao messageDao;
	@Autowired private MessageHandler messageHandler;
	@Autowired private AutoMessageDao autoMessageDao;
	@Autowired private JwtService jwtService;
	
	// 이벤트메세지 유형.
	public enum EventName {
		ERROR,
		LOGIN,
		READ_MESSAGE,
		ROOM_DETAIL,
		ASSIGN, ASSIGNED, 
		JOIN, JOINED,
		AUTO_MESSAGE,
		WELCOME_MESSAGE, START_MESSAGE,
		MESSAGE, MESSAGE_LIST,
		SAVE_HISTORY,
		DELETE_MESSAGE,
		LEAVE,
		END,
		RELOAD
	}
	
	public enum Target{
		ALL,		// 전체 수신.
		MEMBER,		// 멤버만 수신.
		CUSTOMER	// 고객만 수신.
	}
	
	// 프로필에 해당하는 대상(Target) 추출
	private Target getProfileTarget(Member profile) {
		return profile.getIsCustomer() == 1 ? Target.CUSTOMER : Target.MEMBER;
	}
	
	// 에러 이벤트메세지 전송.
	public void sendErrorMessage(String companyId, String roomId, Object profile, String reason, SocketData payload) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("reason", reason);
		data.put("payload", payload);
		data.put("profile", profile);
		
		SocketData socketData = new SocketData();
		socketData.setEventName(EventName.ERROR);
		socketData.setRoomId(roomId);
		socketData.setTarget(Target.MEMBER);
		socketData.setCompanyId(companyId);
		socketData.setData(data);
		socketData.setToken(null);
		
		this.logger.error("Send error message > " + socketData.toString());
		this.redisTemplate.convertAndSend(channelTopic.getTopic(), socketData);
	}
	
	// 새로고침 이벤트메세지 전송.
	public void sendReloadMessage(String companyId) {
		SocketData socketData = new SocketData();
		socketData.setEventName(EventName.RELOAD);
		socketData.setRoomId(getLobbyRoom(companyId));
		socketData.setTarget(Target.MEMBER);
		socketData.setCompanyId(companyId);
		socketData.setData(null);
		socketData.setToken(null);

		this.logger.info("Send reload message > " + socketData.toString());
		this.redisTemplate.convertAndSend(channelTopic.getTopic(), socketData);
	}
	
	// 이벤트 메세지 전송.
	public void sendMessage(EventName eventName, String companyId, String roomId, Target target, Map<String, Object> data) {
		SocketData socketData = new SocketData();
		socketData.setEventName(eventName);
		socketData.setRoomId(roomId);
		socketData.setTarget(target);
		socketData.setCompanyId(companyId);
		socketData.setData(data);
		socketData.setToken(null);
		
		this.logger.info("Send message > " + socketData.toString());
		this.redisTemplate.convertAndSend(channelTopic.getTopic(), socketData);
	}
	
	// 연결처리.
	
	public void connect(SessionConnectedEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		MessageHeaders headers = headerAccessor.getMessageHeaders();
		
		String sessionId = (String) headers.get("simpSessionId");
		String token = "";
		
		Map<String, Object> nativeHeaders = null;
		if (headers.containsKey("simpConnectMessage")) {
			GenericMessage simpConnectMessage = (GenericMessage) headers.get("simpConnectMessage");
			Map<String, Object> subHeaders = simpConnectMessage.getHeaders();
			nativeHeaders = (Map<String, Object>) subHeaders.get("nativeHeaders");

			if (nativeHeaders.containsKey("Authorization")) {
				token = ((List<String>) nativeHeaders.get("Authorization")).get(0);
			}
		}
		
		// [Redis] 연결된 세션ID와 Token을 저장.
		this.chatRoomRepository.setUserToken(sessionId, token);
	}
	
	// 구독처리.
	public void subscribe(SessionSubscribeEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		this.logger.info("[Subscribe] headerAccessor : " + headerAccessor.toString());

		MessageHeaders headers = headerAccessor.getMessageHeaders();
		String path = (String) headers.get("simpDestination");
		String roomId = path.replaceAll("/sub" + Constant.SOCKET_ROOM_PREFIX + "/", "");
		String sessionId = (String) headers.get("simpSessionId");
		
		this.logger.info("roomId : " + roomId);
		this.logger.info("sessionId : " + sessionId);

		// [Redis] 채팅방에 들어온 클라이언트 sessionId를 roomId와 매핑.
		this.chatRoomRepository.setUserJoinInfo(sessionId, roomId);

		// [Redis] 해당 채팅방의 참여인원수 1증가.
		this.chatRoomRepository.plusUserCount(roomId);
		
		// [Redis] SessionId에 대한 토큰 추출
		String token = this.chatRoomRepository.getUserToken(sessionId);

		// 토큰에서 인증정보 추출.
		//Member profile = this.loginService.getMember(token);
		Member profile = getProfile(token);
		SocketData payload = new SocketData();
		payload.setEventName(EventName.JOIN);
		payload.setRoomId(roomId);
		payload.setCompanyId(profile.getCompanyId());
		payload.setToken(token);
		payload.setData(DataUtils.convertMap(profile));
		
		// 조인처리.
		join(payload);
	}
	
	public void unsubscribe(SessionUnsubscribeEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		this.logger.info("[Unsubscribe] headerAccessor : " + headerAccessor.toString());

		MessageHeaders headers = headerAccessor.getMessageHeaders();
		this.logger.info("unsubscribe headers : " + headers.toString());
		
		String sessionId = (String) headers.get("simpSessionId");
		String roomId = this.chatRoomRepository.getUserJoinRoomId(sessionId);
		
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
				this.chatRoomRepository.deleteChatRoom(roomId);
				this.chatRoomRepository.deleteUserCount(roomId);
			}
				
			// [Redis] 퇴장한 클라이언트의 roomId 매핑정보 삭제.
			this.chatRoomRepository.removeUserJoinInfo(sessionId);
		}
		
	}
	
	
	// 방 상세 조회
	public void roomDetail(SocketData payload) {
		// 사용자정보 추출.
		Member profile = getProfile(payload.getToken());
		
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
			sendMessage(EventName.ROOM_DETAIL, payload.getCompanyId(), payload.getRoomId(), getProfileTarget(profile), sendData);
		}else {
			
			// [Socket] 오류 알림 메세지 전송. - 일치하는 채팅상담이 존재하지 않음.
			sendErrorMessage(payload.getCompanyId(), payload.getRoomId(), profile, this.messageHandler.getMessage("error.room.room-detail"), payload);
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
	public void assign(SocketData payload) {
		// 사용자정보 추출.
		Member profile = getProfile(payload.getToken());
		
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
		sendData.put("roomId", room.getId());
		sendData.put("speakerId", profile.getSpeakerId());
		sendData.put("room", room);
		sendMessage(EventName.ASSIGNED, payload.getCompanyId(), room.getId() + "", getProfileTarget(profile), sendData);
	}

	// 방 조인
	public void join(SocketData payload) {
		
		// 사용자정보 추출.
		Member profile = getProfile(payload.getToken());
		this.logger.debug("profile : " + profile.toString());
		
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		String lobbyRoom = getLobbyRoom(payload.getCompanyId());
		
		// 로비에 조인일 경우 - 조인만 시키고 아무일도 없음.
		if(lobbyRoom.equals(payload.getRoomId())) {
			this.logger.debug("Lobby join : " + payload.toString());
			return;
		}
		
		// [DB] 채팅방 정보 조회
		params = new HashMap<String, Object>();
		params.put("id", payload.getRoomId());
		Room room = this.roomDao.getDetail(params);
		this.logger.debug("room : " + room);
		
		// [DB] 채팅방에 조인 처리.
		params = new HashMap<String, Object>();
		params.put("roomId", payload.getRoomId());
		params.put("speakerId", profile.getSpeakerId());
		Map<String, Object> joinResult = this.roomDao.joinRoom(params);
		
		Long maxMessageId = DataUtils.getLong(joinResult, "maxMessageId", 0);	// 마지막 메세지id
		Long readLastMessageId = DataUtils.getLong(joinResult, "readLastMessageId", 0);	// 마지막 읽음메세지id
		
		// 마지막 메세지가 존재하고, 마지막 메세지와 마지막 읽음메세지가 같지 않을경우 Read 메세지 전송.
		if(maxMessageId > 0 && (maxMessageId != readLastMessageId)) {	
			
			// [Socket] > 읽음 메세지 전송.
			sendData = new HashMap<String, Object>();
			sendData.put("roomId", payload.getRoomId());
			sendData.put("speakerId", profile.getSpeakerId());
			sendData.put("startId", readLastMessageId);
			sendData.put("endId", maxMessageId);
			sendMessage(EventName.READ_MESSAGE, payload.getCompanyId(), payload.getRoomId(), getProfileTarget(profile), sendData);
		}
		
		// [Socket] > 채팅방 참여자들에게 조인완료 메세지 전송.
		sendData = new HashMap<String, Object>();
		sendData.put("member", profile);
		sendMessage(EventName.JOINED, payload.getCompanyId(), payload.getRoomId(), Target.ALL, sendData);
		
		
		
		// 고객여부 판단.
		if(profile.getIsCustomer() == 1) {
			// 고객일 경우.
			
			// [DB] 채팅방을 온라인상태로 변경.
			params = new HashMap<String, Object>();
			params.put("id", payload.getRoomId());
			params.put("isOnline", 1);
			this.roomDao.updateOnline(params);
			
			// 채팅방의 상태가 최초이거나 종료일 경우 시작메세지 준비.
			if(room.getState() == 0 || room.getState() == 2) {	
				
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
				params.put("speakerId", profile.getSpeakerId());
				params.put("messageType", 0);		// 메세지 유형 (0-일반, 1-이미지, 2-동영상, 3-첨부, 4-링크, 5-이모티콘)
				params.put("isSystemMessage", 1);
				params.put("message", startMessage);
				params.put("messageAdminType", 0);	// 시스템 메세지의 다른 유형. (0-일반 메세지, 1-시스템 메세지)
				params.put("isEmployee", 1);
				params.put("messageDetail", "");
				params.put("templateId", "");
				Message newMessage = this.messageDao.create(params);
				
				// [Socket] 시작메시지 전송.
				sendData = new HashMap<String, Object>();
				sendData.put("message", newMessage);
				sendMessage(EventName.START_MESSAGE, payload.getCompanyId(), payload.getRoomId(), Target.CUSTOMER, sendData);
				
			}
			
			// [DB] 상담사 배정지연 안내메세지 조회
			params = new HashMap<String, Object>();
			params.put("type", 1);
			AutoMessage autoMessage1 = this.autoMessageDao.getAutoMessageByMatchWait(params);
			
			// [DB] 답변지연 안내메세지 조회
			params = new HashMap<String, Object>();
			params.put("type", 2);
			AutoMessage autoMessage2 = this.autoMessageDao.getAutoMessageByMatchWait(params);
			
			// [Socket] 상담사 배정지연 및 답변지연 안내 메세지 전송. - 메세지를 미리 보내주고 알맞는 시기에 사용할 수 있도록 한다.
			sendData = new HashMap<String, Object>();
			sendData.put("autoMessage1", autoMessage1);
			sendData.put("autoMessage2", autoMessage2);
			sendMessage(EventName.WELCOME_MESSAGE, payload.getCompanyId(), payload.getRoomId(), Target.CUSTOMER, sendData);
		}
		
		// [DB] 대화내용 조회
		params = new HashMap<String, Object>();
		params.put("roomId", payload.getRoomId());
		params.put("speakerId", profile.getSpeakerId());
		params.put("intervalDay", Constant.DEFAULT_MESSAGE_INTERVAL_DAY);
		params.put("pageSize", Constant.DEFAULT_MESSAGE_MORE_PAGE_SIZE);
		List<Message> messages = this.messageDao.findByRoomIdToSpeaker(params);
		
		if(messages != null && messages.size() > 0) {		// 이전 메세지가 존재할 경우.
			
			// [Socket] 이전 대화내용 전송.
			sendData = new HashMap<String, Object>();
			sendData.put("messages", messages);
			sendMessage(EventName.MESSAGE_LIST, payload.getCompanyId(), payload.getRoomId(), getProfileTarget(profile), sendData);
		}
		
		// [Socket] 상담목록 갱신요청 메세지 전송.
		sendReloadMessage(payload.getCompanyId());
	}
	
	
	
	// 메세지 전송
	public void message(SocketData payload) {
		
		// 사용자정보 추출.
		Member profile = getProfile(payload.getToken());
		
		Map<String, Object> data = payload.getData();
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		// [DB] 채팅방 정보 조회
		params = new HashMap<String, Object>();
		params.put("id", payload.getRoomId());
		Room room = this.roomDao.getDetail(params);
		
		String templateId = DataUtils.getString(data, "templateId", "");
		
		// [DB] 신규 메세지 생성.
		params = new HashMap<String, Object>();
		params.put("companyId", payload.getCompanyId());
		params.put("roomId", payload.getRoomId());
		params.put("speakerId", profile.getSpeakerId());
		params.put("messageType", DataUtils.getInt(data, "messageType", 0));		// 메세지 유형 (0-일반, 1-이미지, 2-동영상, 3-첨부, 4-링크, 5-이모티콘)
		params.put("isSystemMessage", DataUtils.getInt(data, "isSystemMessage", 0));
		params.put("message", DataUtils.getString(data, "message",""));
		params.put("messageAdminType", DataUtils.getString(data, "messageAdminType","0"));	// 시스템 메세지의 다른 유형. (0-일반 메세지, 1-시스템 메세지)
		params.put("isEmployee", profile.getIsCustomer() == 1 ? 0 : 1);
		params.put("messageDetail", DataUtils.getString(data, "messageDetail",""));
		params.put("templateId", templateId.equals("") ? null : templateId);
		
		Message newMessage = this.messageDao.create(params);
		
		if(newMessage != null) {
			
			// [Socket] 생성된 메세지 전송.
			sendData = new HashMap<String, Object>();
			sendData.put("message", newMessage);
			sendMessage(EventName.MESSAGE, payload.getCompanyId(), payload.getRoomId(), Target.ALL, sendData);
			
			// 고객이 작성한 메세지일 경우.
			if(profile.getIsCustomer() == 1) {
				
				// 조인 메세지id와 마지막 생성된 메세지가 동일한 경우.
				//if(newMessage.getJoinMessageId() == newMessage.getId()) {
					sendReloadMessage(payload.getCompanyId());
				//}
				
			}else {	// 상담사의 메세지일 경우.
				
				// 채팅방의 상태가 오프라인일 경우.
				if(room.getIsOnline() == 0) {
					// TODO : 푸시 메세지 발송.
					//this.pushService.sendPush(0, this.messageHandler.getMessage("push.replay"));
					
				}
			}
		}
	}
	
	
	// 챗봇 이력저장
	public void saveHistory(SocketData payload) {
		Map<String, Object> data = payload.getData();
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		String roomId = payload.getRoomId();
		String history = DataUtils.getString(data, "history", "");
		
		// [DB] 이력저장.
		params = new HashMap<String, Object>();
		params.put("roomId", roomId);
		params.put("history", history);
		int result = this.roomDao.updateJoinHistory(params);
		
		// [Socket] 이력저장 완료메세지 전송.
		sendData = new HashMap<String, Object>();
		sendData.put("success", result > 0 ? true : false);
		sendMessage(EventName.SAVE_HISTORY, payload.getCompanyId(), payload.getRoomId(), Target.MEMBER, sendData);
	}
	
	
	// 메세지 삭제
	public void deleteMessage(SocketData payload) {
		// 사용자정보 추출.
		Member profile = getProfile(payload.getToken());
				
		Map<String, Object> data = payload.getData();
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		// 메시지 id
		long id = DataUtils.getLong(data, "id", 0);
		
		if(id > 0) {
			params = new HashMap<String, Object>();
			params.put("id", id);
			
			// [DB] 메세지 읽음 카운트 조회.
			int readCount = this.messageDao.getReadCountByMessageId(params);
			if(readCount <= 0) {
				// [Socket] 메세지를 이미 읽은상태이기에 삭제불가 알림 전송.
				sendErrorMessage(payload.getCompanyId(), payload.getRoomId(), profile, this.messageHandler.getMessage("error.message.delete"), payload);
			
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
				sendMessage(EventName.DELETE_MESSAGE, payload.getCompanyId(), payload.getRoomId(), getProfileTarget(profile), sendData);
			}
			
		}else {
			String[] errorParam = new String[1];
			errorParam[0] = "id";
			
			// 필수 파라미터 누락에 대한 에러메시지 전송.
			sendErrorMessage(payload.getCompanyId(), payload.getRoomId(), profile, this.messageHandler.getMessage("error.params.type1"), payload);
		}
	}
	
	
	// 메시지 읽음
	public void readMessage(SocketData payload) {
		// 사용자정보 추출.
		Member profile = getProfile(payload.getToken());
				
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		// [DB] 메세지 읽음 처리.
		params = new HashMap<String, Object>();
		params.put("roomId", payload.getRoomId());
		params.put("speakerId", profile.getSpeakerId());
		params.put("startId", DataUtils.getLong(params, "startId", 0));
		params.put("endId", DataUtils.getLong(params, "endId", 0));
		this.messageDao.readMessage(params);
		
		// [DB] 메세지 읽음처리 완료알림 메세지 전송.
		sendData = new HashMap<String, Object>();
		sendData.put("success", true);
		sendMessage(EventName.READ_MESSAGE, payload.getCompanyId(), payload.getRoomId(), Target.ALL, sendData);
	}
	
	
	// 상담 종료.
	public void end(SocketData payload) {
		// 사용자정보 추출.
		Member profile = getProfile(payload.getToken());
		
		if(profile.getIsCustomer() == 1) {
			endByCustomer(payload);
		}else {
			endByEmp(payload);
		}
	}
	
	
	// 고객의 상담 종료
	public void endByCustomer(SocketData payload) {
		Member customer = getProfile(payload.getToken());
		
		Map<String, Object> params = null;
		
		// [DB] 채팅상담 종료처리.
		params = new HashMap<String, Object>();
		params.put("roomId", payload.getRoomId());
		params.put("loginId", customer.getId());
		this.roomDao.closeRoom(params);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("success", true);
		
		// [Socket] 상담종료 메세지 전송.
		sendMessage(EventName.END, payload.getCompanyId(), payload.getRoomId(), Target.ALL, data);
	}
	
	
	// 상담사 상담 종료
	public void endByEmp(SocketData payload) {
		Member member = getProfile(payload.getToken());
		
		Map<String, Object> params = null;
		Map<String, Object> data = payload.getData();
		Map<String, Object> sendData = payload.getData();
		
		
		Room room = null;
		
		// [DB] 룸 종료처리.
		params = new HashMap<String, Object>();
		params.put("roomId", DataUtils.getInt(data, "roomId", 0));
		params.put("id", DataUtils.getInt(data, "roomId", 0));
		params.put("loginId", member.getId());
		int result = this.roomDao.closeRoom(params);
		if(result > 0) {
			
			// [DB] 종료된 룸 정보 조회.
			room = this.roomDao.getDetail(params);
			sendData.put("room", room);
		}
		
		// [Socket] 상담종료 메세지 전송.
		sendMessage(EventName.END, payload.getCompanyId(), room.getId() + "", Target.ALL, sendData);
		sendReloadMessage(payload.getCompanyId());
	}
	
	
	// 메세지 더보기
	public void messageList(SocketData payload) {
		// 사용자정보 추출.
		Member profile = getProfile(payload.getToken());
				
		Map<String, Object> data = payload.getData();
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		// [DB] 메시지 조회
		params = new HashMap<String, Object>();
		params.put("roomId", payload.getRoomId());
		params.put("speakerId", profile.getSpeakerId());
		params.put("messageAdminType", DataUtils.getInt(data, "messageAdminType", 0));
		params.put("startId", DataUtils.getLong(params, "startId", 0));
		params.put("intervalDay", Constant.DEFAULT_MESSAGE_INTERVAL_DAY);
		params.put("pageSize", Constant.DEFAULT_MESSAGE_MORE_PAGE_SIZE);
		List<Message> messages = this.messageDao.findByRoomIdToSpeaker(params);
		
		if(messages != null && messages.size() > 0) {
			
			// [Socket] 메세지 전송.
			sendData = new HashMap<String, Object>();
			sendData.put("messages", messages);
			sendMessage(EventName.MESSAGE_LIST, payload.getCompanyId(), payload.getRoomId(), getProfileTarget(profile), sendData);
		}
	}
	
	
	// 연결해제.
	public void disconnect(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		MessageHeaders headers = headerAccessor.getMessageHeaders();
		String sessionId = (String) headers.get("simpSessionId");
		
		if (this.chatRoomRepository.getUserJoinRoomId(sessionId) != null) {
			String roomId = this.chatRoomRepository.getUserJoinRoomId(sessionId).replaceAll(Constant.SOCKET_ROOM_PREFIX, "");
			String token = this.chatRoomRepository.getUserToken(sessionId);
			
			// 사용자정보 추출.
			Member profile = getProfile(token);

			if (profile.getIsCustomer() == 1) {
				// [DB] 채팅방을 오프라인상태로 변경.
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", roomId);
				params.put("isOnline", 0);
				this.roomDao.updateOnline(params);
				
				// [Socket] 상담목록 갱신요청 메세지 전송.
				sendReloadMessage(profile.getCompanyId());
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
			
			// [Redis] 퇴장한 클라이언트의 roomId 매핑정보 삭제.
			this.chatRoomRepository.removeUserJoinInfo(sessionId);
			
			// [Redis] 커네션한 유저 토큰 삭제.
			this.chatRoomRepository.dropUserToken(sessionId);
		}
	}
	
	
	// 상담사 채팅방 나가기.
	public void leave(SocketData payload) {
		// STOMP에서 leave는 사용하지 않고 해당 채팅방과의 Socket 연결을 해제(disconnected)한다.
	}
	
	
	// 로그인.
	public void login(SocketData payload) {
		// 토큰에서 사용자정보 추출.
		Member profile = this.loginService.getMember(payload.getToken());
		
		if(profile.getIsCustomer() == 1) {
			loginCustomer(payload);
		}else {
			loginMember(payload);
		}
	}
	
	
	// 고객 로그인
	public void loginCustomer(SocketData socketData) {
	}
	
	
	// 회원 로그인
	public void loginMember(SocketData socketData) {
	}
	
	
	// 로비룸 조회
	public String getLobbyRoom(String companyId) {
		return "LOBBY" + companyId;
	}
	
	// 운영중인 룸 정보 조회
	public List<ChatRoom> findRooms(HttpServletRequest request, HttpServletResponse response){
		List<ChatRoom> rooms = this.chatRoomRepository.findAllRoom();
		return rooms;
	}
	
}



