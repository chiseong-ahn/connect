package com.scglab.connect.base.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.scglab.connect.base.interceptor.CommonInterceptor;

@Component
public class WebSocketEventListener {
	Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

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
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        this.logger.info(headerAccessor.toString());
        this.logger.info("Socket Disconnected!");
    }
}
