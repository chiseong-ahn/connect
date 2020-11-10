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
			// 정적리소스에 대해서는 별도 처리를 진행하지 않는다.
			return true;
		}
		
		// 접근권한 리턴.
		return isAccess(request, handler);
	}
	
	/**
	 * 
	 * @Method Name : isAccess
	 * @작성일 : 2020. 11. 10.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 접근권한 체크
	 * @param request
	 * @param handler
	 * @return
	 */
	private boolean isAccess(HttpServletRequest request, Object handler) {
		
		Auth auth = null;
		try {
			// 요청을 담당하는 컨트롤러에 인증 어노테이션(@Auth)이 적용되어있는지 체크. 
			auth = ((HandlerMethod)handler).getMethodAnnotation(Auth.class);
		}catch(Exception e) {
			auth = null;
		}
		
		// 인증이 필요한 경우.
		if(auth != null) {
			
			// Header에서 인증토큰을 받아온다.
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
		
		return true;
		
	}
	
	// Header에서 인증토큰 추출.
	private String getAccessToken(HttpServletRequest request) {
		String accessToken = DataUtils.getSafeValue(request.getHeader("Authorization")).replaceAll("Bearer ", "");
		this.logger.debug("accessToken : " + accessToken);
		return accessToken;
	}
}





