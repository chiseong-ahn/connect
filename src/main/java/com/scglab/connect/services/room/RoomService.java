package com.scglab.connect.services.room;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.utils.DataUtils;

@Service
public class RoomService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageHandler messageService;
	
	@Autowired
	private RoomDao roomDao;
	
	public Object getRoomInfo(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){
		Object object = null;
		
		String queryId = DataUtils.getString(params, "queryId", "");
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
	public Map<String, Object> closeRoom(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.roomDao.closeRoom(params);
		
		data.put("success", result > 0 ? true :  false);
		
		return data;
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
	public Map<String, Object> transferRoom(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.roomDao.transferRoom(params);
		
		data.put("success", result > 0 ? true :  false);
		
		return data;
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
	public Map<String, Object> findSearchJoinHistory(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> data = this.roomDao.findSearchJoinHistory(params);
		return data;
	}
	
	
}