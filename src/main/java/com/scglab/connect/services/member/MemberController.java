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
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
@Tag(name = "멤버관리", description = "멤버관리")
public class MemberController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MemberService memberService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="멤버 검색", description = "멤버 검색 : 이름, 사번", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
    	@Parameter(name = "searchType", description = "검색 타입", required = true, in = ParameterIn.QUERY, example = "name"),
    	@Parameter(name = "searchValue", description = "검색 값", required = true, in = ParameterIn.QUERY, example = "서울"),
    	@Parameter(name = "page", description = "페이지 번호", required = true, in = ParameterIn.QUERY, example = "1"),
    	@Parameter(name = "pageSize", description = "한 페이지에 보여줄 목록 갯수", required = true, in = ParameterIn.QUERY, example = "10"),
    })
	public Map<String, Object> members(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.memberService.members(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="멤버 수정 : 권한, 상태", description = "멤버 수정 : 권한, 상태", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
    	@Parameter(name = "authLevel", description = "권한레벨", required = true, in = ParameterIn.QUERY, example = "2"),
    	@Parameter(name = "state", description = "상태", required = true, in = ParameterIn.QUERY, example = "0"),
    })
	public Member update(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.memberService.update(params, id, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/state", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="멤버 수정 : 상태", description = "멤버 수정 : 상태", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
    	@Parameter(name = "state", description = "상태", required = true, in = ParameterIn.QUERY, example = "0"),
    })
	public Map<String, Object> updateState(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.memberService.state(params, id, request, response);
	}
	
	
}
	