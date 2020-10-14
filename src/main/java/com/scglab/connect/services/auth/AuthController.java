package com.scglab.connect.services.auth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotatios.Auth;
import com.scglab.connect.services.chat.JwtTokenProvider;

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
	
	private final JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="로그인", description = "아이디 비밀번호를 통해 로그인한다.")
	@Parameters({
		@Parameter(name = "cid", description = "", required = true, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "empno", description = "아이디", required = true, in = ParameterIn.QUERY, example = "csmaster1"),
		@Parameter(name = "passwd", description = "비밀번호", required = true, in = ParameterIn.QUERY, example = "1212")
	})
	public Map<String, Object> login(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.authService.login(params, request);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="로그인", description = "아이디 비밀번호를 통해 로그아웃한다.")
	public Map<String, Object> logout(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.authService.logout(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="인증 정보 조회", description = "인증 정보를 조회한다.", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> user(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader int cid, HttpServletRequest request) throws Exception {
		return (Map<String, Object>)request.getAttribute("users");
	}
}
	