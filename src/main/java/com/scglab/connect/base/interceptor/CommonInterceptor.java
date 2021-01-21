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
import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.common.service.JwtService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;

@Configuration
public class CommonInterceptor extends HandlerInterceptorAdapter {
	
	Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);
	
	@Autowired private JwtService jwtService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
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
		
		// Header에서 인증토큰을 받아온다.
		String accessToken = getAccessToken(request);
		
		if(accessToken.equals("")) {
			if(auth != null) {
				// 인증되지 않은경우.
				throw new UnauthorizedException("auth.valid.fail.reason1");
			}
		}else {
			if(!this.jwtService.validateToken(accessToken)) {
				// 토큰이 유효하지 않거나 만료된 경우.
				
				if(auth != null) {
					throw new UnauthorizedException("auth.valid.fail.reason2");
				}
			}else {
				// 토큰이 유효한 경우.
				
				Map<String, Object> claims = this.jwtService.getJwtData(accessToken);
				if(claims == null) {
					throw new RuntimeException("claims is null");
				}
				this.logger.debug("claims : " + claims.toString());
				request.setAttribute("accessToken", accessToken);
				
				Member profile = new Member();
				profile = (Member) DataUtils.convertMapToObject(claims, profile);
				
				// token에서 추출한 Profile 정보를 request객체에 저장.
				request.setAttribute(Constant.AUTH_MEMBER, profile);
			}
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