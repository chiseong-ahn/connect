package com.scglab.connect.services.common.service;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.scglab.connect.constant.Constant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 이름으로 Jwt Token을 생성한다.
	 */
	public String generateToken(Map<String, Object> claims) {
		Date now = new Date();
		Date expireDate = new Date(now.getTime() + Constant.JSONTOKEN_EXPIRE);
		return generateToken(claims, expireDate);
	}

	public String generateToken(Map<String, Object> claims, Date expire) {
		Date now = new Date();
		/*
		 * return Jwts.builder() //.setId(key) .setClaims(claims) .setIssuedAt(now) //
		 * 토큰 발행일자 .setExpiration(expire) // 유효시간 설정 .signWith(SignatureAlgorithm.HS256,
		 * Constant.JSONTOKEN_SECRETKEY) // 암호화 알고리즘, secret값 세팅 .compact();
		 */
		// Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		return Jwts.builder()
				// .setSubject("Joe")
				.setClaims(claims).setExpiration(expire).setIssuedAt(new Date()).signWith(Constant.JWT_KEY).compact();
	}

	/**
	 * Jwt Token을 복호화 하여 식별자를 얻는다.
	 */
	public Claims getJwtData(String jwt) {
		jwt = jwt.replaceAll("Bearer", "").trim();
		Claims claims = null;

		try {
			claims = getClaims(jwt).getBody();
		} catch (Exception e) {
			//
		}

		return claims;
	}

	/**
	 * Jwt Token의 유효성을 체크한다.
	 */
	public boolean validateToken(String jws) {
		boolean isValid = false;
		try {
			Jwts.parserBuilder().setSigningKey(Constant.JWT_KEY).build().parseClaimsJws(jws);
			isValid = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isValid;
	}

	private Jws<Claims> getClaims(String jws) {
		try {
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(Constant.JWT_KEY).build().parseClaimsJws(jws);
			return claims;
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
