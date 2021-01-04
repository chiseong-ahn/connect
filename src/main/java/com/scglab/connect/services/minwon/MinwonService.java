package com.scglab.connect.services.minwon;

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
import com.scglab.connect.services.company.external.ICompany;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;

@Service
public class MinwonService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired private MessageHandler messageService;
	@Autowired private MinwonDao minwonDao;
	@Autowired private LoginService loginService;
	@Autowired private CommonService commonService;
	@Autowired private ErrorService errorService;

	public List<Map<String, Object>> codes(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		// 각 회사별 기간망 클래스 가져오기.
		ICompany company = this.commonService.getCompany(member.getCompanyId());

		List<Map<String, Object>> codes = company.getMinwonsCodes();

		return codes == null ? new ArrayList<>() : codes;
	}

	/**
	 *
	 * @Method Name : regist
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 민원 등록 
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Minwon regist(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		String errorParams = "";
		if(!this.commonService.valid(params, "gasappMemberNumber"))
			errorParams = this.commonService.appendText(errorParams, "가스앱 회원번호-gasappMemberNumber");
		if(!this.commonService.valid(params, "useContractNum"))
			errorParams = this.commonService.appendText(errorParams, "사용계약번호-useContractNum");
		if(!this.commonService.valid(params, "categorySmallId"))
			errorParams = this.commonService.appendText(errorParams, "소분류 카테고리id-categorySmallId");
		if(!this.commonService.valid(params, "minwonCode"))
			errorParams = this.commonService.appendText(errorParams, "민원코드-minwonCode");
		if(!this.commonService.valid(params, "telNumber"))
			errorParams = this.commonService.appendText(errorParams, "휴대폰번호-telNumber");
		if(!this.commonService.valid(params, "chatId"))
			errorParams = this.commonService.appendText(errorParams, "룸에 대한 chatId-chatId");
		if(!this.commonService.valid(params, "roomId"))
			errorParams = this.commonService.appendText(errorParams, "방id-roomId");

		// 파라미터 유효성 검증.
		if(!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		Minwon minwon = null;
		if(this.minwonDao.insertMinwon(params) > 0) {
			minwon = this.minwonDao.findMinwon(params);

			// 각 회사별 기간망 클래스 가져오기.
			ICompany company = this.commonService.getCompany(member.getCompanyId());
			
			/*
			 	customerMobileId: 가스앱 회원 id
				useContractNum: 계약번호
				reqName: 요청자명
				classCode: 민원코드
				transfer: 민원이관여부
				S handphone: 핸드폰번호
				memo: 메모
				employeeId: 민원등록 직원 사번
				chatId: 채팅 id
			 */
			Map<String, String> obj = new HashMap<String, String>();
			obj.put("customerMobileId", DataUtils.getString(params, "gasappMemberNumber", ""));
			obj.put("useContractNum", DataUtils.getString(params, "useContractNum", ""));
			obj.put("reqName", member.getName());
			obj.put("classCode", DataUtils.getString(params, "minwonCode", ""));
			obj.put("transfer", false + "");
			obj.put("handphone", DataUtils.getString(params, "telNumber", ""));
			obj.put("memo", DataUtils.getString(params, "memo", ""));
			obj.put("employeeId", member.getLoginName());
			obj.put("chatId", Integer.toString(DataUtils.getInt(params, "chatId", 0)));
			this.logger.info("Minwon > " + obj.toString());

			company.minwons(obj);
		}
		return minwon;
	}

}