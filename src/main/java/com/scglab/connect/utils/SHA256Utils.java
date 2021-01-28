package com.scglab.connect.utils;

import java.security.MessageDigest;

public class SHA256Utils {

	/**
	 * 
	 * @Method Name : encrypt
	 * @작성일 : 2020. 12. 10.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 문자열 암호화.
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String msg) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(msg.getBytes());
		StringBuilder builder = new StringBuilder();
		for (byte b : md.digest()) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}

	public static void main(String[] args) {

		try {
			SHA256Utils sha256Utils = new SHA256Utils();

			// 평문 문자열
			String text = "안녕하세요";

			// 암호화된 문자열
			String encryptText = sha256Utils.encrypt(text);

			// 결과 출력
			System.out.println("text : " + text);
			System.out.println("encryptText : " + encryptText);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
