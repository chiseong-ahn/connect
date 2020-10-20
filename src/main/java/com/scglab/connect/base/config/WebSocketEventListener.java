package com.scglab.connect.base.config;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.scglab.connect.base.interceptor.CommonInterceptor;
import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.chat.ChatRoomRepository;
import com.scglab.connect.services.common.auth.AuthService;
import com.scglab.connect.services.common.auth.User;
import com.scglab.connect.services.talk.ChatMessage;
import com.scglab.connect.services.talk.ChatMessage.MessageType;
import com.scglab.connect.services.talk.TalkHandler;
import com.scglab.connect.services.talk.TalkService;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.JwtUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class WebSocketEventListener {
	Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);
	
	private final ChatRoomRepository chatRoomRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    
    @Autowired
    private TalkService talkService;
    
    @Autowired
    private TalkHandler talkHandler;
    
    @Autowired
    private AuthService authService;

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
        //String token = headerAccessor.getFirstNativeHeader("token");
        //this.logger.info("CONNECTED {} " + token);
    }

   
    
    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
    	this.logger.info("Socket subscribe!!");
    	
    	StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    	this.logger.info("headerAccessor : " + headerAccessor.toString());
    	
    	String roomId = getRoomId(getSocketPath(headerAccessor));
    	String sessionId = getSessionId(headerAccessor);
    	
    	this.logger.info("roomId : " + roomId);
    	this.logger.info("sessionId : " + sessionId);
    	
    	// 채팅방에 들어온 클라이언트 sessionId를 roomId와 매핑.
    	this.chatRoomRepository.setUserEnterInfo(sessionId, roomId);
    	
    	// 채팅방의 인원수 +1
    	this.chatRoomRepository.plusUserCount(roomId);
    	this.logger.debug("user count : " + this.chatRoomRepository.getUserCount(roomId));
    	
    	String token = getToken(headerAccessor);
    	this.logger.info("token : " + token);
    	
    	JwtUtils jwtUtils = new JwtUtils();
    	User user = this.authService.getUserInfo(jwtUtils.getJwtData(token));
    	this.logger.info("user : " + user);
    	 
    	if(!roomId.equals(Constant.SPACE_LOBBY)) {
    		this.talkHandler.join(user, roomId);
    	}
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
    	
    	if(this.chatRoomRepository.getUserEnterRoomId(sessionId) != null) {
    		String roomId = getRoomId(this.chatRoomRepository.getUserEnterRoomId(sessionId));
    		
    		// 채팅방의 인원수 -1.
        	this.chatRoomRepository.minusUserCount(roomId);
    		
        	// 퇴장한 클라이언트의 roomId 매핑정보 삭제.
        	this.chatRoomRepository.removeUserEnterInfo(sessionId);
    	}
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
    
    private String getToken(StompHeaderAccessor headerAccessor) {
    	MessageHeaders headers = headerAccessor.getMessageHeaders();
    	Map<String, Object> nativeHeaders = (Map<String, Object>)headers.get("nativeHeaders");
    	String token = ((List<String>)nativeHeaders.get("token")).get(0);
    	return token;
    }
}
