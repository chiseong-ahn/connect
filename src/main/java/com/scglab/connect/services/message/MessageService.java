package com.scglab.connect.services.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.utils.DataUtils;

@Service
public class MessageService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageDao messageDao;
	
	/**
	 * 
	 * @Method Name : getMessages
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 :  
	 * @Method 설명 : 메세지 조회
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public List<Message> getMessages(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){
		List<Message> messages = null;
		
		// 조회유형 구분자.
		String queryId = DataUtils.getString(params, "queryId", "");
		this.logger.debug("queryId : " + queryId);
		
		switch(queryId) {
			case "findByRoomIdAll" :		// 메세지 전체 조회.
				messages = this.messageDao.findByRoomIdAll(params);
				break;
			case "findByRoomIdToSpeaker":	// 메세지 조회(조인한 사용자)
				messages = this.messageDao.findByRoomIdToSpeaker(params);
				break;
			case "findByRoomIdToAdmin":		// 메세지 조회(관리자, 조회자)
				messages = this.messageDao.findByRoomIdToAdmin(params);
				break;
			case "findRangeById":			// 메세지 조회(id 범위)
				messages = this.messageDao.findRangeById(params);
				break;
		}
		
		return messages;
	}
	
	/**
	 * 
	 * @Method Name : delete
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 메세지 삭제
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String, Object> delete(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.messageDao.delete(params);
		
		data.put("success", result > 0 ? true : false);
		return data;
	}
}