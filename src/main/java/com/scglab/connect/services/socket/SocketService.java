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
import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.company.external.ICompany;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.services.message.MessageDao;
import com.scglab.connect.services.room.Room;
import com.scglab.connect.services.room.RoomDao;
import com.scglab.connect.services.talk.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SocketService {
	
	Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);
	
	private final ChannelTopic channelTopic;
	private final RedisTemplate redisTemplate;
	private final ChatRoomRepository chatRoomRepository;
	
	@Autowired private CommonService commonService;
	@Autowired private LoginService loginService;
	@Autowired private RoomDao roomDao;
	@Autowired private MessageDao messageDao;
	@Autowired private MessageHandler messageHandler;
	
	public enum EventName {
		ERROR,
		LOGIN,
		ROOM_DETAIL,
		JOIN, JOINED,
		MESSAGE, DELETE_MESSAGE,
		LEAVE,
		END
	}
	
	// 에러 전송.
	public void sendError(String roomId, String reason, Exception e) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("eventName", EventName.ERROR);
		data.put("roomId", roomId);
		this.redisTemplate.convertAndSend(channelTopic.getTopic(), data);
	}
	
	// 이벤트 메세지 전송.
	public void sendMessage(EventName eventName, String companyId, String roomId, Map<String, Object> data) {
		data.put("eventName", eventName);
		data.put("roomId", roomId);
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
		
		this.logger.info("sessionId : " + sessionId);
		this.logger.info("token : " + token);

		// Redis에 연결된 세션ID와 Token을 저장.
		this.chatRoomRepository.setUserToken(sessionId, token);
	}
	
	// 구독처리.
	public void subscribe(SessionSubscribeEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		this.logger.info("[Subscribe] headerAccessor : " + headerAccessor.toString());

		MessageHeaders headers = headerAccessor.getMessageHeaders();
		String path = (String) headers.get("simpDestination");
		String roomId = path.replaceAll(Constant.TALK_ROOM_PREFIX, "");
		String sessionId = (String) headers.get("simpSessionId");
		
		this.logger.info("roomId : " + roomId);
		this.logger.info("sessionId : " + sessionId);

		// 채팅방에 들어온 클라이언트 sessionId를 roomId와 매핑.
		this.chatRoomRepository.setUserJoinInfo(sessionId, roomId);

		// 해당 채팅방의 참여인원수 1증가.
		this.chatRoomRepository.plusUserCount(roomId);
		
		// Redis에서 SessionId에 대한 토큰 추출
		String token = this.chatRoomRepository.getUserToken(sessionId);
		this.logger.info("token : " + token);

		// 토큰에서 인증정보 추출.
		Member profile = this.loginService.getMember(token);
		this.logger.info("profile : " + profile);
		SocketData socketData = new SocketData();
		socketData.setEventName(EventName.JOIN);
		socketData.setCompanyId(profile.getCompanyId());
		socketData.setToken(token);
		socketData.setData(profile);
		
		// 조인처리.
		join(socketData);
	}
	
	// 연결해제.
	public void disconnect(SessionDisconnectEvent event) {
		
	}
	
	// 방 상세 조회
	public void roomDetail(SocketData data) {
		
	}
	
	// 방 조인
	public void join(SocketData socketData) {
		String lobbyRoom = getLobbyRoom(socketData.getCompanyId());
		
		// 로비에 조인일 경우 - 조인만 시키고 아무일도 없음.
		if(lobbyRoom.equals(socketData.getRoomId())) {
			this.logger.debug("Lobby join : " + socketData.toString());
			return;
		}
		
		// Socket > 채팅방 참여자들에게 JOINED 메세지 전송.
		Member member = (Member) socketData.getData();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("member", member);
		sendMessage(EventName.JOINED, socketData.getCompanyId(), socketData.getRoomId(), data);
		
		// 채팅방 정보 조회
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", socketData.getRoomId());
		Room room = this.roomDao.getDetail(params);
		this.logger.debug("room : " + room);
		
		// 고객여부 판단.
		if(member.getIsCustomer() > 0) {
			// 고객일 경우.
			
			if(room.getState() == 0 || room.getState() == 2) {
				// 채팅방의 상태가 최초이거나 종료일 경우 시작메세지 준비.
				
				ICompany company = this.commonService.getCompany(socketData.getCompanyId());
				
				// 근무상태 (1-근무 중, 2-근무 외 시간, 3-점심시간.
				int isWorkType = company.isWorking();
				
				if(isWorkType == 1) {
					// 근무중일 경우.(1)
					
					// 로비에 상담가능한 상담사 카운트 조회. 
					Long readyMemberCount = this.chatRoomRepository.getUserCount(getLobbyRoom(socketData.getCompanyId()));
					
					
					
					// 시작메세지 조회
					String startMessage = "";
					
					
					Map<String, Object> startMessageData = new HashMap<String, Object>();
					startMessageData.put("message", startMessage);
					startMessageData.put("message", startMessage);
					sendMessage(EventName.MESSAGE, socketData.getCompanyId(), socketData.getRoomId(), startMessageData);
				}
				
			}
		
			
		}
			
			
		
	}
	
	// 메세지 전송
	public void message(SocketData data) {
		
	}
	
	// 고객의 상담 종료
	public void endByCustomer(SocketData data) {
		
	}
	
	// 챗봇 이력저장
	public void saveHistory(SocketData data) {
		
	}
	
	// 메세지 삭제
	public void deleteMessage(SocketData data) {
		
	}
	
	// 이벤트 전달
	
	// 메시지 읽음
	public void readMessage(SocketData data) {
		
	}
	
	// 상담사 상담 종료
	public void endByEmp(SocketData data) {
		
	}
	
	// 메세지 더보기
	public void moreMessage(SocketData data) {
		
	}
	
	// 고객 로그인
	public void loginCustomer() {
		
	}
	
	// 회원 로그인
	public void loginUser() {
		
	}
	
	// 로비룸 조회
	public String getLobbyRoom(String companyId) {
		return "LOBBY" + companyId;
	}
	
}



