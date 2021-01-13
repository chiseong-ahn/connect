package com.scglab.connect.services.member;

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
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.utils.DataUtils;

@Service
public class MemberService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private LoginService loginService;
	@Autowired private MemberDao memberDao;
	@Autowired private CommonService commonService;
	@Autowired private ErrorService errorService;
	
	/**
	 * 
	 * @Method Name : members
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 회원 조회.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> members(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		
		//페이지 번호.
		int page = Integer.parseInt(DataUtils.getObjectValue(params, "page", "1"));
		page = page < 1 ? 1 : page;
		
		// 페이지당 노출할 게시물 수.
		int pageSize = Integer.parseInt(DataUtils.getObjectValue(params, "pageSize", "10"));
		pageSize = pageSize < 1 ? 10 : pageSize;
		
		// 조회 시작 번호.
		int startNum = (page - 1) * pageSize;
		
		params.put("startNum", startNum);
		params.put("pageSize", pageSize);
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		int totalCount = this.memberDao.findAllCount(params);
		
		List<Member> memberList = new ArrayList<Member>();
		if(totalCount > 0) {
			memberList = this.memberDao.findAll(params);
		}
		data.put("totalCount", totalCount);
		data.put("list", memberList);
		
		return data;
	}
	
	/**
	 * 
	 * @Method Name : member
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 회원 상세 조회.
	 * @param params
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> member(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		Member member = this.memberDao.findMemberWithId(params);
		data.put("member", member);
		return data;
	}
	
	
	/**
	 * 
	 * @Method Name : regist
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 회원 등록.
	 * @param params
	 * @param id
	 * @param request
	 * @param response 등록된 회원정보.
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> regist(Map<String, Object> params, int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		String errorParams = "";
		if(!this.commonService.valid(params, "authLevel"))
			errorParams = this.commonService.appendText(errorParams, "권한레벨-authLevel");
		if(!this.commonService.valid(params, "name"))
			errorParams = this.commonService.appendText(errorParams, "이름-name");
			
		// 파라미터 유효성 검증.
		if(!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		int result = this.memberDao.insertMember(params);
		if(result > 0) {
			Member newMember = this.memberDao.findOne("member.regist", params);
			data.put("member", newMember);
		}
		return data;
	}
	
	/**
	 * 
	 * @Method Name : update
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 회원 수정.
	 * @param params
	 * @param id
	 * @param request
	 * @param response 수정된 회원정보.
	 * @return
	 * @throws Exception
	 */
	public Member update(Map<String, Object> params, int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		String errorParams = "";
		if(!this.commonService.valid(params, "authLevel"))
			errorParams = this.commonService.appendText(errorParams, "권한레벨-authLevel");
		if(!this.commonService.valid(params, "state"))
			errorParams = this.commonService.appendText(errorParams, "상담상태-state");
			
		// 파라미터 유효성 검증.
		if(!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}
		
		Member memberInfo = null;
		int result = this.memberDao.update("member.update", params);
		if(result > 0) {
			memberInfo = this.memberDao.findMemberWithId(params);
		}
		return memberInfo;
	}

	/**
	 * 
	 * @Method Name : state
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 회원 상태 변경.
	 * @param params
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> state(Map<String, Object> params, int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		String errorParams = "";
		if(!this.commonService.valid(params, "state"))
			errorParams = this.commonService.appendText(errorParams, "상담상태-state");
			
		// 파라미터 유효성 검증.
		if(!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.memberDao.updateMemberState(params);
		data.put("success", result > 0 ? true : false);
		return data;
	}
}