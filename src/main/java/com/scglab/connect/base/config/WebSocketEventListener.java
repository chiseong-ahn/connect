package com.scglab.connect.base.config;

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
import com.scglab.connect.services.chat.ChatRoom;
import com.scglab.connect.services.chat.ChatRoomRepository;
import com.scglab.connect.services.talk.ChatMessage;
import com.scglab.connect.services.talk.ChatMessage.MessageType;
import com.scglab.connect.services.talk.TalkService;

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
        logger.info("Received a new web socket connection");
//        this.logger.info("Connected : " + event.toString());
        
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
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
    	String roomId = getRoomId(this.chatRoomRepository.getUserEnterRoomId(sessionId));
    	
//    	this.logger.info("sessionId : " + sessionId);
//    	this.logger.info("roomId : " + roomId);
    	
    	this.chatRoomRepository.minusUserCount(roomId);
    	
    }
    
    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
    	this.logger.info("Socket subscribe!!");
    	
    	StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    	String roomId = getRoomId(getSocketPath(headerAccessor));
    	String sessionId = getSessionId(headerAccessor);
    	
    	if(this.chatRoomRepository.findRoomById(roomId) == null) {
    		this.chatRoomRepository.createChatRoom(roomId);
    	}
    	this.chatRoomRepository.removeUserEnterInfo(sessionId);
    	this.chatRoomRepository.setUserEnterInfo(sessionId, roomId);
    	
    	this.chatRoomRepository.plusUserCount(roomId);
    	
    	ChatRoom room = this.chatRoomRepository.findRoomById(roomId);
    	
    	int cid = 1;
    	
    	if(!roomId.equals("base")) {
    		ChatMessage welcomeMessage = new ChatMessage(cid, roomId, MessageType.WELCOME);
    		this.talkService.sendChatMessage(welcomeMessage);
    		
    		ChatMessage payloadMessage = new ChatMessage(cid, "base", MessageType.PAYLOAD);
        	this.talkService.sendChatMessage(payloadMessage);
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
}
