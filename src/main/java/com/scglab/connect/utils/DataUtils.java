package com.scglab.connect.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class DataUtils {
	
	public static String getParameter(HttpServletRequest request, String name, String defaultValue) {
		
		if(request != null) {
			return StringUtils.defaultString(request.getParameter(name), defaultValue);
		}
		return defaultValue;
	}
	
	public static String getAttribute(HttpServletRequest request, String name, String defaultValue) {
		
		if(request != null) {
			return StringUtils.defaultString((String)request.getAttribute(name), defaultValue);
		}
		return defaultValue;
	}
	
	public static String getObjectValue(Map<String, Object> object, String name, String defaultValue) {
		
		if(object != null) {
			if(object.containsKey(name)) {
				return StringUtils.defaultString((String)object.get(name), defaultValue);
			}
		}
		return defaultValue;
	}
	
	public static int getInt(Map<String, Object> object, String key, int defaultValue) {
		if(object == null) {
			return defaultValue;
		}
		
		if(!object.containsKey(key)) {
			return defaultValue;
		}
		
		return (int)object.get(key);
	}
	
	public static long getLong(Map<String, Object> object, String key, long defaultValue) {
		if(object == null) {
			return defaultValue;
		}
		
		if(!object.containsKey(key)) {
			return defaultValue;
		}
		
		return (long)object.get(key);
	}
	
	public static String getString(Map<String, Object> object, String name, String defaultValue) {
		
		if(object != null) {
			if(object.containsKey(name)) {
				return StringUtils.defaultString((String)object.get(name), defaultValue);
			}
		}
		return defaultValue.trim();
	}
	
	
	
	public static String getSafeValue(String value) {
		return getSafeValue(value, "");
	}
	
	public static String getSafeValue(String value, String defaultValue) {
		if(value == null) {
			return defaultValue;
		}
		
		if(value.equals("")) {
			return defaultValue;
		}
		
		return value.trim();
	}
	
	
	
	
}
