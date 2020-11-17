package com.scglab.connect.base.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.scglab.connect.constant.Constant;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(Constant.SOCKET_BASE_URI) // 연결할 socket space.
        		.setAllowedOrigins("*")	  // socket CORS.
        		.withSockJS();			  // sockjs 사용.
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub");	// 메세지를 받을 uri prefix
        registry.enableSimpleBroker("/sub");	// 메세지를 보낼 uri prefix
    }
}
