package com.scglab.connect.services.member;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotations.Auth;
import com.scglab.connect.constant.Constant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
@Tag(name = "회원관리", description = "회원관리")
public class MemberController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MemberService memberService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="회원목록 조회", description = "회원 목록 조회", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> members(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.memberService.members(params, id, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="회원 등록", description = "회원 등록", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> regist(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.memberService.regist(params, id, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="회원 수정", description = "회원 수정", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> update(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.memberService.update(params, id, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/state", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="회원상태 수정", description = "회원 상담상태 수정", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> updateState(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.memberService.state(params, id, request, response);
	}
	
	
}
	