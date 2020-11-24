package com.scglab.connect.services.company.external;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.common.service.PushService;


@Service
public class ExternalService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private MessageHandler messageService;
	@Autowired private PushService pushService;
	
	public Map<String, Object> sendPush(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response){
		
		int userno = 3825;
		String message = "푸시발송 테스트";
		this.pushService.sendPush(userno, message);
		
		return null;
	}
	
}

