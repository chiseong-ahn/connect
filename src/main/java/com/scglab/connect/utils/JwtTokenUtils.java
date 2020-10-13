package com.scglab.connect.utils;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JwtTokenUtils {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String secretKey = "#@SCGLAB_SDTALK__$$";

    private long tokenValidMilisecond = 1000L * 60 * 60; // 1시간만 토큰 유효

    /**
     * 이름으로 Jwt Token을 생성한다.
     */
    public String generateToken(Map<String, Object> claims) {
        Date now = new Date();
        return Jwts.builder()
                //.setId(key)
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + this.tokenValidMilisecond)) // 유효시간 설정
                .signWith(SignatureAlgorithm.HS256, this.secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();
    }

    /**
     * Jwt Token을 복호화 하여 식별자를 얻는다.
     */
    public String getUserNameFromJwt(String jwt) {
        return getClaims(jwt).getBody().getId();
    }

    /**
     * Jwt Token의 유효성을 체크한다.
     */
    public boolean validateToken(String jwt) {
        return this.getClaims(jwt) != null;
    }

    private Jws<Claims> getClaims(String jwt) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
        } catch (SignatureException ex) {
            this.logger.error("Invalid JWT signature");
            throw ex;
        } catch (MalformedJwtException ex) {
        	this.logger.error("Invalid JWT token");
            throw ex;
        } catch (ExpiredJwtException ex) {
        	this.logger.error("Expired JWT token");
            throw ex;
        } catch (UnsupportedJwtException ex) {
        	this.logger.error("Unsupported JWT token");
            throw ex;
        } catch (IllegalArgumentException ex) {
        	this.logger.error("JWT claims string is empty.");
            throw ex;
        }
    }
}
