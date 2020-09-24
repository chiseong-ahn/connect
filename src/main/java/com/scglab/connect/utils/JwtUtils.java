package com.scglab.connect.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtils {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	String secretKey = "sdfslfsd!";
	
	public String createToken(String payload) {
		
		String token = "";
		token = Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setHeaderParam("site", "connect")
				.setSubject(payload)
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
		
		this.logger.info("token : " + token);
	
		return token;
	}
	
	public boolean decodeToken(String token) {
		
		
		
		return true;
	}
}
