package com.scglab.connect.base.exception;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.services.common.service.MessageService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final static String EXCEPTION_CODE_NULL_POINT = "A001";
	private final static String EXCEPTION_CODE_ARITHMETIC = "B001";
	private final static String EXCEPTION_CODE_NOT_FOUND = "C001";
	private final static String EXCEPTION_CODE_UNKNOWN = "Z001";
	
	@Autowired
	private MessageService messageService;
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<?> handleBaseException(Exception e) {
		
		HttpStatus httpStatus = null;
		String reason = this.messageService.getMessage(e.getMessage());	
		
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
		// 에러메일 발송 또는 Slack을 연동.
		
		return new ResponseEntity<>(res, httpStatus);
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



