package com.scglab.connect.services.room;

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
import com.scglab.connect.utils.DataUtils;

@Service
public class RoomService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private MessageHandler messageService;
	@Autowired private LoginService loginService;
	
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
				params.put("memberId", member.getLoginName());
				
			}else if(member.getAuthLevel() < 4){
				params.put("memberId", null);
				
			}
			
			if(searchType.equals("message")) {					// 메세지 조건으로 검색.
				params.put("message", member.getLoginName());
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
			
		case "findCloseState" :			// 종료된 방 목록 조회.
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
		
		// 대상 방 정보조회.
		Room room = this.roomDao.getDetail(params);
		if(room.getMemberId() > 0) {
			// "이미 상담사가 배정되어 있는 경우" Exception 발생시킴.
			throw new RuntimeException(this.messageService.getMessage("error.room.assigned"));
		}
		
		// 매칭처리.
		int result = this.roomDao.matchRoom(params);
		if(result > 0) {
			// 매칭된 방 정보조회.
			room = this.roomDao.getDetail(params);
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
		Room room = null;
		int result = this.roomDao.transferRoom(params);
		if(result > 0) {
			room = this.roomDao.getDetail(params);
		}
		return room;
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
			//
		}
		
		return list;
	}
	
	
}