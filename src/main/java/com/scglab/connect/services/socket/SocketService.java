package com.scglab.connect.services.socket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.scglab.connect.base.interceptor.CommonInterceptor;
import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.automessage.AutoMessage;
import com.scglab.connect.services.automessage.AutoMessageDao;
import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.common.service.PushService;
import com.scglab.connect.services.company.external.ICompany;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.services.message.Message;
import com.scglab.connect.services.message.MessageDao;
import com.scglab.connect.services.room.Room;
import com.scglab.connect.services.room.RoomDao;
import com.scglab.connect.services.talk.ChatRoomRepository;
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
	
	// 이벤트메세지 유형.
	public enum EventName {
		ERROR,
		LOGIN,
		READ_MESSAGE,
		ROOM_DETAIL,
		JOIN, JOINED,
		MESSAGE, MESSAGE_LIST,
		SAVE_HISTORY,
		DELETE_MESSAGE,
		LEAVE,
		END,
		RELOAD
	}
	
	// 에러 이벤트메세지 전송.
	public void sendErrorMessage(String roomId, int speakerId, String reason, SocketData socketData) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("eventName", EventName.ERROR);
		data.put("roomId", roomId);
		data.put("socketData", socketData);
		data.put("reason", reason);
		data.put("targetSpeakerId", speakerId);
		
		this.logger.error("Send error message > " + data.toString());
		this.redisTemplate.convertAndSend(channelTopic.getTopic(), data);
	}
	
	// 새로고침 이벤트메세지 전송.
	public void sendReloadMessage(String companyId) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("eventName", EventName.RELOAD);
		data.put("roomId", getLobbyRoom(companyId));
		
		this.logger.info("Send reload message > " + data.toString());
		this.redisTemplate.convertAndSend(channelTopic.getTopic(), data);
	}
	
	// 이벤트 메세지 전송.
	public void sendMessage(EventName eventName, String companyId, String roomId, Map<String, Object> data) {
		if(data == null) {
			data = new HashMap<String, Object>();
		}
		data.put("eventName", eventName);
		data.put("roomId", roomId);
		
		this.logger.info("Send message > " + data.toString());
		this.redisTemplate.convertAndSend(channelTopic.getTopic(), data);
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
		String roomId = path.replaceAll(Constant.SOCKET_ROOM_PREFIX, "");
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
		Member profile = this.loginService.getMember(token);
		SocketData socketData = new SocketData();
		socketData.setEventName(EventName.JOIN);
		socketData.setRoomId(roomId);
		socketData.setCompanyId(profile.getCompanyId());
		socketData.setToken(token);
		socketData.setData(DataUtils.convertMap(profile));
		
		// 조인처리.
		join(socketData);
	}
	
	
	// 방 상세 조회
	public void roomDetail(SocketData socketData) {
		// 토큰에서 사용자정보 추출.
		Member profile = this.loginService.getMember(socketData.getToken());
		
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		// [DB] 방 상세정보 조회
		params = new HashMap<String, Object>();
		params.put("id", socketData.getRoomId());
		Room room = this.roomDao.getDetail(params);
		
		if(room != null) {
			
			// [Socket] 방 상세 정보 전송.
			sendData = new HashMap<String, Object>();
			sendData.put("room", room);
			sendMessage(EventName.ROOM_DETAIL, socketData.getCompanyId(), socketData.getRoomId(), sendData);
		}else {
			
			// [Socket] 오류 알림 메세지 전송. - 일치하는 채팅상담이 존재하지 않음.
			sendErrorMessage(socketData.getRoomId(), profile.getSpeakerId(), this.messageHandler.getMessage("error.room.room-detail"), socketData);
		}
	}
	
	// 방 조인
	public void join(SocketData socketData) {
		// 토큰에서 사용자정보 추출.
		Member profile = this.loginService.getMember(socketData.getToken());
		
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		String lobbyRoom = getLobbyRoom(socketData.getCompanyId());
		
		// 로비에 조인일 경우 - 조인만 시키고 아무일도 없음.
		if(lobbyRoom.equals(socketData.getRoomId())) {
			this.logger.debug("Lobby join : " + socketData.toString());
			return;
		}
		
		// [DB] 채팅방에 조인 처리.
		params = new HashMap<String, Object>();
		params.put("roomId", socketData.getRoomId());
		params.put("speakerId", profile.getSpeakerId());
		Map<String, Object> joinResult = this.roomDao.joinRoom(params);
		
		Long maxMessageId = DataUtils.getLong(joinResult, "maxMessageId", 0);	// 마지막 메세지id
		Long readLastMessageId = DataUtils.getLong(joinResult, "readLastMessageId", 0);	// 마지막 읽음메세지id
		
		// 마지막 메세지가 존재하고, 마지막 메세지와 마지막 읽음메세지가 같지 않을경우 Read 메세지 전송.
		if(maxMessageId > 0 && (maxMessageId != readLastMessageId)) {	
			
			// [Socket] > 읽음 메세지 전송.
			sendData = new HashMap<String, Object>();
			sendData.put("roomId", socketData.getRoomId());
			sendData.put("speakerId", profile.getSpeakerId());
			sendData.put("startId", readLastMessageId);
			sendData.put("endId", maxMessageId);
			sendMessage(EventName.READ_MESSAGE, socketData.getCompanyId(), socketData.getRoomId(), sendData);
		}
		
		// [Socket] > 채팅방 참여자들에게 조인완료 메세지 전송.
		sendData = new HashMap<String, Object>();
		sendData.put("member", profile);
		sendMessage(EventName.JOINED, socketData.getCompanyId(), socketData.getRoomId(), sendData);
		
		// [DB] 채팅방 정보 조회
		params = new HashMap<String, Object>();
		params.put("id", socketData.getRoomId());
		Room room = this.roomDao.getDetail(params);
		this.logger.debug("room : " + room);
		
		// 고객여부 판단.
		if(profile.getIsCustomer() == 1) {
			// 고객일 경우.
			
			if(room.getState() == 0 || room.getState() == 2) {	// 채팅방의 상태가 최초이거나 종료일 경우 시작메세지 준비.
				
				// 각 회사의 서비스 클래스 가져오기.
				ICompany company = this.commonService.getCompany(socketData.getCompanyId());
				
				// 시작메세지 유형
				int messageType = 0;
				
				// 회사별 근무상태 조회. (1-근무 중, 2-근무 외 시간, 3-점심시간.)
				int isWorkType = company.isWorking();
				
				if(isWorkType == 1) {	// 근무중일 경우.(1)
					
					// 로비에 상담가능한 상담사 카운트 조회. 
					Long readyMemberCount = this.chatRoomRepository.getUserCount(getLobbyRoom(socketData.getCompanyId()));
					
					Map<String, Object> msgParams = new HashMap<String, Object>();
					msgParams.put("companyId", socketData.getCompanyId());
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
				
				// [Socket] 시작메세지 조회 및 전송.
				String startMessage = this.messageHandler.getMessage("socket.startmessage.type" + messageType);				
				sendData = new HashMap<String, Object>();
				sendData.put("message", startMessage);
				sendMessage(EventName.MESSAGE, socketData.getCompanyId(), socketData.getRoomId(), sendData);
				
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
				sendMessage(EventName.MESSAGE, socketData.getCompanyId(), socketData.getRoomId(), sendData);
				
			}
			
			// [DB] 채팅방을 온라인상태로 변경.
			params = new HashMap<String, Object>();
			params.put("roomId", socketData.getRoomId());
			params.put("isOnline", 1);
			this.roomDao.updateOnline(params);
		}
		
		// [DB] 대화내용 조회
		params = new HashMap<String, Object>();
		params.put("roomId", socketData.getRoomId());
		params.put("speakerId", profile.getSpeakerId());
		params.put("intervalDay", Constant.DEFAULT_MESSAGE_INTERVAL_DAY);
		params.put("pageSize", Constant.DEFAULT_MESSAGE_MORE_PAGE_SIZE);
		List<Message> messages = this.messageDao.findByRoomIdToSpeaker(params);
		
		if(messages != null && messages.size() > 0) {		// 이전 메세지가 존재할 경우.
			
			// [Socket] 이전 대화내용 전송.
			sendData = new HashMap<String, Object>();
			sendData.put("messages", messages);
			sendMessage(EventName.MESSAGE_LIST, socketData.getCompanyId(), socketData.getRoomId(), sendData);
		}
		
		// [Socket] 상담목록 갱신요청 메세지 전송.
		sendReloadMessage(socketData.getCompanyId());
	}
	
	
	// 메세지 전송
	public void message(SocketData socketData) {
		
		// 토큰에서 사용자정보 추출.
		Member profile = this.loginService.getMember(socketData.getToken());
		
		Map<String, Object> data = socketData.getData();
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		// [DB] 채팅방 정보 조회
		params = new HashMap<String, Object>();
		params.put("id", socketData.getRoomId());
		Room room = this.roomDao.getDetail(params);
		
		// [DB] 신규 메세지 생성.
		params = new HashMap<String, Object>();
		params.put("companyId", socketData.getCompanyId());
		params.put("roomId", socketData.getRoomId());
		params.put("speakerId", profile.getSpeakerId());
		params.put("messageType", DataUtils.getInt(data, "messageType", 0));
		params.put("isSystemMessage", DataUtils.getInt(data, "isSystemMessage", 0));
		params.put("message", socketData.getRoomId());
		params.put("messageAdminType", socketData.getRoomId());
		params.put("isEmployee", profile.getIsCustomer() == 1 ? 0 : 1);
		params.put("messageDetail", DataUtils.getString(data, "messageDetail", ""));
		params.put("templateId", DataUtils.getString(data, "templateId", null));
		Message newMessage = this.messageDao.create(params);
		
		if(newMessage != null) {
			
			// [Socket] 생성된 메세지 전송.
			sendData = new HashMap<String, Object>();
			sendData.put("message", newMessage);
			sendMessage(EventName.MESSAGE, socketData.getCompanyId(), socketData.getRoomId(), sendData);
			
			// 고객이 작성한 메세지일 경우.
			if(profile.getIsCustomer() == 1) {
				
				// 조인 메세지id와 마지막 생성된 메세지가 동일한 경우.
				if(newMessage.getJoinMessageId() == newMessage.getId()) {
					sendReloadMessage(socketData.getCompanyId());
				}
				
			}else {	// 상담사의 메세지일 경우.
				
				// 채팅방의 상태가 오프라인일 경우.
				if(room.getIsOnline() == 0) {
					// TODO : 푸시 메세지 발송.
					this.pushService.sendPush(0, this.messageHandler.getMessage("push.replay"));
					
				}
			}
		}
	}
	
	
	
	// 챗봇 이력저장
	public void saveHistory(SocketData socketData) {
		Map<String, Object> data = socketData.getData();
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		String roomId = socketData.getRoomId();
		String history = DataUtils.getString(data, "history", "");
		
		// [DB] 챗봇 이력저장.
		params = new HashMap<String, Object>();
		params.put("roomId", roomId);
		params.put("history", history);
		int result = this.roomDao.updateJoinHistory(params);
		
		// [Socket] 챗봇 이력저장 완료메세지 전송.
		sendData = new HashMap<String, Object>();
		sendData.put("success", result > 0 ? true : false);
		sendMessage(EventName.SAVE_HISTORY, socketData.getCompanyId(), socketData.getRoomId(), sendData);
	}
	
	
	// 메세지 삭제
	public void deleteMessage(SocketData socketData) {
		// 토큰에서 사용자정보 추출.
		Member profile = this.loginService.getMember(socketData.getToken());
				
		Map<String, Object> data = socketData.getData();
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		long id = DataUtils.getLong(data, "id", 0);
		if(id > 0) {
			params = new HashMap<String, Object>();
			params.put("id", id);
			
			// [DB] 메세지 읽음 카운트 조회.
			int readCount = this.messageDao.getReadCountByMessageId(params);
			if(readCount < 0) {
			
				// [Socket] 메세지를 이미 읽은상태이기에 삭제불가 알림 전송.
				sendErrorMessage(socketData.getRoomId(), profile.getSpeakerId(), this.messageHandler.getMessage("error.message.delete"), socketData);
			
			}else {
				// [DB] 메세지 삭제처리.
				int result = this.messageDao.deleteMessageRead(params);
				
				//[Socket] 메세지 삭제완료 알림 전송.
				sendData = new HashMap<String, Object>();
				sendData.put("success", result > 0 ? true : false);
				sendMessage(EventName.DELETE_MESSAGE, socketData.getCompanyId(), socketData.getRoomId(), sendData);
			}
		}
	}
	
	
	// 메시지 읽음
	public void readMessage(SocketData socketData) {
		// 토큰에서 사용자정보 추출.
		Member profile = this.loginService.getMember(socketData.getToken());
				
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		// [DB] 메세지 읽음 처리.
		params = new HashMap<String, Object>();
		params.put("roomId", socketData.getRoomId());
		params.put("speakerId", profile.getSpeakerId());
		params.put("startId", DataUtils.getLong(params, "startId", 0));
		params.put("endId", DataUtils.getLong(params, "endId", 0));
		//int result = this.messageDao.readMessage(params);
		int result = 0;
		
		// [DB] 메세지 읽음처리 완료알림 메세지 전송.
		sendData = new HashMap<String, Object>();
		sendData.put("success", result > 0 ? true : false);
		sendMessage(EventName.READ_MESSAGE, socketData.getCompanyId(), socketData.getRoomId(), sendData);
	}
	
	
	// 상담 종료.
	public void end(SocketData socketData) {
		// 토큰에서 사용자정보 추출.
		Member profile = this.loginService.getMember(socketData.getToken());
		
		if(profile.getIsCustomer() == 1) {
			endByCustomer(socketData);
		}else {
			endByEmp(socketData);
		}
	}
	
	
	// 고객의 상담 종료
	public void endByCustomer(SocketData socketData) {
		Map<String, Object> params = null;
		
		// [DB] 채팅상담 종료처리.
		params = new HashMap<String, Object>();
		params.put("roomId", socketData.getRoomId());
		params.put("loginId", null);
		this.roomDao.closeRoom(params);
		
		// [Socket] 상담종료 메세지 전송.
		sendMessage(EventName.END, socketData.getCompanyId(), socketData.getRoomId(), null);
	}
	
	
	// 상담사 상담 종료
	public void endByEmp(SocketData socketData) {
		
	}
	
	
	// 메세지 더보기
	public void messageList(SocketData socketData) {
		// 토큰에서 사용자정보 추출.
		Member profile = this.loginService.getMember(socketData.getToken());
				
		Map<String, Object> data = socketData.getData();
		Map<String, Object> sendData = null;
		Map<String, Object> params = null;
		
		// [DB] 메시지 조회
		params = new HashMap<String, Object>();
		params.put("roomId", socketData.getRoomId());
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
			sendMessage(EventName.MESSAGE_LIST, socketData.getCompanyId(), socketData.getRoomId(), sendData);
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
			Member profile = this.loginService.getMember(token);

			if (profile.getIsCustomer() == 1) {
				// [DB] 채팅방을 오프라인상태로 변경.
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("roomId", roomId);
				params.put("isOnline", 0);
				this.roomDao.updateOnline(params);
				
				// [Socket] 상담목록 갱신요청 메세지 전송.
				sendReloadMessage(profile.getCompanyId());
			}
			
			// [Redis] 채팅방의 인원수 -1.
			this.chatRoomRepository.minusUserCount(roomId);
			
			// [Redis] 채팅에 참여중인 인원 확인.
			long joinCount = this.chatRoomRepository.getUserCount(roomId);

			this.logger.debug("[ROOM_" + roomId + "] Join count : " + joinCount);
			if (joinCount <= 0) {
				// [Redis] 채팅방에 조인된 사람이 없다면 데이터 삭제 - Redis에 데이터 누적 방지.
				this.chatRoomRepository.deleteChatRoom(roomId);
			}
		}
		
		// [Redis] 퇴장한 클라이언트의 roomId 매핑정보 삭제.
		this.chatRoomRepository.removeUserJoinInfo(sessionId);
		
		// [Redis] 커네션한 유저 토큰 삭제.
		this.chatRoomRepository.dropUserToken(sessionId);
	}
	
	
	// 상담사 채팅방 나가기.
	public void leave(SocketData socketData) {
		// STOMP에서 leave는 사용하지 않고 해당 채팅방과의 Socket 연결을 해제(disconnected)한다.
	}
	
	
	// 로그인.
	public void login(SocketData socketData) {
		// 토큰에서 사용자정보 추출.
		Member profile = this.loginService.getMember(socketData.getToken());
		
		if(profile.getIsCustomer() == 1) {
			loginCustomer(socketData);
		}else {
			loginMember(socketData);
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
	
}



