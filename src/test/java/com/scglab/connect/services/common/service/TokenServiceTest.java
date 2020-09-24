package com.scglab.connect.services.common.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TokenServiceTest {
	
	@Test
	public void testToken() {
		
		// 토큰 서비스.
		TokenService tokenService = new TokenService();
		
		// header 데이터 정의.
		Map<String, Object> header = new HashMap<String, Object>();
		String headerKey = "HEADER_KEY";
		String headerVal = "HEADER_VAL";
		header.put(headerKey, headerVal);
		
		// payload 데이터 정의.
		Map<String, Object> claims = new HashMap<String, Object>();
		String claimKey = "USER_ID";
		String claimVal = "csahn";
		claims.put(claimKey, claimVal);
		
		// test1. JWT토큰 생성 확인.
		String tokenString = tokenService.create(header, claims);
		assertNotEquals(tokenString, null);
		assertNotEquals(tokenString, "");
		
		// test2. 토큰 유효성 확인.
		boolean verify = tokenService.verify(tokenString);
		assertEquals(verify, true);
		
		// test3. 토큰 헤더값 확인.
		String value1 = (String)tokenService.getHeader(tokenString).get(headerKey);;
		assertEquals(value1, headerVal);
		
		// test4. 토큰 payload 확인.
		String value2 = (String)tokenService.getBody(tokenString).get(claimKey);
		assertEquals(value2, claimVal);
	}
}
