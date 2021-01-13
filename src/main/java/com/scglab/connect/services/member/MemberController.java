package com.scglab.connect.services.member;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotations.Auth;

@RestController
@RequestMapping(name = "멤버관리", value="/api/member")
public class MemberController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MemberService memberService;
	
	@Auth
	@RequestMapping(name="멤버 검색 : 이름, 사번", method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> members(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.memberService.members(params, request, response);
	}
	
	@Auth
	@RequestMapping(name="멤버 검색 : 이름, 사번", method = RequestMethod.GET, value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> member(@RequestParam Map<String, Object> params, @PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.memberService.member(params, request, response);
	}
	
	@Auth
	@RequestMapping(name="멤버 수정 : 권한, 상태", method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Member update(@RequestBody Map<String, Object> params, @PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.memberService.update(params, id, request, response);
	}
	
	@Auth
	@RequestMapping(name="멤버 수정 : 상태",method = RequestMethod.PUT, value = "/{id}/state", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> updateState(@RequestBody Map<String, Object> params, @PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.memberService.state(params, id, request, response);
	}
	
	
}
	