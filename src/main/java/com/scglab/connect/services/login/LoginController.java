package com.scglab.connect.services.login;

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

import com.scglab.connect.base.annotations.Auth;
import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.member.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Tag(name = "인증관리", description = "로그인 관리")
public class LoginController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="로그인", description = "아이디 비밀번호를 통해 로그인한다.")
	@Parameters({
		@Parameter(name = "companyId", description = "회사 id", required = true, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "loginName", description = "로그인 id", required = true, in = ParameterIn.QUERY, example = "csmaster1"),
		@Parameter(name = "password", description = "비밀번호", required = true, in = ParameterIn.QUERY, example = "1212"),
		@Parameter(name = "name", description = "이름(개발용)", required = true, in = ParameterIn.QUERY, example = "서울도시가스")
	})
	@ApiResponse(responseCode = "200", description = "OK", content = {
		@Content(schema = @Schema(oneOf = Member.class)),
	})
	public Map<String, Object> login(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.loginService.login(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="인증 정보 조회", description = "인증 정보를 조회한다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@ApiResponse(responseCode = "200", description = "OK", content = {
		@Content(schema = @Schema(oneOf = Member.class)),
	})
	
	public Map<String, Object> profile(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.loginService.profile(params, request, response);
	}
}
	