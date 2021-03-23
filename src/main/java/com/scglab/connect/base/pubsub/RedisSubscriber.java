package com.scglab.connect.base.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.socket.SocketData;
import com.scglab.connect.services.socket.SocketService;
import com.scglab.connect.services.socket.SocketService.Target;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final ObjectMapper objectMapper;
	private final SimpMessageSendingOperations messagingTemplate;
	
	@Autowired
	private SocketService socketService;

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
		
		String sessionId = "";
		
		try {
			SocketData payload = this.objectMapper.readValue(publishMessage, SocketData.class);
			String roomId = payload.getRoomId();
			
			String destination = "";
		
			// 발송 대상(전체, 멤버, 고객, 본인)
			Target target = payload.getTarget();
			
			if(target == Target.BROADCAST || target == Target.LOBBY) {
				
				if(target == Target.BROADCAST) {
					// 룸 전체에 메시지 보내기.
					
					// :: /sub/socket/room/[룸번호]
					destination = Constant.SOCKET_SIMPLE_BROKER + Constant.SOCKET_ROOM_PREFIX + "/" + roomId;
					
				}else if(target == Target.LOBBY) {
					// 대기실에 메시지 보내기.
					
					// :: /sub/socket/room/LOBBY[회사번호]
					destination = Constant.SOCKET_SIMPLE_BROKER + Constant.SOCKET_ROOM_PREFIX + "/" + Constant.SOCKET_LOBBY_ROOM + payload.getCompanyId();
				}
				
				this.logger.debug("broadcast[" + payload.getEventName() + "] : " + destination);
				
				// 전체에게 메시지 발송.
				this.messagingTemplate.convertAndSend(destination, payload);
				
			}else {
				
				
				if(target == Target.CUSTOMER) {
					// 고객의 sessionId 조회.
					sessionId = this.socketService.getCustomerSessionId(roomId);
					
				}else if(target == Target.MEMBER) {
					// 멤버의 sessionId 조회.
					sessionId = this.socketService.getMemberSessionId(roomId);
					
				}else if(target == Target.SELF) {
					// 본인의 sessionId 조회.
					sessionId = payload.getSessionId();
					
				}
				
				// :: /socket/message
				destination = Constant.SOCKET_USER_SUBSCRIBE_URI;
				this.logger.debug("send to " + sessionId + "[" + payload.getEventName() + "] : " + destination);
				
				// SessionId 기반으로 메시지 발송.
				this.messagingTemplate.convertAndSendToUser(sessionId, destination, payload, createHeaders(sessionId));
			}
			

		} catch (Exception e) {
			this.logger.warn("sessionId : " + sessionId);
			e.printStackTrace();
		}
	}
	
	private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
