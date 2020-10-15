package com.scglab.connect.services.common.auth;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
	
	public Map<String, Object> login(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		// 회원정보 조회
		Map<String, Object> object = this.authDao.selectOne(params);
		
		// 회원정보가 존재하지 않을 경우
		if(object == null) {
			data.put("login", false);
			
		}else {
			// 인증토큰(JWT) 생성.
			JwtUtils jwtUtil = new JwtUtils();
			String token = jwtUtil.generateToken(object);
			this.logger.debug("token : " + token);
			object.put("token", token);
			
			data.put("login", true);
			data.put("token", token);
		}
		return data;
	}
}
