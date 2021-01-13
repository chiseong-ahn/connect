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
import com.scglab.connect.services.common.service.ErrorService;
import com.scglab.connect.services.common.service.JwtService;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.company.external.ICompany;
import com.scglab.connect.services.customer.Customer;
import com.scglab.connect.services.customer.CustomerDao;
import com.scglab.connect.services.customer.VCustomer;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.services.member.MemberDao;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.SHA256Utils;


@Service
public class LoginService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired private MessageHandler messageHandler;
	@Autowired private LoginDao loginDao;
	@Autowired private JwtService jwtService;
	@Autowired private CommonService commonService;
	@Autowired private CustomerDao customerDao;
	@Autowired private ErrorService errorService;
	@Autowired private MemberDao memberDao;
	
	public Map<String, Object> loginAdmin(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		String errorParams = "";
		if(!this.commonService.valid(params, "loginName"))
			errorParams = this.commonService.appendText(errorParams, "로그인id-loginName");
		if(!this.commonService.valid(params, "password"))
			errorParams = this.commonService.appendText(errorParams, "비밀번호-password");

		// 파라미터 유효성 검증.
		if(!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}
		
		Member admin = this.loginDao.findAdmin(params);
		if(admin == null) {
			// 아이디에 일치하는 계정이 없을경우.
			this.logger.debug("loginName : " + DataUtils.getString(params, "loginName", ""));
			
			data.put("token", null);
			data.put("admin", null);
			data.put("reason", this.messageHandler.getMessage("error.auth.login.reason6"));
			return data;
		}
		
		SHA256Utils sha256Utils = new SHA256Utils();
		
		String dbPassword = DataUtils.getSafeValue(admin.getPassword());		// DB에 저장되어있는 암호화된 비밀번호.
		String paramPassword = sha256Utils.encrypt(DataUtils.getString(params, "password", ""));	// 입력된 비밀번호
		
		if(!dbPassword.equals(paramPassword)) {
			//  비밀번호가 일치하지 않을경우.
			this.logger.debug("dbPassword : " + dbPassword);
			this.logger.debug("paramPassword : " + paramPassword);
			
			data.put("token", null);
			data.put("admin", null);
			data.put("reason", this.messageHandler.getMessage("error.auth.login.reason7"));
			return data;
		}
		
		admin.setPassword(null);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> memberMap = objectMapper.convertValue(admin, Map.class);

		// 인증토큰 발급.
		String token = this.jwtService.generateToken(memberMap);
		this.logger.debug("token : " + token);
		data.put("token", token);
		data.put("admin", admin);
		
		return data;
	}
	

	/**
	 *
	 * @Method Name : loginMember
	 * @작성일 : 2020. 12. 11.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 멤버 로그인 처리.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> loginMember(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();

		String errorParams = "";
		if(!this.commonService.valid(params, "companyId"))
			errorParams = this.commonService.appendText(errorParams, "회사id-companyId");
		if(!this.commonService.valid(params, "loginName"))
			errorParams = this.commonService.appendText(errorParams, "로그인id-loginName");
		if(!this.commonService.valid(params, "password"))
			errorParams = this.commonService.appendText(errorParams, "비밀번호-password");

		// 파라미터 유효성 검증.
		if(!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		String companyId = DataUtils.getString(params, "companyId", "");
		String loginName = DataUtils.getString(params, "loginName", "");
		String password = DataUtils.getString(params, "password", "");

		this.logger.debug("companyId : " + companyId);
		this.logger.debug("loginName : " + loginName);
		this.logger.debug("password : " + password);

		// 각 회사별 기간망 클래스 가져오기.
		ICompany company = this.commonService.getCompany(companyId);

		// 기간계 로그인
		if(!company.login(loginName, password)) {
			data.put("token", null);
			data.put("member", null);
			data.put("reason", this.messageHandler.getMessage("error.auth.login.reason3"));
			return data;
		}

		// 기간계 계정정보 조회.
		Map<String, Object> employee = company.employee(loginName);
		params.put("authLevel", 9);
		params.put("deptName", DataUtils.getString(employee, "deptName", ""));
		params.put("positionName", DataUtils.getString(employee, "posName", ""));
		params.put("useStatus", 1);
		params.put("name", DataUtils.getString(employee, "name", ""));

		// 계정 등록 또는 변경.
		this.loginDao.saveProfile(params);

		// 멤버정보 조회
		Member profile = this.loginDao.findProfile(params);
		profile.setCompanyName(company.getCompanyName());
		profile.setIsAdmin(1);
		profile.setLoginName(profile.getLoginName());
		this.logger.debug("member : " + profile.toString());

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> memberMap = objectMapper.convertValue(profile, Map.class);

		// 인증토큰 발급.
		String token = this.jwtService.generateToken(memberMap);
		this.logger.debug("token : " + token);
		data.put("token", token);
		data.put("profile", profile);

		return data;
	}

	/**
	 *
	 * @Method Name : loginCustomer
	 * @작성일 : 2020. 12. 11.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객 로그인 처리.
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> loginCustomer(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();

		String errorParams = "";
		if(!this.commonService.valid(params, "companyId"))
			errorParams = this.commonService.appendText(errorParams, "회사id-companyId");
		if(!this.commonService.valid(params, "gasappMemberNumber"))
			errorParams = this.commonService.appendText(errorParams, "가스앱 고객번호-gasappMemberNumber");

		// 파라미터 유효성 검증.
		if(!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		String companyId = DataUtils.getString(params, "companyId", "1");					// 회사id
		String gasappMemberNumber = DataUtils.getString(params, "gasappMemberNumber", "");	// 가스앱 고객번호.

		// 기간계 서버에서 고객정보 조회.
		Map<String, Object> profile = this.commonService.getCompany(companyId).getProfile(gasappMemberNumber);
		if(profile == null) {
			data.put("token", null);
			data.put("customer", null);
			data.put("reason", this.messageHandler.getMessage("error.auth.login.reason4"));
			return data;
		}
		params.put("name", DataUtils.getString(profile, "name", ""));		//  이름
		params.put("telNumber", DataUtils.getString(profile, "handphone", "").replaceAll("-", ""));	// 휴대폰번호

		// 고객정보 등록 - 이미 등록되어 있더라도 해당 프로시저를 실행하여야 함 - (기타 작업 존재)
		this.customerDao.regist(params);

		// 고객정보 조회.
		VCustomer customer = this.customerDao.findByGassappMemberNumber(params);
		this.logger.debug("customer : " + customer);

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> customerMap = objectMapper.convertValue(customer, Map.class);

		// 인증 토큰 발급.
		String token = this.jwtService.generateToken(customerMap);

		data.put("token", token);
		data.put("customer", customer);

		return data;
	}


	/**
	 *
	 * @Method Name : profile
	 * @작성일 : 2020. 12. 11.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 인증된 멤버 정보를 반환한다.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Member profile(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Token에서 회원정보 추출.
		Member member = getMember(request);
		params.put("id", member.getId());
		return this.memberDao.findMemberWithId(params);
	}


	/**
	 *
	 * @Method Name : getMember
	 * @작성일 : 2020. 12. 11.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 토큰 문자열을 파싱하여 멤버정보를 반환한다.
	 * @param token
	 * @return
	 */
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

	/**
	 *
	 * @Method Name : getCustomer
	 * @작성일 : 2020. 12. 11.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 토큰 문자열을 파싱하여 고객정보를 반환한다.
	 * @param token
	 * @return
	 */
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

