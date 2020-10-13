package com.scglab.connect.services.auth;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "인증관리", description = "인증관리")
public class AuthController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="로그인", description = "아이디 비밀번호를 통해 로그인한다.")
	@Parameters({
		@Parameter(name = "cid", description = "", required = true, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "empno", description = "아이디", required = true, in = ParameterIn.QUERY, example = "csmaster1"),
		@Parameter(name = "passwd", description = "비밀번호", required = true, in = ParameterIn.QUERY, example = "1212")
	})
	public Map<String, Object> login(@Parameter(hidden = true) @RequestParam Map<String, Object> params) throws Exception {
		return this.authService.object(params);
	}
}
	