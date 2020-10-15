package com.scglab.connect.services.common.message;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

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
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private LocaleResolver localResolver;
	
	public String getMessage(String code) {
		return getMessage(code, null);
	}
	
	public String getMessage(String code, Object[] parameters) {
		return getMessage(code, parameters, null);
	}
	
	public String getMessage(String code, Object[] parameters, Locale locale) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder .getRequestAttributes()).getRequest();
		
		// 언어 정의.
		locale = locale == null ? this.localResolver.resolveLocale(request) : locale;
		
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
