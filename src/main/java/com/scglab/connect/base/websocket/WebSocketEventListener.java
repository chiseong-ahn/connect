package com.scglab.connect.base.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.scglab.connect.base.interceptor.CommonInterceptor;
import com.scglab.connect.services.socket.SocketService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class WebSocketEventListener {
	Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);

	@Autowired private SocketService socketService;
	
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
	public void handleWebSocketConnectListener(SessionConnectedEvent event) throws Exception {
		this.socketService.connect(event);
	}
	
	/**
	 * 
	 * @Method Name : handleWebSocketSubscribeListener
	 * @작성일 : 2020. 11. 25.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 구독 이벤트 리스너.
	 * @param event
	 */
	@EventListener
	public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
		this.socketService.subscribe(event);
	}
	
	@EventListener
	public void handleSessionConnectedEvent(SessionConnectedEvent event) {
		this.logger.info("sessionConnectedEvent : " +event.toString());
	}
	
	/**
	 * 
	 * @Method Name : handleWebSocketUnSubscribeListener
	 * @작성일 : 2020. 11. 25.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 구독해제 이벤트 리스너.
	 * @param event
	 */
	@EventListener
	public void handleWebSocketUnSubscribeListener(SessionUnsubscribeEvent event) {
		this.socketService.unsubscribe(event);
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
		this.socketService.disconnect(event);
	}
}
