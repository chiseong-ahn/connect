package com.scglab.connect.services.template;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/templates")
@Tag(name = "답변템플릿", description = "답변템플릿 API")
public class TemplateController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	TemplateService templateService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="전체 답변템플릿 조회", description = "전체 답변템플릿을 조회합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
		@Parameter(name = "type", description = "답변템플릿 유형(0-전체 답변템플릿, 1-나의 답변템플릿, 2-즐겨찾기 답변템플릿)", required = true, in = ParameterIn.QUERY, example = "0"),
		@Parameter(name = "catelg", description = "대분류 코드", required = false, in = ParameterIn.QUERY, example = "13"),
		@Parameter(name = "catemd", description = "중분류 코드", required = false, in = ParameterIn.QUERY, example = "13"),
		@Parameter(name = "catesm", description = "소분류 코드", required = false, in = ParameterIn.QUERY, example = "9"),
		@Parameter(name = "keyfield", description = "검색항목(ask-질문, reply-답변, keyword-키워드, empname-작성자), ", required = false, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "keyword", description = "검색어", required = false, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "pageSize", description = "조회할 게시물 수", required = false, in = ParameterIn.QUERY, example = "15"),
		@Parameter(name = "page", description = "페이지번호", required = false, in = ParameterIn.QUERY, example = "1")
	})
	public Map<String, Object> mytemplate(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.templateService.list(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "keyword", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="키워드 등록", description = "키워드를 등록합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
		@Parameter(name = "name", description = "키워드", required = false, in = ParameterIn.QUERY, example = "")
	})
	public Map<String, Object> saveKeyword(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.templateService.saveKeyword(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 등록", description = "답변템플릿을 등록합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
		@Parameter(name = "emp", description = "관리자번호", required = true, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "catesm", description = "소분류 코드", required = true, in = ParameterIn.QUERY, example = "9"),
		@Parameter(name = "ask", description = "고객질문 메세지", required = true, in = ParameterIn.QUERY, example = "질문내용"),
		@Parameter(name = "reply", description = "상담사 답변 메세지", required = true, in = ParameterIn.QUERY, example = "답변내용"),
		@Parameter(name = "link", description = "링크주소", required = false, in = ParameterIn.QUERY, example = "scgmsc://simple"),
		@Parameter(name = "img", description = "첨부 이미지주소", required = false, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "keywords", description = "키워드코드", required = false, in = ParameterIn.QUERY, example = "598,599")
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public Map<String, Object> save(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.templateService.save(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 정보 변경(수정)", description = "답변템플릿 정보를 변경(수정)합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
		@Parameter(name = "emp", description = "관리자번호", required = true, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "catesm", description = "소분류 코드", required = true, in = ParameterIn.QUERY, example = "9"),
		@Parameter(name = "ask", description = "고객질문 메세지", required = true, in = ParameterIn.QUERY, example = "질문내용(수정)"),
		@Parameter(name = "reply", description = "상담사 답변 메세지", required = true, in = ParameterIn.QUERY, example = "답변내용(수정)"),
		@Parameter(name = "link", description = "링크주소", required = false, in = ParameterIn.QUERY, example = "scgmsc://simple"),
		@Parameter(name = "img", description = "첨부 이미지주소", required = false, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "keywords", description = "키워드코드", required = false, in = ParameterIn.QUERY, example = "598,599"),
		@Parameter(name = "id", description = "템플릿 관리번호", required = false, in = ParameterIn.QUERY, example = "642")
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public  Map<String, Object> update(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.templateService.update(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 삭제", description = "답변템플릿를 삭제합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	@Parameters({
		@Parameter(name = "id", description = "템플릿 관리번호", required = true, in = ParameterIn.QUERY, example = "")
	})
	public Map<String, Object> delete(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.templateService.delete(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/favorite", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿-즐겨찾기 등록", description = "답변템플릿을 즐겨찾기에 등록 합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
		@Parameter(name = "emp", description = "관리자 관리번호", required = true, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "template", description = "템플릿 관리번호", required = true, in = ParameterIn.QUERY, example = "634")
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public Map<String, Object> saveFavorite(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("isFavorite", true);
		return this.templateService.favorite(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/favorite", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿-즐겨찾기 해제", description = "답변템플릿을 즐겨찾기에서 해제 합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
		@Parameter(name = "emp", description = "관리자 관리번호", required = true, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "template", description = "템플릿 관리번호", required = true, in = ParameterIn.QUERY, example = "634")
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public Map<String, Object> cancelFavorite(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("isFavorite", false);
		return this.templateService.favorite(params, request);
	}
}


