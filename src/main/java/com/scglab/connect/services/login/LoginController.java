package com.scglab.connect.services.login;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.services.member.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(name = "인증 관리", value = "/auth")
public class LoginController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping(name="멤버 로그인",method = RequestMethod.POST, value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> loginMember(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.loginService.loginMember(params, request, response);
	}
	
	@RequestMapping(name="멤버 인증 정보 조회",method = RequestMethod.GET, value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public Member profile(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.profile(params, request, response);
		return member == null ? new Member() : member;
	}
	
	@RequestMapping(name="고객 로그인",method = RequestMethod.POST, value = "/loginCustomer", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> loginCustomer(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.loginService.loginCustomer(params);
	}
}
	