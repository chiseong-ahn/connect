package com.scglab.connect.services.adminMenu.emp;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotatios.Auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/emps")
@Tag(name = "계정관리", description = "관리자메뉴 > 계정관리")
public class EmpController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	EmpService empService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="계정 조회(목록)", description = "조건에 맞는 계정 목록을 조회합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> list(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.empService.list(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="계정 조회(상세)", description = "조건에 맞는 계정 상세정보를 조회합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> object(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, @Parameter(name = "계정번호", description = "상담톡시스템에 등록된 계정 관리번호", required = true, example = "67") @PathVariable String id) throws Exception {
		params.put("id", id);
		return this.empService.object(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="계정 생성(등록)", description = "계정을 등록(생성)합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
		@Parameter(name = "auth", description = "시스템을 사용할 수 있는 권한 (0~9)", required = true, in = ParameterIn.QUERY, example = "9"),
		@Parameter(name = "speaker", description = "상담자 고유번호", required = true, in = ParameterIn.QUERY, example = "66"),
		@Parameter(name = "profileimg", description = "프로필 이미지 업로드 번호", required = false, in = ParameterIn.QUERY),
		@Parameter(name = "state", description = "상담상태 (0~9)", required = true, in = ParameterIn.QUERY, example = "9"),
		@Parameter(name = "empno", description = "아이디", required = true, in = ParameterIn.QUERY, example = "csahn")
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public Map<String, Object> save(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.empService.save(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="계정 정보 변경(수정)", description = "계정 정보를 변경(수정)합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
		@Parameter(name = "auth", description = "시스템을 사용할 수 있는 권한 (0~9)", required = true, in = ParameterIn.QUERY, example = "9"),
		@Parameter(name = "speaker", description = "상담자 고유번호", required = true, in = ParameterIn.QUERY, example = "66"),
		@Parameter(name = "profileimg", description = "프로필 이미지 업로드 번호", required = false, in = ParameterIn.QUERY),
		@Parameter(name = "state", description = "상담상태 (0~9)", required = true, in = ParameterIn.QUERY, example = "9"),
		@Parameter(name = "empno", description = "아이디", required = true, in = ParameterIn.QUERY, example = "csahn"),
		@Parameter(name = "id", description = "계정관리번호", required = true, in = ParameterIn.QUERY, example = "67")
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public  Map<String, Object> update(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.empService.update(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="계정 삭제", description = "계정을 삭제합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public Map<String, Object> delete(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.empService.delete(params, request);
	}
}


