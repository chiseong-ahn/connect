package com.scglab.connect.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class MessageUtils {
	
	@Autowired 
	private static MessageSource messageSource;
	
	public static String getMessage(String code, Locale locale) {
		return messageSource.getMessage(code, null, locale);
	}
}
