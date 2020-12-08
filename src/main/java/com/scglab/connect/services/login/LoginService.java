package com.scglab.connect.services.login;

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
import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.JwtService;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.company.external.ICompany;
import com.scglab.connect.services.customer.Customer;
import com.scglab.connect.services.customer.CustomerDao;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;


@Service
public class LoginService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private MessageHandler messageService;
	@Autowired private LoginDao loginDao;
	@Autowired private JwtService jwtService;
	@Autowired private CommonService commonService;
	@Autowired private CustomerDao customerDao;
	
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
		
		this.logger.debug("companyId : " + companyId);
			
		// 각 회사별 기간망 클래스 가져오기.
		ICompany company = this.commonService.getCompany(companyId);

		// 기간계 로그인
		if(!company.login(loginName, password)) {
			return null;
		}
		
		
		// 상담톡에 계정정보가 존재하는지 체크.
		int count = this.loginDao.findCount(params);
		if(count == 0) {
			// 상담톡에 계정정보가 존재하지 않는다면 등록.
			params.put("authLevel", 9);
			this.loginDao.saveProfile(params);
		}
		
		// 계정정보 조회
		Member member = this.loginDao.findProfile(params);
		member.setCompanyName(company.getCompanyName());
		member.setIsAdmin(1);
		member.setLoginName(member.getLoginName());
		this.logger.debug("member : " + member.toString());
			
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> memberMap = objectMapper.convertValue(member, Map.class);
		
		String token = this.jwtService.generateToken(memberMap);
		this.logger.debug("token : " + token);
		data.put("token", token);
		data.put("profile", member);
		
		return data;
	}
	
	public Map<String, Object> loginCustomer(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		this.customerDao.regist(params);
		Customer customer = this.customerDao.findByGassappMemberNumber(params);
		this.logger.debug("customer : " + customer);
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> customerMap = objectMapper.convertValue(customer, Map.class);
		String token = this.jwtService.generateToken(customerMap);
		data.put("token", token);
		data.put("customer", customer);
		
		return data;
	}
	
	
	public Member profile(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getMember(request);
	}
	
	public Member getMember(String token) {
		Map<String, Object> claims = this.jwtService.getJwtData(token);
		Member member = new Member();
		try {
			member = (Member) DataUtils.convertMapToObject(claims, member);
		}catch(Exception e) {
			this.logger.error(token);
			//e.printStackTrace();
		}
		return member;
	}
	
	public Customer getCustomer(String token) {
		Map<String, Object> claims = this.jwtService.getJwtData(token);
		Customer customer = new Customer();
		try {
			customer = (Customer) DataUtils.convertMapToObject(claims, customer);
		}catch(Exception e) {
			this.logger.error(token);
			//e.printStackTrace();
		}
		return customer;
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

