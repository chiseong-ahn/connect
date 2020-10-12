package com.scglab.connect.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpUtils {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Map<String, Object> getApi(String url) throws JsonProcessingException {

		Map<String, Object> result = new HashMap<String, Object>();

		try {

			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 타임아웃 설정 5초
			factory.setReadTimeout(5000);// 타임아웃 설정 5초
			RestTemplate restTemplate = new RestTemplate(factory);

			// 이 한줄의 코드로 API를 호출해 MAP타입으로 전달 받는다.
			ResponseEntity<Map> resultMap = restTemplate.getForEntity(url, Map.class);

			// 데이터를 제대로 전달 받았는지 확인 string형태로 파싱해줌
			ObjectMapper mapper = new ObjectMapper();
			String data = mapper.writeValueAsString(resultMap.getBody());
			this.logger.debug("data : " + data);

		} catch (HttpClientErrorException | HttpServerErrorException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}
	
	public Map<String, Object> postApi(String url, StringBuffer params, HttpHeaders headers) throws JsonProcessingException {

		Map<String, Object> result = new HashMap<String, Object>();

		try {

			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setConnectTimeout(5000); // 타임아웃 설정 5초
			factory.setReadTimeout(5000);// 타임아웃 설정 5초
			RestTemplate restTemplate = new RestTemplate(factory);

			HttpEntity<StringBuffer> request = new HttpEntity<>(params, headers);

			// 이 한줄의 코드로 API를 호출해 MAP타입으로 전달 받는다.
			ResponseEntity<Map> resultMap = restTemplate.postForEntity(url, request, Map.class);
			result.put("statusCode", resultMap.getStatusCodeValue()); // http status code를 확인
			result.put("header", resultMap.getHeaders()); // 헤더 정보 확인
			result.put("body", resultMap.getBody()); // 실제 데이터 정보 확인

			// 데이터를 제대로 전달 받았는지 확인 string형태로 파싱해줌
			ObjectMapper mapper = new ObjectMapper();
			String data = mapper.writeValueAsString(resultMap.getBody());
			this.logger.debug("data : " + data);

		} catch (HttpClientErrorException | HttpServerErrorException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}
}
