package com.scglab.connect.services.auth;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.utils.JwtTokenUtils;

@Service
public class AuthService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AuthDao authDao;
	
	public Map<String, Object> object(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		// 회원정보 조회
		Map<String, Object> object = this.authDao.selectOne(params);
		// 회원정보가 존재할 경우
		if(object == null) {
			data.put("RESULT", false);
		}else {
			JwtTokenUtils jwtTokenUtil = new JwtTokenUtils();
			String token = jwtTokenUtil.generateToken(object);
			
			this.logger.debug("token : " + token);
			object.put("token", token);
			
			data.put("result", true);
			data.put("token", token);
		}
		return data;
	}
}
