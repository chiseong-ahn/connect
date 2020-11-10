	package com.scglab.connect.utils;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scglab.connect.base.exception.UnauthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtils {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String secretKey = "#@SCGLAB_SDTALK__$$";

    private long tokenValidMilisecond = 1000L * 60 * 60 * 24; // 토큰 유효시간 - 1일. 
	//private long tokenValidMilisecond = 1000L * 30; // 토큰 유효시간 - 30초. 

    /**
     * 이름으로 Jwt Token을 생성한다.
     */
    public String generateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + this.tokenValidMilisecond);
        return generateToken(claims, expireDate);
    }
    
    public String generateToken(Map<String, Object> claims, Date expire) {
        Date now = new Date();
        return Jwts.builder()
                //.setId(key)
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(expire) // 유효시간 설정
                .signWith(SignatureAlgorithm.HS256, this.secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();
    }

    /**
     * Jwt Token을 복호화 하여 식별자를 얻는다.
     */
    public Claims getJwtData(String jwt) {
    	jwt = jwt.replaceAll("Bearer", "").trim();
        return getClaims(jwt).getBody();
    }

    /**
     * Jwt Token의 유효성을 체크한다.
     */
    public boolean validateToken(String jwt) {
    	boolean isValid = false;
    	try {
    		Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
    		isValid = true;
    	}catch(Exception e) {
    		throw new UnauthorizedException("error.auth.type2");
    	}
        return isValid;
    }

    private Jws<Claims> getClaims(String jwt) {
        try {
        	return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
        } catch (Exception ex) {
        	throw new UnauthorizedException("error.auth.type2");
        }
    }
}
