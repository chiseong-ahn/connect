package com.scglab.connect.services.socket;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.login.Profile;
import com.scglab.connect.services.socket.SocketService.EventName;

@RestController
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
	public void receiveMessage(SocketData socketData, SimpMessageHeaderAccessor accessor) {

		EventName eventName = socketData.getEventName();
		Profile profile = this.socketService.getMemberWithSettings(accessor, socketData);

		this.logger.debug("socketData : " + socketData);
		this.logger.debug("profile : " + profile);

		socketData.setCompanyId(profile.getCompanyId());
		socketData.setRoomId(profile.getRoomId());

		if (eventName.equals(EventName.ASSIGN)) {
			this.socketService.assign(profile, socketData);
		} else if (eventName.equals(EventName.ROOM_DETAIL)) { // 방 정보 요청.
			this.socketService.roomDetail(profile, socketData);

		} else if (eventName.equals(EventName.MESSAGE_LIST)) { // 이전 대화내용 요청.(더보기)
			this.socketService.messageList(profile, socketData);

		} else if (eventName.equals(EventName.START_MESSAGE)) { // 시작메세지 요청.
			this.socketService.startMessage(profile, socketData);

		} else if (eventName.equals(EventName.MESSAGE)) { // 메세지 보내기 요청.
			this.socketService.message(profile, socketData);

		} else if (eventName.equals(EventName.READ_MESSAGE)) { // 메세지읽음 보내기 요청.
			this.socketService.readMessage(profile, socketData);

		} else if (eventName.equals(EventName.SAVE_HISTORY)) { // 이력 저장.
			this.socketService.saveHistory(profile, socketData);

		} else if (eventName.equals(EventName.DELETE_MESSAGE)) { // 메세지 삭제 요청.
			this.socketService.deleteMessage(profile, socketData);

		} else if (eventName.equals(EventName.REVIEW)) { // 리뷰 요청.
			this.socketService.review(profile, socketData);

		} else if (eventName.equals(EventName.END)) { // 고객의 상담종료 요청.
			this.socketService.end(profile, socketData);

		}
	}

	@RequestMapping(name = "상담채팅 현황 조회", method = RequestMethod.GET, value = "socket/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ChatRoom> getRoomsList(HttpServletRequest request, HttpServletResponse response) {
		return this.socketService.findRooms(request, response);
	}
}
