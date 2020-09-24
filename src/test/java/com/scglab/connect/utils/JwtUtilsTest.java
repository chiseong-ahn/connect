package com.scglab.connect.utils;

import static org.junit.Assert.assertNotEquals;

import org.junit.jupiter.api.Test;

class JwtUtilsTest {

	@Test
	void testCreateToken() {
		JwtUtils jwtUtils = new JwtUtils();
		String payload = "TEST Message";
		String token = "";
		
		try {
			token = jwtUtils.createToken(payload);
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("token : " + token);
		
		assertNotEquals(token, "");
	}

//	@Test
//	void testVerify() {
//		fail("Not yet implemented");
//	}

}
