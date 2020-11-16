package com.scglab.connect.services.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.services.socket.SocketService.EventName;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/talk")
@Tag(name = "상담 채팅", description = "상담 채팅")
public class SocketController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SocketService socketService;
	
	
	
    @MessageMapping("/talk/message")
    public void talkMessage(SocketData socketData) {	
    	EventName eventName = socketData.getEventName();
    	
    	
    }
    
    public void join(SocketData recvData) {
    	
    }
}


