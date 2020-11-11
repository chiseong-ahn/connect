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
import com.scglab.connect.services.common.service.MessageService;
import com.scglab.connect.services.company.external.ICompany;
import com.scglab.connect.utils.CompanyUtils;
import com.scglab.connect.utils.DataUtils;


@Service
public class LoginService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageService messageService;
	
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
			data.put("profile", null);
			data.put("reason", this.messageService.getMessage("auth.login.fail.reason1"));
			return data;
		}
			
		// 각 회사별 기간망 클래스 가져오기.
		ICompany company = CompanyUtils.getCompany(companyId);

		// 기간계 로그인.
		Profile profile = company.login(loginName, password);
		if(profile == null) {
			// 기간계에 로그인 정보가 맞지않을경우.
			data.put("token", null);
			data.put("profile", null);
			data.put("reason", this.messageService.getMessage("auth.login.fail.reason2"));
			return data;
		}
		
		// 상담톡에 계정정보가 존재하는지 체크.
		int count = this.loginDao.findOne("login.profileCount", params);
		if(count == 0) {
			// 상담톡에 계정정보가 존재하지 않는다면 등록.
			
		}
		
		// 계정정보 조회
		profile = this.loginDao.findOne("login.findProfile", params);
		profile.setCompanyName(company.getCompanyName());
		profile.setIsAdmin(1);
		profile.setLoginName(profile.getName());
		this.logger.debug("profile : " + profile.toString());
			
		ObjectMapper objectMapper = new ObjectMapper();
		Map profileMap = objectMapper.convertValue(profile, Map.class);
		
		String token = this.jwtService.generateToken(profileMap, new Date(new Date().getTime() + Long.parseLong(this.jwtProperty.getValidTimeAdmin())));
		this.logger.debug("token : " + token);
		data.put("token", token);
		data.put("profile", profile);
		
		return data;
	}
	
	public Map<String, Object> profile(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		Profile profile = getProfile(request);
		if(profile != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			Map profileMap = objectMapper.convertValue(profile, Map.class);
			String token = this.jwtService.generateToken(profileMap, new Date(new Date().getTime() + Long.parseLong(this.jwtProperty.getValidTimeAdmin())));
			data.put("profile", profile);
			data.put("token", token);
		}
		
		return data;
	}
	
	public Profile getProfile(String token) {
		Map<String, Object> claims = this.jwtService.getJwtData(token);
		Profile profile = new Profile();
		return (Profile) DataUtils.convertMapToObject(claims, profile);
	}
	
	public void convertMapToProfile(Map<String, Object> data) {
		Profile profile = new Profile();
		profile = (Profile) DataUtils.convertMapToObject(data, profile);
		
		this.logger.debug("convertMapToProfile" + profile.toString());
		
	}
	
	public void setProfile(Profile profile, HttpServletRequest request) {
		request.setAttribute(Constant.AUTH_PROFILE, profile);
	}
	
	public Profile getProfile(HttpServletRequest request) {
		if(request.getAttribute(Constant.AUTH_PROFILE) == null) {
			return null;
		}
		return (Profile) request.getAttribute(Constant.AUTH_PROFILE);
	}
}

