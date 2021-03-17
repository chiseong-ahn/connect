package com.scglab.connect.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpConnectionUtils {

	public int getForResponseCode(String strUrl) {

		return 0;
	}

	public int getForResponseCode(String strUrl, String jsonData) {

		return 0;
	}

	public String getForString(String strUrl) {
		return null;
	}

	public String getForString(String strUrl, String jsonData) {
		return null;
	}

	public Map<String, Object> getForMap(String strUrl) {
		return null;
	}

	public Map<String, Object> getForMap(String strUrl, String jsonData) {
		return null;
	}

	public List<Map<String, Object>> getForList(String strUrl) {
		return getForList(strUrl, null);
	}

	public List<Map<String, Object>> getForList(String strUrl, Map<String, String> params) {
		List<Map<String, Object>> responseData = null;
		HttpURLConnection connect = null;

		try {
			if (params != null) {
				String strParam = "";
				for (String key : params.keySet()) {
					String value = params.get(key);
					strParam += strParam.equals("") ? "?" : "&";
					strParam += key + "=" + value;
				}
				strUrl += strParam;
			}

			System.out.println("strUrl : " + strUrl);

			URL url = new URL(strUrl);
			connect = (HttpURLConnection) url.openConnection();
			connect.setConnectTimeout(5000); // 서버에 연결되는 Timeout 시간 설정
			connect.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
			connect.setRequestMethod("GET");

			StringBuilder sb = new StringBuilder();
			System.out.println("connect.getResponseCode() : " + connect.getResponseCode());
			if (connect.getResponseCode() == HttpURLConnection.HTTP_OK) {

				BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream()));

				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}

				// Response 데이터가 존재할 경우.
				if (sb.length() > 0) {
					responseData = new ObjectMapper().readValue(sb.toString(),
							new TypeReference<List<Map<String, Object>>>() {
							});
				}

				br.close();
			} else {
				System.out.println(connect.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (connect != null) {
				connect.disconnect();
				System.out.println("disconnected!");
			}
		}
		return responseData;
	}

	public int postForResponseCode(String strUrl) {

		return 0;
	}

	public int postForResponseCode(String strUrl, String jsonData) {

		return 0;
	}

	public String postForString(String strUrl) {
		return null;
	}

	public String postForString(String strUrl, String jsonData) {
		return null;
	}

	public Map<String, Object> postForMap(String strUrl) {
		return null;
	}

	public Map<String, Object> postForMap(String strUrl, String jsonData) {

		Map<String, Object> responseData = null;
		HttpURLConnection connect = null;

		try {
			URL url = new URL(strUrl);
			connect = (HttpURLConnection) url.openConnection();
			connect.setConnectTimeout(5000); // 서버에 연결되는 Timeout 시간 설정
			connect.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
			connect.setRequestMethod("POST");
			connect.setRequestProperty("Content-Type", "application/json");
			connect.setDoInput(true);
			connect.setDoOutput(true);
			connect.setUseCaches(false);
			connect.setDefaultUseCaches(false);

			OutputStreamWriter wr = new OutputStreamWriter(connect.getOutputStream());
			wr.write(jsonData);
			wr.flush();

			StringBuilder sb = new StringBuilder();
			if (connect.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream(), "utf-8"));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				System.out.println("ResponseData " + sb.toString());
				responseData = new ObjectMapper().readValue(sb.toString(), new TypeReference<Map<String, Object>>() {
				});

				br.close();
			} else {
				System.out.println(connect.getResponseMessage());
			}
		} catch (Exception e) {
			System.err.println(e.toString());
		} finally {
			if (connect != null) {
				connect.disconnect();
				System.out.println("disconnected!");
			}
		}

		return responseData;
	}

	public List<Map<String, Object>> postForList(String strUrl) {
		return null;
	}

	public List<Map<String, Object>> postForList(String strUrl, String jsonData) {
		List<Map<String, Object>> responseData = null;
		HttpURLConnection connect = null;

		try {
			URL url = new URL(strUrl);
			connect = (HttpURLConnection) url.openConnection();
			connect.setConnectTimeout(5000); // 서버에 연결되는 Timeout 시간 설정
			connect.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
			connect.setRequestMethod("POST");
			connect.setRequestProperty("Content-Type", "application/json");
			connect.setDoInput(true);
			connect.setDoOutput(true);
			connect.setUseCaches(false);
			connect.setDefaultUseCaches(false);

			OutputStreamWriter wr = new OutputStreamWriter(connect.getOutputStream());
			wr.write(jsonData);
			wr.flush();

			StringBuilder sb = new StringBuilder();
			if (connect.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream(), "utf-8"));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				if (sb.length() > 0) {
					responseData = new ObjectMapper().readValue(sb.toString(),
							new TypeReference<List<Map<String, Object>>>() {
							});
				}

				br.close();
			} else {
				System.out.println(connect.getResponseMessage());
			}
		} catch (Exception e) {
			System.err.println(e.toString());
		} finally {
			if (connect != null) {
				connect.disconnect();
			}
		}

		return responseData;
	}

	public void post(String strUrl, String jsonData) {

		HttpURLConnection connect = null;

		try {
			URL url = new URL(strUrl);
			connect = (HttpURLConnection) url.openConnection();
			connect.setConnectTimeout(5000); // 서버에 연결되는 Timeout 시간 설정
			connect.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
			connect.setRequestMethod("POST");
			connect.setRequestProperty("Content-Type", "application/json");
			connect.setDoInput(true);
			connect.setDoOutput(true);
			connect.setUseCaches(false);
			connect.setDefaultUseCaches(false);

			OutputStreamWriter wr = new OutputStreamWriter(connect.getOutputStream());
			wr.write(jsonData);
			wr.flush();

			StringBuilder sb = new StringBuilder();
			if (connect.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream(), "utf-8"));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				br.close();
			} else {
				System.out.println(connect.getResponseMessage());
			}
		} catch (Exception e) {
			System.err.println(e.toString());
		} finally {
			if (connect != null) {
				connect.disconnect();
			}
		}
	}

	public static void main(String[] args) throws JsonProcessingException {
		HttpConnectionUtils connectUtils = new HttpConnectionUtils();

		String url = "https://relay-scg-dev.gasapp.co.kr/api/cstalk/minwons";
		Map<String, String> params = new HashMap<String, String>();
		params.put("customerMobileId", "3716");
		params.put("useContractNum", "6004910783");
		params.put("reqName", "홍길동");
		params.put("classCode", "010202");
		params.put("transfer", "false");
		params.put("handphone", "010-6601-5106");
		params.put("memo", "민원테스트");
		params.put("employeeId", "csmaster1");
		params.put("chatId", "116");

		ObjectMapper mapper = new ObjectMapper();

		String jsonValue = mapper.writeValueAsString(params);

		// httpConnectionUtil.post(url, jsonValue);
		Map<String, Object> resultData = connectUtils.postForMap(url, jsonValue);
		System.out.println(resultData);

		url = "https://relay-scg-dev.gasapp.co.kr/api/employees";
		params = new HashMap<String, String>();
		params.put("comIds", "18");

		List<Map<String, Object>> resultList = connectUtils.getForList(url, params);
		System.out.println("resultList : " + resultList);
	}

}
