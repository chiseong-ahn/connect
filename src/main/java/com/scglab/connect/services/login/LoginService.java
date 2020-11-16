package com.scglab.connect.services.login;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scglab.connect.constant.Constant;
import com.scglab.connect.properties.JwtProperties;
import com.scglab.connect.services.adminmenu.emp.EmpDao;
import com.scglab.connect.services.common.auth.User;
import com.scglab.connect.services.common.service.JwtService;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.company.external.ICompany;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.CompanyUtils;
import com.scglab.connect.utils.DataUtils;


@Service
public class LoginService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageHandler messageService;
	
	@Autowired
	private LoginDao loginDao;
	
	@Autowired
	private EmpDao empDao;
	
	@Autowired
	private JwtProperties jwtProperty;
	
	@Autowired
	private JwtService jwtService;
	
	public Map<String, Object> login(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		String companyId = DataUtils.getString(params, "companyId", "");
		String loginName = DataUtils.getString(params, "loginName", "");
		String password = DataUtils.getString(params, "password", "");
		
		this.logger.debug("companyId : " + companyId);
		this.logger.debug("loginName : " + loginName);
		this.logger.debug("password : " + password);
		
		if(companyId.equals("") || loginName.equals("") || password.equals("")) {	// 로그인 정보가 부족할 경우.
			data.put("token", null);
			data.put("member", null);
			data.put("reason", this.messageService.getMessage("auth.login.fail.reason1"));
			return data;
		}
			
		// 각 회사별 기간망 클래스 가져오기.
		ICompany company = CompanyUtils.getCompany(companyId);

		// 기간계 로그인.
		Member member = company.login(loginName, password);
		if(member == null) {
			// 기간계에 로그인 정보가 맞지않을경우.
			data.put("token", null);
			data.put("member", null);
			data.put("reason", this.messageService.getMessage("auth.login.fail.reason2"));
			return data;
		}
		
		// 상담톡에 계정정보가 존재하는지 체크.
		int count = this.loginDao.findCount(params);
		if(count == 0) {
			// 상담톡에 계정정보가 존재하지 않는다면 등록.
			params.put("authLevel", 9);
			this.loginDao.saveProfile(params);
		}
		
		// 계정정보 조회
		member = this.loginDao.findProfile(params);
		member.setCompanyName(company.getCompanyName());
		member.setIsAdmin(1);
		member.setLoginName(member.getName());
		this.logger.debug("member : " + member.toString());
			
		ObjectMapper objectMapper = new ObjectMapper();
		Map memberMap = objectMapper.convertValue(member, Map.class);
		
		String token = this.jwtService.generateToken(memberMap, new Date(new Date().getTime() + Long.parseLong(this.jwtProperty.getValidTimeAdmin())));
		this.logger.debug("token : " + token);
		data.put("token", token);
		data.put("profile", member);
		
		return data;
	}
	
	public Map<String, Object> profile(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		Member member = getMember(request);
		if(member != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			Map memberMap = objectMapper.convertValue(member, Map.class);
			String token = this.jwtService.generateToken(memberMap, new Date(new Date().getTime() + Long.parseLong(this.jwtProperty.getValidTimeAdmin())));
			data.put("member", member);
			data.put("token", token);
		}
		
		return data;
	}
	
	public Member getMember(String token) {
		Map<String, Object> claims = this.jwtService.getJwtData(token);
		Member member = new Member();
		return (Member) DataUtils.convertMapToObject(claims, member);
	}
	
	public void convertMapTomember(Map<String, Object> data) {
		Member member = new Member();
		member = (Member) DataUtils.convertMapToObject(data, member);
		
		this.logger.debug("convertMapTomember" + member.toString());
		
	}
	
	public void setMember(Member member, HttpServletRequest request) {
		request.setAttribute(Constant.AUTH_MEMBER, member);
	}
	
	public Member getMember(HttpServletRequest request) {
		if(request.getAttribute(Constant.AUTH_MEMBER) == null) {
			return null;
		}
		return (Member) request.getAttribute(Constant.AUTH_MEMBER);
	}
}

