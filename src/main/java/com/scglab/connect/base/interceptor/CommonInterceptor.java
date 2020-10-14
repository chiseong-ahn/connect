package com.scglab.connect.base.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.scglab.connect.base.annotatios.Auth;
import com.scglab.connect.base.exception.UnauthorizedException;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.JwtUtils;

@Configuration
public class CommonInterceptor extends HandlerInterceptorAdapter {
	
	Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return isAccess(request, handler);
	}

	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
	}
	
	private boolean isAccess(HttpServletRequest request, Object handler) {
		
		Auth auth = null;
		try {
			auth = ((HandlerMethod)handler).getMethodAnnotation(Auth.class);
		}catch(Exception e) {}
		
		if(auth != null) {
			// 로그인이 되어야 있어야 진행되는 라우팅.
			
			String accessToken = getAccessToken(request);
			if(accessToken.equals("")) {
				// 인증토큰이 존재하지 않을경우.
				throw new UnauthorizedException("AccessToken is nothing!");
			}
			
			JwtUtils jwtUtils = new JwtUtils();
			Map<String, Object> claims = jwtUtils.getJwtData(accessToken);
			
			if(claims == null) {
				// 토큰이 유효하지 않거나 만료된 경우.
				throw new UnauthorizedException("Invalid credentials");
			}
			
			request.setAttribute("users", claims);
			this.logger.debug("claims : " + claims.toString());
						
		}
		
		// 로그인과 무관하게 진행되는 라우팅일 경우 바이패스.
		return true;
		
	}
	
	private String getAccessToken(HttpServletRequest request) {
		String accessToken = DataUtils.getSafeValue(request.getHeader("Authorization")).replaceAll("Bearer ", "");
		this.logger.debug("accessToken : " + accessToken);
		return accessToken;
	}
}





