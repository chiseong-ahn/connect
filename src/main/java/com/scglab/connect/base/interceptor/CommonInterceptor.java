package com.scglab.connect.base.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.scglab.connect.base.annotatios.Auth;
import com.scglab.connect.services.common.properties.PathProperties;

@Configuration
public class CommonInterceptor extends HandlerInterceptorAdapter {
	
	Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);
	
	@Autowired
	private PathProperties pathProperty;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		this.logger.debug("Path perperties : " + this.pathProperty.toString());
		return isAccess(request, handler);
	}

	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
	}
	
	private boolean isAccess(HttpServletRequest request, Object handler) {
		boolean result = true;
		
		Auth auth = ((HandlerMethod)handler).getMethodAnnotation(Auth.class);
		this.logger.info("auth : " + auth);
		if(auth != null) {
			// 로그인이 되어야 있어야 진행되는 라우팅.
			
			

			
		}else {
			// 로그인과 무관하게 진행되는 라우팅.
		}
		 
		return result;
	}
	
	private String getToken(HttpServletRequest request) {
		
		String tokenString = request.getHeader("Authorization");
		if(tokenString != null) {
			
		}
		
		return "";
	}
}





