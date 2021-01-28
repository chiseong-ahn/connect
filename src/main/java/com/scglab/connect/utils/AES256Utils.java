package com.scglab.connect.utils;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.scglab.connect.constant.Constant;

public class AES256Utils {
	private String iv;
	private Key keySpec;

	// 32byte 암/복호화 비밀키
	final static String key = Constant.ENCRYPT_SECRETKEY;

	public AES256Utils() throws UnsupportedEncodingException {
		this.iv = key.substring(0, 16);
		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length) {
			len = keyBytes.length;
		}
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

		this.keySpec = keySpec;
	}

	/**
	 * 
	 * @Method Name : encrypt
	 * @작성일 : 2020. 12. 10.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 문자열에 대한 암호화.
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public String encrypt(String str)
			throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		String enStr = new String(Base64.encodeBase64(encrypted));
		return enStr;
	}

	/**
	 * 
	 * @Method Name : decrypt
	 * @작성일 : 2020. 12. 10.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 암호화된 문자열에 대한 복호화.
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public String decrypt(String str)
			throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		byte[] byteStr = Base64.decodeBase64(str.getBytes());
		return new String(c.doFinal(byteStr), "UTF-8");
	}

	public static void main(String[] args) {

		try {
			AES256Utils aes256Utils = new AES256Utils();

			// 평문 문자열
			String text = "안녕하세요";

			// 암호화된 문자열
			String encryptText = aes256Utils.encrypt(text);

			// 복호화된 문자열
			String decryptText = aes256Utils.decrypt(encryptText);

			// 결과 출력
			System.out.println("text : " + text);
			System.out.println("encryptText : " + encryptText);
			System.out.println("decryptText : " + decryptText);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
