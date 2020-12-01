package com.scglab.connect.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpUtils {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static void main(String[] args) throws JSONException, JsonMappingException, JsonProcessingException {
		HttpUtils httpUtils = new HttpUtils();
		String url = "http://localhost:8080/api/stats/customer-analysis";
		
		Map<String, Object> params = new HashMap<String, Object>();
		HttpHeaders headers = new HttpHeaders(); 
		
		List<Map<String, Object>> result = httpUtils.postApiForList(url, params, headers);
		System.out.println("result : " + result.toString());
	}
	
	public String getApiForString(String url) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(5000); // 타임아웃 설정 5초
		factory.setReadTimeout(5000);// 타임아웃 설정 5초
		RestTemplate restTemplate = new RestTemplate(factory);

		ResponseEntity<String> res = restTemplate.getForEntity(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(res.getBody(), String.class);
	}
	
	public List<Map<String, Object>> getApiForList(String url) {
		String value = getApiForString(url);
		try {
			return new ObjectMapper().readValue(value, new TypeReference<List<Map<String, Object>>>(){});
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<String, Object> getApiForMap(String url) {
		String value = getApiForString(url);
		try {
			return new ObjectMapper().readValue(value, new TypeReference<Map<String, Object>>(){});	
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String postApiForString(String url, Map<String, Object> params, HttpHeaders headers) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(500000); // 타임아웃 설정 5초
		factory.setReadTimeout(500000);// 타임아웃 설정 5초
		RestTemplate restTemplate = new RestTemplate(factory);

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(params, headers);
		return restTemplate.postForObject(url, httpEntity, String.class);
	}

	public Map<String, Object> postApiForMap(String url, Map<String, Object> params, HttpHeaders headers) {
		String value = postApiForString(url, params, headers);
		try {
			return new ObjectMapper().readValue(value, new TypeReference<Map<String, Object>>(){});	
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Map<String, Object>> postApiForList(String url, Map<String, Object> params, HttpHeaders headers) {
		String value = postApiForString(url, params, headers);
		try {
			return new ObjectMapper().readValue(value, new TypeReference<List<Map<String, Object>>>(){});	
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
