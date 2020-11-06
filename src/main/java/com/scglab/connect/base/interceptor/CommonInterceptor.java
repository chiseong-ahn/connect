package com.scglab.connect.base.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.scglab.connect.base.annotations.Auth;
import com.scglab.connect.base.exception.UnauthorizedException;
import com.scglab.connect.services.common.auth.AuthService;
import com.scglab.connect.services.common.service.MessageService;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.JwtUtils;

@Configuration
public class CommonInterceptor extends HandlerInterceptorAdapter {
	
	Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);
	
	@Autowired
	private AuthService authService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		String uri = request.getRequestURI();
		if(uri.indexOf(".js") > -1 || uri.indexOf(".css") > -1 || uri.indexOf(".map") > -1) {
			return true;
		}
		
		return isAccess(request, handler);
	}
	
	private boolean isAccess(HttpServletRequest request, Object handler) {
		
		Auth auth = null;
		try {
			auth = ((HandlerMethod)handler).getMethodAnnotation(Auth.class);
		}catch(Exception e) {
			auth = null;
		}
		
		if(auth != null) {
			// 로그인이 되어야 있어야 진행되는 라우팅.
			
			String accessToken = getAccessToken(request);
			if(accessToken.equals("")) {
				// 인증토큰이 존재하지 않을경우.
				throw new UnauthorizedException("error.auth.type1");
			}
			
			JwtUtils jwtUtils = new JwtUtils();
			
			
			if(!jwtUtils.validateToken(accessToken)) {
				// 토큰이 유효하지 않거나 만료된 경우.
				throw new UnauthorizedException("error.auth.type2");
			}
			
			Map<String, Object> claims = jwtUtils.getJwtData(accessToken);
			request.setAttribute("accessToken", accessToken);
			// Request객체에 로그인 정보 저장.
			this.authService.setUserInfo(claims, request);
		}
		
		// 로그인과 무관하게 진행되는 라우팅일 경우 바이패스.
		return true;
		
	}
	
	// Header에서 인증토큰 추출.
	private String getAccessToken(HttpServletRequest request) {
		String accessToken = DataUtils.getSafeValue(request.getHeader("Authorization")).replaceAll("Bearer ", "");
		this.logger.debug("accessToken : " + accessToken);
		return accessToken;
	}
}





