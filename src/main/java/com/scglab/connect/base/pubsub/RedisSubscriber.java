package com.scglab.connect.base.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.socket.SocketData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    // Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
//    public void sendMessage(String publishMessage) {
//        try {
//            TalkMessage talkMessage = objectMapper.readValue(publishMessage, TalkMessage.class);
//            messagingTemplate.convertAndSend("/sub/chat/room/" + talkMessage.getRoomId(), talkMessage);
//        } catch (Exception e) {
//            log.error("Exception {}", e);
//        }
//    }
    
    public void sendMessage(String publishMessage) {
    	try {
    		//Map<String, Object> data = this.objectMapper.readValue(publishMessage, Map.class);
    		SocketData payload = this.objectMapper.readValue(publishMessage, SocketData.class);
    		String roomId = payload.getRoomId();
    		//data.remove("roomId");
    		
    		this.messagingTemplate.convertAndSend(Constant.SOCKET_ROOM_PREFIX + roomId, payload);
    	}catch(Exception e) {
    		this.logger.error(e.getMessage());
    	}
    }
}
