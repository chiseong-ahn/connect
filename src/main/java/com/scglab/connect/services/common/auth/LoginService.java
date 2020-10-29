package com.scglab.connect.services.common.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.adminmenu.emp.EmpDao;
import com.scglab.connect.services.common.service.MessageService;
import com.scglab.connect.services.external.External;
import com.scglab.connect.services.external.ExternalInc;
import com.scglab.connect.services.external.ExternalScg;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.JwtUtils;

@Service
public class LoginService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private AuthDao authDao;
	
	@Autowired
	private EmpDao empDao;
	
	private long refreshTokenValidMilisecond = 1000L * 60 * 60 * 24 * 30; // 토큰 유효시간 - 1달. 
	private long accessTokenValidMilisecond = 1000L * 60 * 60 * 24; // 토큰 유효시간 - 1일. 
	
	public Map<String, Object> login(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		String empno = DataUtils.getString(params, "empno", "");
		String passwd = DataUtils.getString(params, "passwd", "");
		int cid = Integer.parseInt(DataUtils.getString(params, "cid", "0"));
		
		this.logger.debug("empno : " + empno);
		this.logger.debug("passwd : " + passwd);
		this.logger.debug("cid : " + cid);
		
		if(empno.equals("") || passwd.equals("") || cid == 0) {	// 로그인 정보가 부족할 경우.
			data.put("login", false);
			data.put("reason", this.messageService.getMessage("auth.login.reason.type0"));
			return data;
		}
			
		// 외부 연동클래스.
		External external = null;
		if(cid == 1) {		// 서울도시가스일 경우.
			external = new ExternalScg();
		
		}else if(cid == 2) {	// 인천도시가스일 경우.
			external = new ExternalInc();
		}
		User user = external.login(empno, passwd);		// 기간계 로그인.
		
		if(user == null) {
			// 기간계에 로그인 정보가 맞지않을경우.
			
			data.put("login", false);
			data.put("reason", this.messageService.getMessage("auth.login.r2eason.type1"));
			return data;
		}
		
		// 회원정보 조회
		Map<String, Object> object = this.authDao.selectOne(params);
		
		// 회원정보가 존재하지 않을 경우
		if(object == null) {
			data.put("login", false);
			data.put("reason", this.messageService.getMessage("auth.login.reason.type2"));
			return data;
			
		}else {
			
			Date now = new Date();
			
			// 인증토큰(JWT) 생성.
			JwtUtils jwtUtil = new JwtUtils();
			String accessToken = jwtUtil.generateToken(object, new Date(now.getTime() + this.accessTokenValidMilisecond));
			
//			Map<String, Object> refreshData = new HashMap<String, Object>();
//			refreshData.put("empno", DataUtils.getString(object, "empno", ""));
//			refreshData.put("cid", DataUtils.getInt(object, "cid", 0));
//			refreshData.put("accessToken", accessToken);
//			
//			String refreshToken = jwtUtil.generateToken(refreshData, new Date(now.getTime() + this.refreshTokenValidMilisecond));
//				Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
//				refreshCookie.setSecure(true);
//				
//				response.addCookie(refreshCookie);
			
			this.logger.debug("accessToken : " + accessToken);
//			this.logger.debug("refreshToken : " + refreshToken);
			
			data.put("login", true);
			data.put("accessToken", accessToken);
//			data.put("refreshToken", refreshToken);
		}
		return data;
	}
}
