package com.scglab.connect.services.adminmenu.automsg;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotations.Auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/automsgs")
@Tag(name = "자동 메세지 관리", description = "관리자메뉴 > 자동 메세지 관리")
public class AutomsgController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	AutomsgService automsgService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="자동메세지 조회(목록)", description = "자동메세지 목록을 조회합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> list(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.automsgService.list(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="자동메세지 생성(등록)", description = "자동메세지을 등록(생성)합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
		@Parameter(name = "type", description = "메세지 유형(0-신규 대화 시작시 인사메세지, 1-상담사 배송지연 안내메세지, 2-답변지연 안내메세지", required = true, in = ParameterIn.QUERY, example = "0"),
		@Parameter(name = "msg", description = "메세지 내용", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public Map<String, Object> save(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.automsgService.save(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="자동메세지 정보 변경(수정)", description = "자동메세지 정보를 변경(수정)합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
		@Parameter(name = "type", description = "메세지 유형(0-신규 대화 시작시 인사메세지, 1-상담사 배송지연 안내메세지, 2-답변지연 안내메세지", required = true, in = ParameterIn.QUERY, example = "0"),
		@Parameter(name = "msg", description = "메세지 내용", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "id", description = "메세지번호", required = true, in = ParameterIn.QUERY, example = "67")
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public  Map<String, Object> update(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.automsgService.update(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="자동메세지 삭제", description = "자동메세지를 삭제합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	@Parameters({
		@Parameter(name = "id", description = "메세지번호", required = true, in = ParameterIn.QUERY, example = "67")
	})
	public Map<String, Object> delete(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.automsgService.delete(params, request);
	}
}

