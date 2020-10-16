package com.scglab.connect.services.common.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.Constant;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.JwtUtils;

@Service
public class AuthService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AuthDao authDao;
	
	private long refreshTokenValidMilisecond = 1000L * 60 * 60 * 24 * 30; // 토큰 유효시간 - 1달. 
	private long accessTokenValidMilisecond = 1000L * 60 * 60 * 24; // 토큰 유효시간 - 1일. 
	
	
	
	/**
	 * 
	 * @Method Name : setUserInfo
	 * @작성일 : 2020. 10. 14.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 사용자 정보 설정.
	 * @param userInfo
	 * @param request
	 */
	public void setUserInfo(Map<String, Object> userInfo, HttpServletRequest request) {
		User user = new User();
		user.setCid(DataUtils.getInt(userInfo, "cid", 1));
		user.setAuth(DataUtils.getInt(userInfo, "auth", 9));
		user.setEmp(DataUtils.getInt(userInfo, "emp", 0));
		user.setEmpno(DataUtils.getObjectValue(userInfo, "empno", ""));
		user.setName(DataUtils.getObjectValue(userInfo, "name", ""));
		user.setSpeaker(DataUtils.getInt(userInfo, "speaker", 0));
		
		request.setAttribute(Constant.AUTH_USER, user);
	}
	
	public User getUserInfo(Map<String, Object> userInfo) {
		User user = new User();
		user.setCid(DataUtils.getInt(userInfo, "cid", 1));
		user.setAuth(DataUtils.getInt(userInfo, "auth", 9));
		user.setEmp(DataUtils.getInt(userInfo, "emp", 0));
		user.setEmpno(DataUtils.getObjectValue(userInfo, "empno", ""));
		user.setName(DataUtils.getObjectValue(userInfo, "name", ""));
		user.setSpeaker(DataUtils.getInt(userInfo, "speaker", 0));
		
		return user;
	}
	
	/**
	 * 
	 * @Method Name : getUserInfo
	 * @작성일 : 2020. 10. 14.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 사용자 정보 조회.
	 * @param request
	 * @return
	 */
	public User getUserInfo(HttpServletRequest request) {
		if(request.getAttribute(Constant.AUTH_USER) == null) {
			return null;
		}
		User user = (User)request.getAttribute(Constant.AUTH_USER);
		return user;
	}
	
	public Map<String, Object> login(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		// 회원정보 조회
		Map<String, Object> object = this.authDao.selectOne(params);
		
		// 회원정보가 존재하지 않을 경우
		if(object == null) {
			data.put("login", false);
			
		}else {
			
			Date now = new Date();
			
			// 인증토큰(JWT) 생성.
			JwtUtils jwtUtil = new JwtUtils();
			String accessToken = jwtUtil.generateToken(object, new Date(now.getTime() + this.accessTokenValidMilisecond));
			
			Map<String, Object> refreshData = new HashMap<String, Object>();
			refreshData.put("empno", DataUtils.getString(object, "empno", ""));
			refreshData.put("cid", DataUtils.getInt(object, "cid", 0));
			refreshData.put("accessToken", accessToken);
			
			
			String refreshToken = jwtUtil.generateToken(refreshData, new Date(now.getTime() + this.refreshTokenValidMilisecond));
//			Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
//			refreshCookie.setSecure(true);
//			
//			response.addCookie(refreshCookie);
			
			
			
			this.logger.debug("accessToken : " + accessToken);
			this.logger.debug("refreshToken : " + refreshToken);
			
			data.put("login", true);
			data.put("accessToken", accessToken);
			data.put("refreshToken", refreshToken);
		}
		return data;
	}
	
	public Map<String, Object> refreshToken(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 인증토큰(JWT) 생성.
		JwtUtils jwtUtils = new JwtUtils();
		Date now = new Date();
		
		Map<String, Object> data = new HashMap<String, Object>();
		boolean result = false;
		
		String refreshToken = DataUtils.getString(params, "refreshToken", "");
		if(!refreshToken.equals("")) {
			if(jwtUtils.validateToken(refreshToken)) {
				Map<String, Object> claims = jwtUtils.getJwtData(refreshToken);
				if(claims.containsKey("accessToken")) {
					String token = DataUtils.getSafeValue((String)claims.get("accessToken"));
					String empno = DataUtils.getSafeValue((String)claims.get("empno"));
					String cid = DataUtils.getSafeValue((String)claims.get("cid"));
					params.put("empno", empno);
					params.put("cid", cid);
					
					String accessToken = (String)request.getAttribute("accessToken");
					if(token.equals(accessToken)) {
						
						// 회원정보 조회
						Map<String, Object> object = this.authDao.selectOne(params);
						
						String newAccessToken = jwtUtils.generateToken(object, new Date(now.getTime() + this.accessTokenValidMilisecond));
						
						Map<String, Object> refreshData = new HashMap<String, Object>();
						refreshData.put("empno", DataUtils.getString(object, "empno", ""));
						refreshData.put("cid", DataUtils.getString(object, "cid", ""));
						refreshData.put("accessToken", accessToken);
						String newRefreshToken = jwtUtils.generateToken(refreshData, new Date(now.getTime() + this.refreshTokenValidMilisecond));
						
						result = true;
						data.put("accessToken", accessToken);
						data.put("refreshToken", refreshToken);
					}
				}
			}
		}
		data.put("result", result);
		return data;
	}
	
	
}
