package com.scglab.connect.base.exception;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.common.service.NotificationService;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.utils.DataUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private MessageHandler messageService;
	@Autowired private NotificationService notiService;
	@Autowired private LoginService loginService;
	@Autowired private HttpServletRequest request;
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<?> handleBaseException(Exception e) {
		
		HttpStatus httpStatus = null;
		this.logger.debug("Exception reason code : " + e.getMessage());
		String reason = this.messageService.getMessage(e.getMessage());	
		if(reason == null) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			ErrorResponse res = new ErrorResponse(httpStatus.value(), httpStatus.name(), reason, e.getStackTrace()[0]);
			e.printStackTrace();
			return new ResponseEntity<>(res, httpStatus);
		}
		
		if(e instanceof com.scglab.connect.base.exception.UnauthorizedException) {
			httpStatus = HttpStatus.UNAUTHORIZED;
		
		}else if(e instanceof RuntimeException) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			e.printStackTrace();
			
		}else if(e instanceof java.lang.NullPointerException) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			e.printStackTrace();
			
		}else if(e instanceof java.lang.ArithmeticException) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			
		} else if(e instanceof io.jsonwebtoken.SignatureException) {
			httpStatus = HttpStatus.UNAUTHORIZED;
			
		} else if(e instanceof io.jsonwebtoken.ExpiredJwtException) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			e.printStackTrace();
			
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			e.printStackTrace();
		}
		
		ErrorResponse res = new ErrorResponse(httpStatus.value(), httpStatus.name(), reason, e.getStackTrace()[0]);
		
		// 오류발생 알림처리.
		sendExceptionNotification(httpStatus, reason, e);
		
		
		return new ResponseEntity<>(res, httpStatus);
	}
	
	/**
	 * 
	 * @Method Name : sendExceptionNotification
	 * @작성일 : 2020. 12. 15.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 오류발생 알림처리 모듈.
	 * @param httpStatus
	 * @param reason
	 * @param e
	 */
	private void sendExceptionNotification(HttpStatus httpStatus, String reason, Exception e) {
		
		try {
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
			Date now = new Date();
			String datetime = format1.format(now);
			
			String accessToken = DataUtils.getSafeValue(this.request.getHeader("Authorization")).replaceAll("Bearer ", "");
			
			String name = "CSTALK-API [" + datetime + "]";
			this.notiService.webhookForSlack(name, "상담톡 오류 발생");
			this.notiService.webhookForSlack(name, "> Request : " + "[" + this.request.getMethod() + "] " + this.request.getRequestURI());
			this.notiService.webhookForSlack(name, "> accessToken : " + accessToken);
			this.notiService.webhookForSlack(name, "> Status : [" + httpStatus.value() + "] " + httpStatus.name());
			this.notiService.webhookForSlack(name, "> Reason : " + reason);
			this.notiService.webhookForSlack(name, "> Trace : " + e.getStackTrace()[0]);
		}catch(Exception e1) {}

	}
	
	@Getter
	@Setter
	@AllArgsConstructor
	@ToString
	private class ErrorResponse implements Serializable{
		private static final long serialVersionUID = 1L;
		
		private int status;
		private String name;
		private String message;
		private StackTraceElement trace;
			
	}
}



