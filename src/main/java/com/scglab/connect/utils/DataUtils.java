package com.scglab.connect.utils;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	public static BigInteger getBigInteger(Map<String, Object> object, String key, BigInteger defaultValue) {
		if(object == null) {
			return defaultValue;
		}
		
		if(!object.containsKey(key)) {
			return defaultValue;
		}
		
		return (BigInteger)object.get(key);
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
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> convertMap(Object object){
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.convertValue(object, Map.class);
	}
	
	public static Object convertMapToObject(Map<String,Object> map,Object obj){
	    String keyAttribute = null;
	    String setMethodString = "set";
	    String methodString = null;
	    Iterator itr = map.keySet().iterator();

	    while(itr.hasNext()){
	        keyAttribute = (String) itr.next();
	        methodString = setMethodString+keyAttribute.substring(0,1).toUpperCase()+keyAttribute.substring(1);
	        Method[] methods = obj.getClass().getDeclaredMethods();
	        for(int i=0;i<methods.length;i++){
	            if(methodString.equals(methods[i].getName())){
	                try{
	                    methods[i].invoke(obj, map.get(keyAttribute));
	                }catch(Exception e){
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
	    return obj;
	}
	
	
	
	
}
