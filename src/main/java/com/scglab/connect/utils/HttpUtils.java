package com.scglab.connect.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fcibook.quick.http.QuickHttp;
import com.fcibook.quick.http.ResponseBody;


public class HttpUtils {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static void main(String[] args) throws JSONException, JsonMappingException, JsonProcessingException {
		HttpUtils httpUtils = new HttpUtils();
		
		
	}
	
	/**
	 * 
	 * @Method Name : getForResponseBody
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Get 방식으로 요청하고 결과를 ResponseBody 객체를 반환한다.
	 * @param url
	 * @return
	 */
	public static ResponseBody getForResponseBody(String url) {
		return getForResponseBody(url, null);
	}
	
	
	/**
	 * 
	 * @Method Name : getForResponseBody
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Get 방식으로 요청하고 결과를 ResponseBody 객체를 반환한다.
	 * @param url
	 * @param params
	 * @return
	 */
	public static ResponseBody getForResponseBody(String url, Map<String, String> params) {
		return requestForGet(url, params);
	}
	
	
	/**
	 * 
	 * @Method Name : getForString
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Get 방식으로 요청하고 결과를 String 문자열로 반환한다.
	 * @param url
	 * @return
	 */
	public static String getForString(String url) {
		return getForString(url, null);
	}
	
	
	/**
	 * 
	 * @Method Name : getForString
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Get 방식으로 요청하고 결과를 String 문자열로 반환한다.
	 * @param url
	 * @param params
	 * @return
	 */
	public static String getForString(String url, Map<String, String> params) {
		ResponseBody body = requestForGet(url, params);
		
		// 요청 성공시(200)
		if(body.getStateCode() == Response.SC_OK) {
			String text = body.text();
			return text;
		}
		
		return null;
	}
	
	
	/**
	 * 
	 * @Method Name : getForMap
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Get 방식으로 요청하고 Json 결과를 Map<String, Object> 객체로 반환한다.
	 * @param url
	 * @return
	 */
	public static Map<String, Object> getForMap(String url){
		return getForMap(url, null);
	}
	
	/**
	 * 
	 * @Method Name : getForMap
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Get 방식으로 요청하고 Json 결과를 Map<String, Object> 객체로 반환한다.
	 * @param url
	 * @param params
	 * @return
	 */
	public static Map<String, Object> getForMap(String url, Map<String, String> params) {
		Map<String, Object> obj = null;
		
		ResponseBody body = requestForGet(url, params);
		
		// 요청 성공시(200)
		if(body.getStateCode() == Response.SC_OK) {
			String text = body.text();
			
			try {
				// String을 Map<String, Object> 객체로 변환.
				obj = new ObjectMapper().readValue(text, new TypeReference<Map<String, Object>>(){});
				
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
		
		return obj;
	}
	
	
	/**
	 * 
	 * @Method Name : getForList
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Get 방식으로 요청하고 Json 결과를 List<Map<String, Object>> 객체로 반환한다.
	 * @param url
	 * @return
	 */
	public static List<Map<String, Object>> getForList(String url){
		return getForList(url, null);
	}
	
	
	/**
	 * 
	 * @Method Name : getForList
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Get 방식으로 요청하고 Json 결과를 List<Map<String, Object>> 객체로 반환한다.
	 * @param url
	 * @param params
	 * @return
	 */
	public static List<Map<String, Object>> getForList(String url, Map<String, String> params) {
		List<Map<String, Object>> list = null;
		
		ResponseBody body = requestForGet(url, params);
		
		// 요청 성공시(200)
		if(body.getStateCode() == Response.SC_OK) {
			String text = body.text();
			
			try {
				// String을 List<Map<String, Object>> 객체로 변환.
				list = new ObjectMapper().readValue(text, new TypeReference<List<Map<String, Object>>>(){});
				
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
		
		return list;
	}
	
	
	/**
	 * 
	 * @Method Name : postForResponseBody
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Post 방식으로 요청하고 결과를 ResponseBodoy 객체로 반환한다.
	 * @param url
	 * @return
	 */
	public static ResponseBody postForResponseBody(String url) {
		return postForResponseBody(url, null);
	}
	
	
	/**
	 * 
	 * @Method Name : postForResponseBody
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Post 방식으로 요청하고 결과를 ResponseBodoy 객체로 반환한다.
	 * @param url
	 * @param params
	 * @return
	 */
	public static ResponseBody postForResponseBody(String url, Map<String, String> params) {
		return requestForPost(url, params);
	}
	
	/**
	 * 
	 * @Method Name : postForString
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Post 방식으로 요청하고 결과를 String 문자열로 반환한다.
	 * @param url
	 * @return
	 */
	public static String postForString(String url) {
		return postForString(url, null);
	}
	
	/**
	 * 
	 * @Method Name : postForString
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Post 방식으로 요청하고 결과를 String 문자열로 반환한다.
	 * @param url
	 * @param params
	 * @return
	 */
	public static String postForString(String url, Map<String, String> params) {
		ResponseBody body = requestForPost(url, params);
		
		// 요청 성공시(200)
		if(body.getStateCode() == Response.SC_OK) {
			String text = body.text();
			return text;
		}
		
		return null;
	}
	
	
	/**
	 * 
	 * @Method Name : postForList
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Post 방식으로 요청하고 Json 결과를 Map<String, Object> 객체로 반환한다.
	 * @param url
	 * @return
	 */
	public static List<Map<String, Object>> postForList(String url){
		return postForList(url, null);
	}
	
	
	/**
	 * 
	 * @Method Name : postForList
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Post 방식으로 요청하고 Json 결과를 Map<String, Object> 객체로 반환한다.
	 * @param url
	 * @param params
	 * @return
	 */
	public static List<Map<String, Object>> postForList(String url, Map<String, String> params) {
		List<Map<String, Object>> list = null;
		
		ResponseBody body = requestForPost(url, params);
		
		// 요청 성공시(200)
		if(body.getStateCode() == Response.SC_OK) {
			String text = body.text();
			
			try {
				// String을 List<Map<String, Object>> 객체로 변환.
				list = new ObjectMapper().readValue(text, new TypeReference<List<Map<String, Object>>>(){});
				
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : postForMap
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Get 방식으로 요청하고 Json 결과를 List<Map<String, Object>> 객체로 반환한다.
	 * @param url
	 * @return
	 */
	public static Map<String, Object> postForMap(String url) {
		return postForMap(url, null);
	}
	
	/**
	 * 
	 * @Method Name : postForMap
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : Get 방식으로 요청하고 Json 결과를 List<Map<String, Object>> 객체로 반환한다.
	 * @param url
	 * @param params
	 * @return
	 */
	public static Map<String, Object> postForMap(String url, Map<String, String> params) {
		Map<String, Object> obj = null;
		
		ResponseBody body = requestForPost(url, params);
		
		System.out.println("body.getStateCode() : " + body.getStateCode());
		// 요청 성공시(200)
		if(body.getStateCode() == Response.SC_OK) {
			String text = body.text();
			
			try {
				// String을 Map<String, Object> 객체로 변환.
				obj = new ObjectMapper().readValue(text, new TypeReference<Map<String, Object>>(){});
				
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
		
		return obj;
	}
	
	
	private static ResponseBody requestForGet(String url, Map<String, String> parames) {
		parames = parames == null ? new HashMap<String, String>() : parames;
		ResponseBody body = new QuickHttp()
	               .url(url)
	               .get()
	               .addParames(parames)
	               .body();
		
		return body;
	}
	
	
	private static ResponseBody requestForPost(String url, Map<String, String> parames) {
		parames = parames == null ? new HashMap<String, String>() : parames;
		ResponseBody body = (ResponseBody) new QuickHttp()
	               .url(url)
	               .post()
	               .addParames(parames)
	               .body();
		
		return body;
	}
	
	
	
	
}
