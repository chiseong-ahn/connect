package com.scglab.connect.services.common.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fcibook.quick.http.ResponseBody;
import com.scglab.connect.properties.DomainProperties;
import com.scglab.connect.utils.HttpUtils;

@Service
public class PushService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private DomainProperties domainProperty;
	
	@Value("${relay.use-example}") private boolean relayUseExample;
	
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
	public void sendPush(long member, String message) {
		this.logger.debug("[Send push] member - " + member);
		this.logger.debug("[Send push] message - " + message);
		
		if(this.relayUseExample) {
			this.logger.info("테스트 모드이므로 푸시를 발송하지 않음.");
		}else {
			HttpUtils httpUtils = new HttpUtils();
			
			String url = "https://" + this.domainProperty.getRelayScg() + "/api/cstalk/push";
			Map<String, String> params = new HashMap<String, String>();
			
			params.put("member", member + "");
			params.put("message", message);
			String content = JSONObject.toJSONString(params);
			
			ResponseBody body = httpUtils.requestForPostwithBodyContent(url, content);
			if(body.getStateCode() == Response.SC_OK) {
				this.logger.debug("Push발송 성공!");
			}else {
				this.logger.debug("Push발송 실패!");
			}
		}
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
