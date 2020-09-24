package com.scglab.connect.services.common.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String secretKey = "JWT_SECRET_KEY";
	
	public String create(Map<String, Object> header, Map<String, Object> claims) {
		
		String token = "";
		token = Jwts.builder()
				.setHeader(header)
				.addClaims(claims)
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
		
		return token;
	}
	
	public boolean verify(String token) {
		try {
			Jws<Claims> claim = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			if(claim != null) {
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public JwsHeader<?> getHeader(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getHeader();
	}
	
	public Claims getBody(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}
	
}
