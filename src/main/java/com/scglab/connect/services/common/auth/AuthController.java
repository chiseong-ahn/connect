package com.scglab.connect.services.common.auth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotatios.Auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "인증관리", description = "인증관리")
public class AuthController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="토큰갱신", description = "refresh 토큰으로 access 토큰을 갱신한다.")
	@Parameters({
		@Parameter(name = "refreshToken", description = "", required = true, in = ParameterIn.QUERY, example = "")
	})
	public Map<String, Object> refreshToken(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
 		return this.authService.refreshToken(params, request, response);
	}
	
	@SuppressWarnings("unchecked")
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="인증 정보 조회", description = "인증 정보를 조회한다.", security = {@SecurityRequirement(name = "bearer-key")})
	public User user(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.authService.getUserInfo(request);
	}
}
	