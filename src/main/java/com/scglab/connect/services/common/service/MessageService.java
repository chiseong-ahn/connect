package com.scglab.connect.services.common.service;

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
	
	public String getMessage(String code, Object[] parameter) {
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder .getRequestAttributes()).getRequest();
		
		if(parameter != null) {
			if(parameter.length > 0) {
				return this.messageSource.getMessage("main.greeting", parameter, this.localResolver.resolveLocale(request));
			}
		}
		return this.messageSource.getMessage("main.greeting", null, this.localResolver.resolveLocale(request));
	}
}
