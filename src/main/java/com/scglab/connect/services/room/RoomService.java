package com.scglab.connect.services.room;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.services.message.Message;
import com.scglab.connect.services.message.MessageDao;
import com.scglab.connect.services.socket.SocketService;
import com.scglab.connect.services.socket.SocketService.EventName;
import com.scglab.connect.services.socket.SocketService.Target;
import com.scglab.connect.utils.DataUtils;

@Service
public class RoomService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private MessageHandler messageHandler;
	@Autowired private MessageDao messageDao;
	@Autowired private LoginService loginService;
	@Autowired private SocketService socketService;
	
	@Autowired
	private RoomDao roomDao;
	
	public Object getRoomInfo(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		
		Object object = null;
		
		String queryId = DataUtils.getString(params, "queryId", "");
		String searchType = DataUtils.getString(params, "searchType", "");
		String searchValue = DataUtils.getString(params, "searchValue", "");
		
		if(queryId.indexOf("findReadyState") > -1) {	// 대기중인 방 목록 조회.
			String sort = DataUtils.getString(params, "sort", "");
			if(sort.equals("joinDate")){
				params.put("sort", "last_message_create_date desc");
			}else {
				params.put("sort", "wait_start_date asc");
			}
		}
			
		if(queryId.indexOf("State") > -1) {		// 그 외 조회
			String checkSelf = DataUtils.getString(params, "checkSelf", Constant.NO); 
			if(checkSelf.equals(Constant.YES)) {
				params.put("memberId", member.getId());
				
			}else if(member.getAuthLevel() < 4){
				params.put("memberId", null);
				
			}
			
			if(searchType.equals("message")) {					// 메세지 조건으로 검색.
				params.put("message", searchValue);
				params.put("customerName", "");
				params.put("memberName", "");
				
			}else if(searchType.equals("customerName")) {		// 고객명 조건으로 검색.
				params.put("message", "");
				params.put("customerName", searchValue);
				params.put("memberName", "");
				
			}else if(searchType.equals("memberName")) {			// 멤버명 조건으로 검색.
				params.put("message", "");
				params.put("customerName", "");
				params.put("memberName", searchValue);
				
			}else {												// 검색조건 없을경우.
				params.put("message", "");
				params.put("customerName", "");
				params.put("memberName", "");
			}	
		}
		this.logger.debug("params : " + params.toString());
		
		switch(queryId) {
		case "getCurrentTimeStats" :	// 현재 시간 방 통계정보.
			object = this.roomDao.getCurrentTimeStats(params);
			break;
			
		case "findReadyState" :			// 대기중인 방 목록 조회.
			object = this.roomDao.findReadyState(params);
			break;
			
		case "findIngState" :			// 진행중인 방 목록 조회.
			object = this.roomDao.findIngState(params);
			break;
			
		case "findSearchCloseState" :			// 종료된 방 목록 조회.
			object = this.roomDao.findCloseState(params);
			break;
		}
		
		return object;
	}
	
	/**
	 * 
	 * @Method Name : room
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 방 상세정보 조회
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public Room room(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){
		Room room = this.roomDao.getDetail(params);
		return room;
	}
	
	/**
	 * 
	 * @Method Name : matchRoom
	 * @작성일 : 2020. 11. 23.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 방 상담사 매칭시키기
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public Room matchRoom(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){
		Member member = this.loginService.getMember(request);
		params.put("memberId", member.getId());
		
		// 대상 방 정보조회.
		Room room = this.roomDao.getDetail(params);
		if(room != null) {
			if(room.getMemberId() > 0) {
				// "이미 상담사가 배정되어 있는 경우" Exception 발생시킴.
				throw new RuntimeException(this.messageHandler.getMessage("error.room.assigned"));
			}
			
			// 매칭처리.
			this.roomDao.matchRoom(params);
			
			// 매칭된 방 정보조회.
			room = this.roomDao.getDetail(params);
			Map<String, Object> sendData = new HashMap<String, Object>();
			sendData.put("room", room);
			
			this.socketService.sendMessage(EventName.ASSIGNED, member.getCompanyId(), room.getId() + "", Target.ALL, sendData);
		}
		return room;
	}
	
	/**
	 * 
	 * @Method Name : closeRoom
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 방 종료.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public Room closeRoom(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());
		
		Room room = null;
		
		int result = this.roomDao.closeRoom(params);
		if(result > 0) {
			room = this.roomDao.getDetail(params);
		}
		return room;
	}
	
	/**
	 * 
	 * @Method Name : transferRoom
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 방 이관.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public Room transferRoom(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());
		
		String transferType = DataUtils.getString(params, "transferType", "");
		String memberId = DataUtils.getString(params, "memberId", "");
		if(transferType.equals("")) {
			this.logger.error("transferType : " + transferType);
			throw new RuntimeException("error.params.type0");
		}else if(transferType.equals("toMember")) {
			if(memberId.equals("")) {
				this.logger.error("memberId : " + memberId);
				throw new RuntimeException("error.params.type0");
			}
		}
		
		this.roomDao.transferRoom(params);
		return this.roomDao.getDetail(params);
	}
	
	/**
	 * 
	 * @Method Name : findSearchJoinHistory
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 이전 상담 검색.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public List<Map<String, Object>> findSearchJoinHistory(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){
		List<Map<String, Object>> list = this.roomDao.findSearchJoinHistory(params);
		
		for(Map<String, Object> obj : list) {
			// 대화 메시지 조회
			BigInteger roomId = (BigInteger) obj.get("room_id");
			BigInteger startMessageId = (BigInteger) obj.get("start_message_id");
			BigInteger endMessageId = (BigInteger) obj.get("end_message_id");
			
			obj.put("roomId", roomId);
			obj.put("startMessageId", startMessageId);
			obj.put("endMessageId", endMessageId);
			List<Message> messages = this.messageDao.findRangeById(obj);
			obj.put("messages", messages);
		}
		
		return list;
	}
	
	
}