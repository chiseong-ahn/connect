package com.scglab.connect.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.apache.http.entity.ContentType;
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

	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

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
		if (body != null && body.getStateCode() == Response.SC_OK) {
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
	public static Map<String, Object> getForMap(String url) {
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
		if (body != null && body.getStateCode() == Response.SC_OK) {
			String text = body.text();

			try {
				obj = new ObjectMapper().readValue(text, new TypeReference<Map<String, Object>>() {
				});

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
	public static List<Map<String, Object>> getForList(String url) {
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
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		ResponseBody body = requestForGet(url, params);

		// 요청 성공시(200)
		if (body != null && body.getStateCode() == Response.SC_OK) {
			try {
				list = new ObjectMapper().readValue(body.text(), new TypeReference<List<Map<String, Object>>>() {
				});

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
		if (body.getStateCode() == Response.SC_OK) {
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
	public static List<Map<String, Object>> postForList(String url) {
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
		if (body.getStateCode() == Response.SC_OK) {
			String text = body.text();

			try {
				// String을 List<Map<String, Object>> 객체로 변환.
				list = new ObjectMapper().readValue(text, new TypeReference<List<Map<String, Object>>>() {
				});

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

		logger.debug(params.toString());
		ResponseBody body = requestForPost(url, params);

		logger.debug("body.getStateCode() : " + body.getStateCode());
		// 요청 성공시(200)
		if (body.getStateCode() == Response.SC_OK) {
			String text = body.text();

			try {
				// String을 Map<String, Object> 객체로 변환.
				obj = new ObjectMapper().readValue(text, new TypeReference<Map<String, Object>>() {
				});

			} catch (Exception e) {
				e.printStackTrace();

			}
		}

		return obj;
	}

	public static Map<String, Object> postForMapWithBodyContent(String url, String content) {
		Map<String, Object> obj = null;

		ResponseBody body = requestForPostwithBodyContent(url, content);

		// 요청 성공시(200)
		if (body.getStateCode() == Response.SC_OK) {
			String text = body.text();
			
			try {
				// String을 Map<String, Object> 객체로 변환.
				obj = new ObjectMapper().readValue(text, new TypeReference<Map<String, Object>>() {
				});

			} catch (Exception e) {
				e.printStackTrace();

			}
		}

		return obj;
	}

	private static ResponseBody requestForGet(String url, Map<String, String> parames) {
		LocalTime startTime = LocalTime.now();
		logger.debug("외부통신 시작. : " + startTime);
		logger.debug("url : " + url);

		parames = parames == null ? new HashMap<String, String>() : parames;

		ResponseBody body = null;
		try {
			body = new QuickHttp()
					.url(url)
					.get()
					.addParames(parames)
					.body();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (body == null) {
			logger.debug("외부통신 결과데이터가 존재하지 않음.");
		} else {
			logger.debug("StateCode : " + body.getStateCode());
			logger.debug("Data : " + body.text());
		}

		LocalTime endTime = LocalTime.now();
		logger.debug("외부통신 종료 : " + endTime);

		Duration duration = Duration.between(startTime, endTime);

		long diffMillis = duration.toMillis();
		logger.info("외부통신 처리시간 : " + diffMillis + "ms");

		return body;
	}

	private static ResponseBody requestForPost(String url, Map<String, String> parames) {

		LocalTime startTime = LocalTime.now();
		logger.debug("외부통신 시작. : " + startTime);
		logger.debug("url : " + url);
		logger.debug("parames : " + parames.toString());

		parames = parames == null ? new HashMap<String, String>() : parames;

		ResponseBody body = null;
		try {
			body = (ResponseBody) new QuickHttp()
					.url(url)
					.post()
					.addParames(parames)
					.body();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (body == null) {
			logger.debug("외부통신 결과데이터가 존재하지 않음.");
		} else {
			logger.debug("StateCode : " + body.getStateCode());
			logger.debug("Data : " + body.text());
		}

		LocalTime endTime = LocalTime.now();
		logger.debug("외부통신 종료. : " + endTime);

		Duration duration = Duration.between(startTime, endTime);

		long diffMillis = duration.toMillis();
		logger.info("외부통신 처리시간 : " + diffMillis + "ms");

		return body;
	}

	public static ResponseBody requestForPostwithBodyContent(String url, String content) {

		LocalTime startTime = LocalTime.now();
		logger.debug("외부통신 시작. : " + startTime);
		logger.debug("- url : " + url);
		logger.debug("- bodyContent : " + content);

		ResponseBody body = null;
		try {
			body = (ResponseBody) new QuickHttp()
					.url(url)
					.post()
					.setContentType(ContentType.APPLICATION_JSON)
					.setBodyContent(content).body();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (body == null) {
			logger.debug("외부통신 결과데이터가 존재하지 않음.");
		} else {
			logger.debug("> StateCode : " + body.getStateCode());
			logger.debug("> Data : " + body.text());
		}

		LocalTime endTime = LocalTime.now();
		logger.debug("외부통신 종료. : " + endTime);

		Duration duration = Duration.between(startTime, endTime);
		
		long diffMillis = duration.toMillis();
		logger.info("외부통신 처리시간 : " + diffMillis + "ms");

		return body;
	}
	
	
	
	public void post(String strUrl, String jsonMessage) {
		
		HttpURLConnection connect = null;
		
		try {
			URL url = new URL(strUrl);
			connect = (HttpURLConnection) url.openConnection();
			connect.setConnectTimeout(5000); // 서버에 연결되는 Timeout 시간 설정
			connect.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
			
			connect.setRequestMethod("POST");

			// json으로 message를 전달하고자 할 때
			connect.setRequestProperty("Content-Type", "application/json");
			connect.setDoInput(true);
			connect.setDoOutput(true); // POST 데이터를 OutputStream으로 넘겨 주겠다는 설정
			connect.setUseCaches(false);
			connect.setDefaultUseCaches(false);

			
			OutputStreamWriter wr = new OutputStreamWriter(connect.getOutputStream());
			
			if(jsonMessage != null) {
				wr.write(jsonMessage); // json 형식의 message 전달
			}
			
			wr.flush();

			StringBuilder sb = new StringBuilder();
			if (connect.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// Stream을 처리해줘야 하는 귀찮음이 있음.
				BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream(), "utf-8"));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				br.close();
				System.out.println("" + sb.toString());
			} else {
				System.out.println(connect.getResponseMessage());
			}
		} catch (Exception e) {
			System.err.println(e.toString());
		} finally {
			if(connect != null) {
				connect.disconnect();
			}
		}
	}
}
