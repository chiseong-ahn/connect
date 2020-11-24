package com.scglab.connect.services.common.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scglab.connect.properties.DomainProperties;
import com.scglab.connect.utils.HttpUtils;

@Service
public class PushService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DomainProperties domainProperty;
	
	/**
	 * 
	 * @Method Name : sendPush
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 텍스트 기반 푸시발송 모듈.
	 * @param userno
	 * @param message
	 * @throws JsonProcessingException 
	 */
	public void sendPush(int userno, String message) {
		this.logger.debug("[Send push] userno - " + userno);
		this.logger.debug("[Send push] message - " + message);
		
		HttpUtils httpUtils = new HttpUtils();
		
		String url = "https://" + this.domainProperty.getMobileapi() + "/api/matt/push";
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("member", userno);
		params.put("message", message);
		
		HttpHeaders headers = new HttpHeaders();
		httpUtils.postApiForString(url, params, headers);
	}
	
	/**
	 * 
	 * @Method Name : sendPush
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 이미지 기반 푸시발송 모듈.
	 * @param userno
	 * @param message
	 * @param imgUrl
	 */
	public void sendPush(int userno, String message, String imgUrl) {
		
	}
}
