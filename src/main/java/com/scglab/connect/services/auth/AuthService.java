package com.scglab.connect.services.auth;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.JwtUtils;

@Service
public class AuthService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AuthDao authDao;
	
	public Map<String, Object> login(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		// 회원정보 조회
		Map<String, Object> object = this.authDao.selectOne(params);
		
		// 회원정보가 존재하지 않을 경우
		if(object == null) {
			data.put("login", false);
			
		}else {
//			
//			User user = new User();
//				user.setCid(DataUtils.getObjectValue(object, "cid", ""));
//				user.setAuth(DataUtils.getObjectValue(object, "auth", ""));
//				user.setEmp(DataUtils.getObjectValue(object, "emp", ""));
//				user.setSpeaker(DataUtils.getObjectValue(object, "speaker", ""));
//				user.setEmpno(DataUtils.getObjectValue(object, "empno", ""));
//				user.setName(DataUtils.getObjectValue(object, "name", ""));
			
			JwtUtils jwtUtil = new JwtUtils();
			String token = jwtUtil.generateToken(object);
			this.logger.debug("token : " + token);
			object.put("token", token);
			
			data.put("login", true);
			data.put("token", token);
		}
		return data;
	}
	
	public Map<String, Object> logout(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		HttpSession session = (HttpSession) request.getSession();
		session.removeAttribute("user");
		
		data.put("result", true);
		
		return data;
	}
}
