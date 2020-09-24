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
		
		TokenService tokenService = new TokenService();
		
		Map<String, Object> header = new HashMap<String, Object>();
		String headerKey = "HEADER_KEY";
		String headerVal = "HEADER_VAL";
		header.put(headerKey, headerVal);
		
		Map<String, Object> claims = new HashMap<String, Object>();
		String claimKey = "USER_ID";
		String claimVal = "csahn";
		claims.put(claimKey, claimVal);
		
		String tokenString = tokenService.create(header, claims);
			
		assertNotEquals(tokenString, null);
		assertNotEquals(tokenString, "");
		
		boolean verify = tokenService.verify(tokenString);
		assertEquals(verify, true);
		
		String value1 = (String)tokenService.getHeader(tokenString).get(headerKey);;
		assertEquals(value1, headerVal);
		
		String value2 = (String)tokenService.getBody(tokenString).get(claimKey);
		assertEquals(value2, claimVal);
	}

}
