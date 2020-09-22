package com.scglab.connect.base.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Configuration
public class CommonInterceptor extends HandlerInterceptorAdapter {
	
	Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		int cid = 0;
		int appid = 0; // 요청서비스 구분(1-app, 2-admn)
		
		//Authorization: 8737577cab7eb664d5924f15792d5a91f568d5beb9a7f0e5589b003062ba35d653b0d0961ecbfa80c6b605270e45f07ccb77d26a94615afd32c9cf5d33346949058d53396c13b9fdc3777f3f244aa2
		String authorization = request.getHeader("Authorization");
		
		
		return true;
	}

	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
	}
	
	 
}





