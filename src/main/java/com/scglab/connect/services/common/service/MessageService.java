package com.scglab.connect.services.common.service;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

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
		
		if(locale == null) {
			locale = this.localResolver.resolveLocale(request);
		}
		
		if(parameters != null) {
			if(parameters.length > 0) {
				return this.messageSource.getMessage(code, parameters, locale);
			}
		}
		
		return this.messageSource.getMessage(code, null, locale);
	}
}
