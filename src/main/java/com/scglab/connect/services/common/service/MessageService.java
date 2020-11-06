package com.scglab.connect.services.common.service;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 
 * @FileName : MessageService.java
 * @Project : connect
 * @Date : 2020. 9. 24. 
 * @작성자 : anchiseong
 * @변경이력 :
 * @프로그램 설명 : 요청된 Request에 맞는 메세지 반환 서비스.
 
 */
@Service
public class MessageService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private LocaleResolver localResolver;
	
	public String getMessage(String code) {
		Locale locale;
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder .getRequestAttributes()).getRequest();
			locale = this.localResolver.resolveLocale(request);
		}catch(Exception e) {
			locale = Locale.getDefault();
		}
		
		Object[] parameters = null;
				
		return getMessage(code, parameters, locale);
	}
	
	public String getMessage(String code, Locale locale) {
		return getMessage(code, null, locale);
	}
	
	public String getMessage(String code, Object[] parameters) {
		Locale locale;
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder .getRequestAttributes()).getRequest();
			locale = this.localResolver.resolveLocale(request);
		}catch(Exception e) {
			locale = Locale.getDefault();
		}
		
		// 메세지 반환.
		return getMessage(code, parameters, locale);
	}
	
	public String getMessage(String code, Object[] parameters, Locale locale) {
		
		this.logger.debug("Message [" + code + ", " + locale);
		
		// 파라미터 확인.
		if(parameters != null) {
			if(parameters.length > 0) {
				// 파라미터 매핑된 메세지 반환.
				return this.messageSource.getMessage(code, parameters, locale);
			}
		}
		
		// 메세지 반환.
		return this.messageSource.getMessage(code, null, locale);
	}
}
