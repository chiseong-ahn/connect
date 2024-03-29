package com.scglab.connect.services.room;

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
import com.scglab.connect.services.automessage.AutoMessage;
import com.scglab.connect.services.automessage.AutoMessageDao;
import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.ErrorService;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.login.Profile;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.services.message.Message;
import com.scglab.connect.services.message.MessageDao;
import com.scglab.connect.services.socket.SocketMessageHandler;
import com.scglab.connect.services.socket.SocketService.EventName;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.JsonUtils;

@Service
public class RoomService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageHandler messageHandler;
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private LoginService loginService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ErrorService errorService;
	@Autowired
	private SocketMessageHandler socketMessageHandler;
	@Autowired
	private RoomDao roomDao;
	@Autowired
	private AutoMessageDao autoMessageDao;

	/**
	 * 
	 * @Method Name : getRoomInfo
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 방 검색.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public Object getRoomInfo(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
		Member member = this.loginService.getMember(request);
		params.put("companyId", DataUtils.getString(params, "companyId", member.getCompanyId()));

		String errorParams = "";
		if (!this.commonService.valid(params, "queryId"))
			errorParams = this.commonService.appendText(errorParams, "조회항목구분-queryId");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		Object object = null;

		String queryId = DataUtils.getString(params, "queryId", "");

		if (queryId.equals("findSearchCloseState")) {
			errorParams = "";
			if (!this.commonService.valid(params, "startDate"))
				errorParams = this.commonService.appendText(errorParams, "검색시작일-startDate");

			if (!this.commonService.valid(params, "endDate"))
				errorParams = this.commonService.appendText(errorParams, "검색시작일-endDate");

			// 파라미터 유효성 검증.
			if (!errorParams.equals("")) {
				// 필수파라미터 누락에 따른 오류 유발처리.
				this.errorService.throwParameterErrorWithNames(errorParams);
			}
		}

		String searchType = DataUtils.getString(params, "searchType", "");
		String searchValue = DataUtils.getString(params, "searchValue", "");

		if (queryId.indexOf("findReadyState") > -1) { // 대기중인 방 목록 조회.
			String sort = DataUtils.getString(params, "sort", "");
			if (sort.equals("joinDate")) {
				params.put("sort", "last_message_create_date desc");
			} else {
				params.put("sort", "wait_start_date asc");
			}
		}

		if (queryId.indexOf("State") > -1) { // 그 외 조회
			String checkSelf = DataUtils.getString(params, "checkSelf", Constant.NO);
			if (checkSelf.equals(Constant.YES)) {
				params.put("memberId", member.getId());

			} else if (member.getAuthLevel() < 4) {
				params.put("memberId", null);

			} else {
				params.put("memberId", member.getId());
			}

			if (searchType.equals("message")) { // 메세지 조건으로 검색.
				params.put("message", searchValue);
				params.put("customerName", "");
				params.put("memberName", "");

			} else if (searchType.equals("customerName")) { // 고객명 조건으로 검색.
				params.put("message", "");
				params.put("customerName", searchValue);
				params.put("memberName", "");

			} else if (searchType.equals("memberName")) { // 멤버명 조건으로 검색.
				params.put("message", "");
				params.put("customerName", "");
				params.put("memberName", searchValue);

			} else { // 검색조건 없을경우.
				params.put("message", "");
				params.put("customerName", "");
				params.put("memberName", "");
			}
		}
		this.logger.debug("params : " + params.toString());

		switch (queryId) {
		case "getCurrentTimeStats": // 현재 시간 방 통계정보.
			object = this.roomDao.getCurrentTimeStats(params);
			break;
		case "getRoomStateCountByLoginMember": // 방 상태별 count 정보.
			object = this.roomDao.getRoomStateCountByLoginMember(params);
			break;
		case "findReadyState": // 대기중인 방 목록 조회.
			object = this.roomDao.findReadyState(params);
			break;

		case "findIngState": // 진행중인 방 목록 조회.
			object = this.roomDao.findIngState(params);
			break;

		case "findSearchCloseState": // 종료된 방 목록 조회.
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
	public Room room(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
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
	public Room matchRoom(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
		Member member = this.loginService.getMember(request);
		params.put("memberId", member.getId());

		// 대상 방 정보조회.
		Room room = this.roomDao.getDetail(params);
		if (room != null) {
			if (room.getMemberId() > 0) {
				// "이미 상담사가 배정되어 있는 경우" Exception 발생시킴.
				throw new RuntimeException(this.messageHandler.getMessage("error.room.assigned"));
			}

			// 매칭처리.
			this.roomDao.matchRoom(params);

			// 매칭된 방 정보조회.
			room = this.roomDao.getDetail(params);
			Map<String, Object> sendData = new HashMap<String, Object>();
			sendData.put("room", room);

			Profile profile = null;
			if (member != null) {
				profile = new Profile();
				profile.setCompanyId(member.getCompanyId());
				profile.setIsAdmin(0);
				profile.setIsMember(1);
				profile.setIsCustomer(0);
				profile.setName(member.getName());
				profile.setSpeakerId(member.getSpeakerId());
				profile.setRoomId(room.getId() + "");
			}

//			Map<String, Object> sendData = new HashMap<String, Object>();
//			sendData.put("room", room);
			sendData.put("profile", profile);
			sendData.put("isCustomer", false);

			this.socketMessageHandler.sendMessageToSelf(EventName.ASSIGNED, profile, sendData);
			this.socketMessageHandler.sendMessageToLobby(EventName.RELOAD_READY, profile, sendData);
			
			
			// [DB] 상담시작 및 대기시간 메세지 생성.
			String timeMessage = DataUtils.getString(params, "timeMessage", "");
			if(!timeMessage.equals("")) {
				params = new HashMap<String, Object>();
				params.put("companyId", member.getCompanyId());
				params.put("roomId", room.getId());
				params.put("speakerId", null);
				params.put("messageType", 0); // 메세지 유형 (0-일반, 1-이미지, 2-동영상, 3-첨부, 4-링크, 5-이모티콘)
				params.put("isSystemMessage", 1);
				params.put("message", timeMessage);
				params.put("messageAdminType", 0); // 시스템 메세지의 다른 유형. (0-일반 메세지, 1-시스템 메세지)
				params.put("isEmployee", 0);
				params.put("messageDetail", "");
				params.put("templateId", null);
				
				Message message = this.messageDao.create(params);

				// [Socket] 상담시작 및 대기시간 메세지 생성.
				sendData = new HashMap<String, Object>();
				sendData.put("message", message);
				this.socketMessageHandler.sendMessageToBroadcast(EventName.MESSAGE, profile, sendData);
				this.logger.debug("상담시작 및 대기시간 메세지 전송처리 완료");
			}
			
			
			params = new HashMap<String, Object>();
			params.put("type", 0);
			params.put("companyId", member.getCompanyId());
			AutoMessage autoMessage = this.autoMessageDao.getAutoMessageRandom(params);
			String startMessage = autoMessage.getMessage();
			
			// [DB] 시작 메세지 생성.
			params = new HashMap<String, Object>();
			params.put("companyId", member.getCompanyId());
			params.put("roomId", room.getId());
			params.put("speakerId", null);
			params.put("messageType", 0); // 메세지 유형 (0-일반, 1-이미지, 2-동영상, 3-첨부, 4-링크, 5-이모티콘)
			params.put("isSystemMessage", 1);
			params.put("message", startMessage);
			params.put("messageAdminType", 0); // 시스템 메세지의 다른 유형. (0-일반 메세지, 1-시스템 메세지)
			params.put("isEmployee", 0);
			params.put("messageDetail", "");
			params.put("templateId", null);
			Message newMessage = this.messageDao.create(params);

			// [Socket] 시작메시지 전송.
			sendData = new HashMap<String, Object>();
			sendData.put("message", newMessage);
			this.socketMessageHandler.sendMessageToBroadcast(EventName.MESSAGE, profile, sendData);
			this.logger.debug("시작메시지 전송처리 완료");
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
	public Room closeRoom(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());

		Room room = null;

		Profile profile = null;
		if (member != null) {
			profile = new Profile();
			profile.setCompanyId(member.getCompanyId());
			profile.setIsAdmin(0);
			profile.setIsMember(1);
			profile.setIsCustomer(0);
			profile.setName(member.getName());
			profile.setSpeakerId(member.getSpeakerId());
			profile.setRoomId(params.get("roomId") + "");
		}

		params = new HashMap<String, Object>();
		params.put("companyId", profile.getCompanyId());
		params.put("roomId", profile.getRoomId());
		params.put("id", profile.getRoomId());
		params.put("speakerId", null);
		params.put("messageType", 0); // 메세지 유형 (0-일반, 1-이미지, 2-동영상, 3-첨부, 4-링크, 5-이모티콘)
		params.put("isSystemMessage", 1);
		params.put("message", this.messageHandler.getMessage("socket.end"));
		params.put("messageAdminType", 0);
		params.put("isEmployee", 1);
		params.put("messageDetail", "");
		params.put("templateId", null);
		Message newMessage = this.messageDao.create(params);

		Map<String, Object> sendData = new HashMap<String, Object>();
		sendData.put("message", newMessage);
		this.socketMessageHandler.sendMessageToBroadcast(EventName.MESSAGE, profile, sendData);

		int result = this.roomDao.closeRoom(params);
		if (result > 0) {
			room = this.roomDao.getDetail(params);

			sendData = new HashMap<String, Object>();
			sendData.put("room", room);
			sendData.put("success", true);
			sendData.put("isCustomer", false);
			this.socketMessageHandler.sendMessageToBroadcast(EventName.END, profile, sendData);
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
	public Room transferRoom(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());

		String errorParams = "";
		if (!this.commonService.valid(params, "transferType"))
			errorParams = this.commonService.appendText(errorParams, "이관유형-transferType");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		String transferType = DataUtils.getString(params, "transferType", "");

		if (transferType.equals("toMember")) {
			errorParams = "";
			if (!params.containsKey("memberId"))
				errorParams = this.commonService.appendText(errorParams, "이관할 멤버id-memberId");

			// 파라미터 유효성 검증.
			if (!errorParams.equals("")) {
				// 필수파라미터 누락에 따른 오류 유발처리.
				this.errorService.throwParameterErrorWithNames(errorParams);
			}
		}

		int memberId = DataUtils.getInt(params, "memberId", 0);
		if (transferType.equals("")) {
			this.logger.error("transferType : " + transferType);
			throw new RuntimeException("error.params.type0");

		} else if (transferType.equals("toMember")) {
			if (memberId <= 0) {
				this.logger.error("memberId : " + memberId);
				throw new RuntimeException("error.params.type5");
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
	public List<Map<String, Object>> findSearchJoinHistory(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, Object>> list = this.roomDao.findSearchJoinHistory(params);
		if (list != null) {
			if (list.size() > 0) {
				for (Map<String, Object> obj : list) {
					if (obj != null) {
						String joinHistoryJson = DataUtils.getString(obj, "joinHistoryJson", "");
						if (!joinHistoryJson.equals("")) {
							List<Map<String, Object>> histList = JsonUtils.getListMapFromString(joinHistoryJson);
							obj.put("joinHistoryJson", histList);
						}
					}
				}
			}
		}

		return list;
	}

}