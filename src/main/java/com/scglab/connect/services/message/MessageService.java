package com.scglab.connect.services.message;

import edu.emory.mathcs.backport.java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.ErrorService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.services.review.Review;
import com.scglab.connect.services.room.Room;
import com.scglab.connect.services.room.RoomDao;
import com.scglab.connect.utils.DataUtils;

@Service
public class MessageService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageDao messageDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ErrorService errorService;
	@Autowired
	private RoomDao roomDao;

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
	public List<Message> getMessages(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {

		String errorParams = "";
		if (!this.commonService.valid(params, "queryId"))
			errorParams = this.commonService.appendText(errorParams, "조회구분-queryId");

		if (!this.commonService.valid(params, "roomId"))
			errorParams = this.commonService.appendText(errorParams, "방id-roomId");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		params.put("id", DataUtils.getString(params, "roomId", ""));
		Room room = this.roomDao.getDetail(params);
		if (room != null && room.getState() == 8) {
			params.put("state", room.getState());
		}

		List<Message> messages = null;

		// 조회유형 구분자.
		String queryId = DataUtils.getString(params, "queryId", "");
		this.logger.debug("queryId : " + queryId);

		params.put("startId", DataUtils.getString(params, "startId", ""));
		params.put("endId", DataUtils.getString(params, "endId", ""));
		params.put("intervalDay", DataUtils.getInt(params, "intervalDay", Constant.DEFAULT_MESSAGE_INTERVAL_DAY));
		params.put("pageSize", DataUtils.getInt(params, "pageSize", Constant.DEFAULT_MESSAGE_MORE_PAGE_SIZE));
		params.put("messageAdminType", DataUtils.getString(params, "messageAdminType", ""));

		switch (queryId) {
		case "findByRoomIdAll": // 메세지 전체 조회.
			messages = this.messageDao.findByRoomIdAll(params);
			Collections.reverse(messages);
			break;

		case "findByRoomIdToSpeaker": // 메세지 조회(조인한 사용자)
			messages = this.messageDao.findByRoomIdToSpeaker(params);
			break;

		case "findByRoomIdToAdmin": // 메세지 조회(관리자, 조회자)
			messages = this.messageDao.findByRoomIdToAdmin(params);
			break;

		case "findRangeById": // 메세지 조회(id 범위)

			errorParams = "";
			if (!this.commonService.valid(params, "startId"))
				errorParams = this.commonService.appendText(errorParams, "시작 메시지id-startId");

			if (!this.commonService.valid(params, "endId"))
				errorParams = this.commonService.appendText(errorParams, "종료 메시지id-endId");

			// 파라미터 유효성 검증.
			if (!errorParams.equals("")) {
				// 필수파라미터 누락에 따른 오류 유발처리.
				this.errorService.throwParameterErrorWithNames(errorParams);
			}

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
	public Map<String, Object> delete(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.messageDao.delete(params);

		data.put("success", result > 0 ? true : false);
		return data;
	}
	
	
	public ResponseEntity<Map<String, Object>> noreadCountForCustomer(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		String errorParams = "";
		if (!this.commonService.valid(params, "gasappMemberNumber"))
			errorParams = this.commonService.appendText(errorParams, "고객번호-gasappMemberNumber");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}
		
		int count = this.messageDao.noReadMessageForCustomer(params);
		this.logger.debug("noread count : " + count);
		
		data.put("count", count);
		
		return new ResponseEntity<>(data, null, HttpStatus.OK);
	}
}