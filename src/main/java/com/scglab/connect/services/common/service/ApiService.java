package com.scglab.connect.services.common.service;

import java.net.HttpURLConnection;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.scglab.connect.utils.HttpUtils;

@SuppressWarnings({"unchecked", "rawtypes"})
@Service
public class ApiService<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
 
    private RestTemplate restTemplate;
 
    @Autowired
    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
 
    
	public ResponseEntity<T> get(String url, HttpHeaders httpHeaders) {
        return callApiEndpoint(url, HttpMethod.GET, httpHeaders, null, (Class<T>)Object.class);
    }
 
    public ResponseEntity<T> get(String url, HttpHeaders httpHeaders, Class<T> clazz) {
        return callApiEndpoint(url, HttpMethod.GET, httpHeaders, null, clazz);
    }
 
    public ResponseEntity<T> post(String url, HttpHeaders httpHeaders, Object body) {
        return callApiEndpoint(url, HttpMethod.POST, httpHeaders, body,(Class<T>)Object.class);
    }
 
    public ResponseEntity<T> post(String url, HttpHeaders httpHeaders, Object body, Class<T> clazz) {
        return callApiEndpoint(url, HttpMethod.POST, httpHeaders, body, clazz);
    }
 
    private ResponseEntity<T> callApiEndpoint(String url, HttpMethod httpMethod, HttpHeaders httpHeaders, Object body, Class<T> clazz) {
        return restTemplate.exchange(url, httpMethod, new HttpEntity<>(body, httpHeaders), clazz);
    }
    

    public int getStatusCode(String url){
    	return getStatusCode(url, null);
    }
    
    public int getStatusCode(String url, Map<String, String> params){
    	if (params != null) {
			String strParam = "";
			for (String key : params.keySet()) {
				String value = params.get(key);
				strParam += strParam.equals("") ? "?" : "&";
				strParam += key + "=" + value;
			}
			url += strParam;
		}
    	
 		ResponseEntity response = get(url, null);
		return response.getStatusCodeValue();
    }
    
    public Map<String, Object> getForMap(String url){
    	return getForMap(url, null);
    }
    
    public Map<String, Object> getForMap(String url, Map<String, String> params){
    	LocalTime startTime = LocalTime.now();
		logger.debug("외부통신 시작. : " + startTime);
		logger.debug("url : " + url);
		
    	Map<String, Object> data = null;
    	
    	if (params != null) {
			String strParam = "";
			for (String key : params.keySet()) {
				String value = params.get(key);
				strParam += strParam.equals("") ? "?" : "&";
				strParam += key + "=" + value;
			}
			url += strParam;
		}
    	
 		ResponseEntity response = get(url, null);
		if(response.getStatusCodeValue() == HttpURLConnection.HTTP_OK) {
			data = (Map<String, Object>) response.getBody();
		}
		
		LocalTime endTime = LocalTime.now();
		logger.debug("외부통신 종료 : " + endTime);

		Duration duration = Duration.between(startTime, endTime);

		long diffMillis = duration.toMillis();
		logger.info("외부통신 처리시간 : " + diffMillis + "ms");
 
		return data;
    }
    
    public List<Map<String, Object>> getForList(String url){
    	return getForList(url, null);
    }
    
    public List<Map<String, Object>> getForList(String url, Map<String, String> params){
    	LocalTime startTime = LocalTime.now();
		logger.debug("외부통신 시작. : " + startTime);
		logger.debug("url : " + url);
		
    	List<Map<String, Object>> data = null;
    	
    	if (params != null) {
			String strParam = "";
			for (String key : params.keySet()) {
				String value = params.get(key);
				strParam += strParam.equals("") ? "?" : "&";
				strParam += key + "=" + value;
			}
			url += strParam;
		}
    	
 		ResponseEntity response = get(url, null);
		if(response.getStatusCodeValue() == HttpURLConnection.HTTP_OK) {
			data = (List<Map<String, Object>>) response.getBody();
		}
		
		LocalTime endTime = LocalTime.now();
		logger.debug("외부통신 종료 : " + endTime);

		Duration duration = Duration.between(startTime, endTime);

		long diffMillis = duration.toMillis();
		logger.info("외부통신 처리시간 : " + diffMillis + "ms");
 
		return data;
    }
    
    public int postStatusCode(String url){
    	return postStatusCode(url, null);
    }
    
    public int postStatusCode(String url, Map<String, String> params){
    	LocalTime startTime = LocalTime.now();
		logger.debug("외부통신 시작. : " + startTime);
		logger.debug("url : " + url);
		
    	HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    	
 		ResponseEntity response = post(url, headers, params);
 		
 		LocalTime endTime = LocalTime.now();
		logger.debug("외부통신 종료 : " + endTime);

		Duration duration = Duration.between(startTime, endTime);

		long diffMillis = duration.toMillis();
		logger.info("외부통신 처리시간 : " + diffMillis + "ms");
		
		return response.getStatusCodeValue();
    }
    
    public Map<String, Object> postForMap(String url){
    	return postForMap(url, null);
    }
    
    public Map<String, Object> postForMap(String url, Map<String, String> params){
    	LocalTime startTime = LocalTime.now();
		logger.debug("외부통신 시작. : " + startTime);
		logger.debug("url : " + url);
		
    	Map<String, Object> data = null;
    	
    	HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    	
 		ResponseEntity response = post(url, headers, params);
		if(response.getStatusCodeValue() == HttpURLConnection.HTTP_OK) {
			data = (Map<String, Object>) response.getBody();
		}
		
		LocalTime endTime = LocalTime.now();
		logger.debug("외부통신 종료 : " + endTime);

		Duration duration = Duration.between(startTime, endTime);

		long diffMillis = duration.toMillis();
		logger.info("외부통신 처리시간 : " + diffMillis + "ms");
 
		return data;
    }
    
    public List<Map<String, Object>> postForList(String url){
    	return postForList(url, null);
    }
    
    public List<Map<String, Object>> postForList(String url, Map<String, String> params){
    	LocalTime startTime = LocalTime.now();
		logger.debug("외부통신 시작. : " + startTime);
		logger.debug("url : " + url);
		
    	List<Map<String, Object>> data = null;
    	
    	HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    	
 		ResponseEntity response = post(url, headers, params);
		if(response.getStatusCodeValue() == HttpURLConnection.HTTP_OK) {
			data = (List<Map<String, Object>>) response.getBody();
		}
 
		LocalTime endTime = LocalTime.now();
		logger.debug("외부통신 종료 : " + endTime);

		Duration duration = Duration.between(startTime, endTime);

		long diffMillis = duration.toMillis();
		logger.info("외부통신 처리시간 : " + diffMillis + "ms");
		
		return data;
    }
}