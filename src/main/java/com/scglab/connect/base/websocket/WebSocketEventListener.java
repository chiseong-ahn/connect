package com.scglab.connect.base.websocket;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.scglab.connect.base.interceptor.CommonInterceptor;
import com.scglab.connect.services.common.auth.AuthService;
import com.scglab.connect.services.common.auth.User;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.services.talk.ChatRoomRepository;
import com.scglab.connect.services.talk.TalkHandler;

import lombok.RequiredArgsConstructor;

@SuppressWarnings(value = {"unused", "unchecked", "rawtypes"})
@RequiredArgsConstructor
@Component
public class WebSocketEventListener {
	Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);

	private final ChatRoomRepository chatRoomRepository;

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@Autowired
	private TalkHandler talkHandler;

	@Autowired
	private AuthService authService;
	
	@Autowired
	private LoginService loginService;

	/**
	 * 
	 * @Method Name : handleWebSocketConnectListener
	 * @작성일 : 2020. 10. 13.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 소켓 연결에 대한 이벤트 리스너.
	 * @param event
	 */
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		this.logger.info("[Connection] headerAccessor : " + headerAccessor.toString());

		String token = "";
		String sessionId = getSessionId(headerAccessor);

		MessageHeaders headers = headerAccessor.getMessageHeaders();

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

		this.chatRoomRepository.setUserToken(sessionId, token);

	}

	@EventListener
	public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
		this.logger.info("Socket subscribe!!");

		this.logger.debug("event : " + event.toString());

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		this.logger.info("[Subscribe] headerAccessor : " + headerAccessor.toString());

		String roomId = getRoomId(getSocketPath(headerAccessor));
		String sessionId = getSessionId(headerAccessor);

		this.logger.info("roomId : " + roomId);
		this.logger.info("sessionId : " + sessionId);

		// 채팅방에 들어온 클라이언트 sessionId를 roomId와 매핑.
		this.chatRoomRepository.setUserJoinInfo(sessionId, roomId);

		// 채팅방의 인원수 +1
		this.chatRoomRepository.plusUserCount(roomId);
		this.logger.debug("user count : " + this.chatRoomRepository.getUserCount(roomId));

		String token = getToken(sessionId, headerAccessor);
		this.logger.info("token : " + token);

		Member profile = this.loginService.getMember(token);
		this.logger.info("profile : " + profile);

		// if(!roomId.equals(this.talkHandler.getLobbySpace(user.getCid()))) {
		this.talkHandler.join(profile, roomId);
		// }
	}

	/**
	 * 
	 * @Method Name : handleWebSocketDisconnectListener
	 * @작성일 : 2020. 10. 13.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 소켓 연결해제에 대한 이벤트 리스너.
	 * @param event
	 */
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		this.logger.info("Socket Disconnected!");

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = getSessionId(headerAccessor);
		this.logger.info("sessionId : " + sessionId);
		
		if (this.chatRoomRepository.getUserJoinRoomId(sessionId) != null) {
			String roomId = getRoomId(this.chatRoomRepository.getUserJoinRoomId(sessionId));

			String token = this.chatRoomRepository.getUserToken(sessionId);
			Member profile = this.loginService.getMember(token);
			this.logger.debug("profile : " + profile.toString());

			if (profile.getIsAdmin() == 0) {
				this.talkHandler.disconnected(profile, roomId);
			}
			
			// 채팅방의 인원수 -1.
			this.chatRoomRepository.minusUserCount(roomId);

			// 채팅에 참여중인 인원 확인.
			long count = this.chatRoomRepository.getUserCount(roomId);

			this.logger.debug("Chatroom user count - " + count);
			if (count == 0) {
				this.chatRoomRepository.deleteChatRoom(roomId);
			}
		}
		
		// 퇴장한 클라이언트의 roomId 매핑정보 삭제.
		this.chatRoomRepository.removeUserJoinInfo(sessionId);
		
		// 커네션한 유저 토큰 삭제.
		this.chatRoomRepository.dropUserToken(sessionId);
	}

	private String getSocketPath(StompHeaderAccessor headerAccessor) {
		MessageHeaders headers = headerAccessor.getMessageHeaders();
		String path = (String) headers.get("simpDestination");
		String roomId = path.replaceAll("/sub/chat/room/", "");
		return roomId;
	}

	private String getSessionId(StompHeaderAccessor headerAccessor) {
		MessageHeaders headers = headerAccessor.getMessageHeaders();
		String sessionId = (String) headers.get("simpSessionId");
		return sessionId;
	}

	private String getRoomId(String socketPath) {
		String roomId = socketPath.replaceAll("/sub/chat/room/", "");
		return roomId;
	}

	private String getToken(String sessionId, StompHeaderAccessor headerAccessor) {
		MessageHeaders headers = headerAccessor.getMessageHeaders();

		Map<String, Object> nativeHeaders = null;
		if (headers.containsKey("simpConnectMessage")) {
			GenericMessage simpConnectMessage = (GenericMessage) headers.get("simpConnectMessage");
			Map<String, Object> subHeaders = simpConnectMessage.getHeaders();
			// this.logger.debug("simpConnectMessage : " + simpConnectMessage);
			nativeHeaders = (Map<String, Object>) subHeaders.get("nativeHeaders");
		} else {
			nativeHeaders = (Map<String, Object>) headers.get("nativeHeaders");
		}

		String token = "";
//    	if(nativeHeaders.containsKey("token")) {
//    		token = ((List<String>)nativeHeaders.get("token")).get(0);
//    	}
//    	

		try {

			token = ((List<String>) nativeHeaders.get("token")).get(0);
			this.logger.debug("Headers에서 Token 조회");
		} catch (Exception e) {
			token = this.chatRoomRepository.getUserToken(sessionId);
			this.logger.debug("Redis에서 Token 조회");
		}
		return token;
	}
}
