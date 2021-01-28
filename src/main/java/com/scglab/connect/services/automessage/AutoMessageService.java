package com.scglab.connect.services.automessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.ErrorService;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;

@Service
public class AutoMessageService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageHandler messageService;
	@Autowired
	private AutoMessageDao autoMessageDao;
	@Autowired
	private LoginService loginService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ErrorService errorService;

	/**
	 * 
	 * @Method Name : findAll
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 자동메세지 목록 조회.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public List<AutoMessage> findAll(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		List<AutoMessage> list = this.autoMessageDao.findAutoMessageAll(params);

		return list == null ? new ArrayList<AutoMessage>() : list;
	}

	/**
	 * 
	 * @Method Name : getDetail
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 자동메세지 상세 조회.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public AutoMessage getDetail(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		AutoMessage autoMessage = this.autoMessageDao.getAutoMessage(params);
		return autoMessage == null ? new AutoMessage() : autoMessage;
	}

	/**
	 * 
	 * @Method Name : getAutoMessageWelcome
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 신규대화 시작시 인사메세지 랜덤 조회
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public AutoMessage getAutoMessageWelcome(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		return this.autoMessageDao.getAutoMessageWelcome(params);
	}

	/**
	 * 
	 * @Method Name : getAutoMessageByMatchWait
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 상담사 배정지연 안내 메세지 랜덤 조회.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public AutoMessage getAutoMessageByMatchWait(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		return this.autoMessageDao.getAutoMessageByMatchWait(params);
	}

	/**
	 * 
	 * @Method Name : getAutoMessageByReplyWait
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 답변 지연 안내 메세지 랜덤 조회.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public AutoMessage getAutoMessageByReplyWait(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		return this.autoMessageDao.getAutoMessageByReplyWait(params);
	}

	/**
	 * 
	 * @Method Name : regist
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 자동 메세지 등록.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public AutoMessage regist(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		String errorParams = "";
		if (!this.commonService.validInteger(params, "type"))
			errorParams = this.commonService.appendText(errorParams, "메시지 유형-type");

		if (!this.commonService.valid(params, "message"))
			errorParams = this.commonService.appendText(errorParams, "메시지 내용-message");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		AutoMessage autoMessage = null;
		int result = this.autoMessageDao.createAutoMessage(params);
		if (result > 0) {
			autoMessage = this.autoMessageDao.getAutoMessage(params);
		}
		return autoMessage;
	}

	/**
	 * 
	 * @Method Name : update
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 자동 메세지 수정.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public AutoMessage update(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		String errorParams = "";
		if (!this.commonService.valid(params, "message"))
			errorParams = this.commonService.appendText(errorParams, "메시지 내용-message");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		AutoMessage autoMessage = null;
		int result = this.autoMessageDao.updateAutoMessage(params);
		if (result > 0) {
			autoMessage = this.autoMessageDao.getAutoMessage(params);
		}
		return autoMessage;
	}

	/**
	 * 
	 * @Method Name : delete
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 자동 메세지 삭제.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String, Object> delete(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.autoMessageDao.deleteAutoMessage(params);
		data.put("success", result > 0 ? true : false);
		return data;
	}

}