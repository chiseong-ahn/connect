package com.scglab.connect.services.socket;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.socket.SocketService.EventName;
import com.scglab.connect.services.talk.ChatRoom;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "상담 채팅", description = "상담 채팅")
public class SocketController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SocketService socketService;

	/**
	 * 
	 * @Method Name : receiveMessage
	 * @작성일 : 2020. 11. 17.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 사용자의 요청을 받는다.
	 * @param socketData
	 */
    @MessageMapping(Constant.SOCKET_RECEIVE_URI)
    public void receiveMessage(SocketData socketData) {
    	EventName eventName = socketData.getEventName();
    	
    	if(eventName.equals(EventName.LOGIN)) {
    		this.socketService.login(socketData);
    		
    	}else if(eventName.equals(EventName.ROOM_DETAIL)) {		// 방 정보 요청.
    		this.socketService.roomDetail(socketData);
    		
    	}else if(eventName.equals(EventName.MESSAGE_LIST)) {	// 이전 대화내용 요청.(더보기)
    		this.socketService.messageList(socketData);
    		
    	}else if(eventName.equals(EventName.MESSAGE)) {			// 메세지 보내기 요청.
    		this.socketService.message(socketData);
    		
    	}else if(eventName.equals(EventName.SAVE_HISTORY)) {	// 이력 저장.
    		this.socketService.saveHistory(socketData);
    		
    	}else if(eventName.equals(EventName.DELETE_MESSAGE)) {	// 메세지 삭제 요청.
    		this.socketService.deleteMessage(socketData);
    		
    	}else if(eventName.equals(EventName.END)) {			// 고객의 상담종료 요청.
    		this.socketService.end(socketData);
    		
    	}
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "socket/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary="상담채팅 현황 조회")
    public List<ChatRoom> getRoomsList(HttpServletRequest request, HttpServletResponse response){
    	return this.socketService.findRooms(request, response);
    }
}


