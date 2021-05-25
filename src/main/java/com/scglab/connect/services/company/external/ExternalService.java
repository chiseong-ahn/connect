package com.scglab.connect.services.company.external;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTrace.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.service.ApiService;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.common.service.PushService;

@Service
public class ExternalService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageHandler messageService;
	@Autowired
	private PushService pushService;
	
	@Autowired
	private ApiService<Response> apiService;

	public Map<String, Object> sendPush(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {

		int userno = 3825;
		String message = "푸시발송 테스트";
		this.pushService.sendPush(userno, message);

		return null;
	}
	
	//
	public void getMembers() throws Exception {
		String url = "https://relay-scg-dev.gasapp.co.kr/api/employees?comIds=18";
		ResponseEntity datas = this.apiService.get(url, null);
		System.out.println("datas.getStatusCodeValue() : " + datas.getStatusCodeValue());
		if(datas.getStatusCodeValue() == HttpURLConnection.HTTP_OK) {
			System.out.println("result : " + datas.getBody());
		}else {
			
		}
		
		url = "https://relay-scg-dev.gasapp.co.kr/api/cstalk/minwons";
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
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		ResponseEntity datas2 = this.apiService.post(url, headers, params);
		
		System.out.println("datas.getStatusCodeValue() : " + datas2.getStatusCodeValue());
		if(datas2.getStatusCodeValue() == HttpURLConnection.HTTP_OK) {
			System.out.println("result2 : " + datas2.getBody());
		}else {
			
		}
	}
}
