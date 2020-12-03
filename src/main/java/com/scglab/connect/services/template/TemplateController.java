package com.scglab.connect.services.template;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/template")
@Tag(name = "답변템플릿", description = "답변템플릿 API")
public class TemplateController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	TemplateService templateService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 검색", description = "답변템플릿을 검색합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "checkFavorite", description = "즐겨찾기한 템플릿만 조회", required = false, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "checkMyAdd", description = "내가 등록한 템플릿만 조회", required = false, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "categoryLargeId", description = "대분류 id", required = false, in = ParameterIn.QUERY, example = "13"),
		@Parameter(name = "categoryMiddleId", description = "중분류 id", required = false, in = ParameterIn.QUERY, example = "13"),
		@Parameter(name = "categorySmallId", description = "소분류 id", required = false, in = ParameterIn.QUERY, example = "9"),
		@Parameter(name = "searchType", description = "검색 타입(ask-질문, reply-답변, keyword-키워드, memberName-작성자), ", required = false, in = ParameterIn.QUERY, example = "memberName"),
		@Parameter(name = "searchValue", description = "검색 값", required = false, in = ParameterIn.QUERY, example = "안녕"),
		@Parameter(name = "page", description = "페이지번호", required = false, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "pageSize", description = "조회할 게시물 수", required = false, in = ParameterIn.QUERY, example = "10"),
		
	})
	public Map<String, Object> list(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.templateService.findAll(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 상세정보 조회", description = "답변템플릿 상세정보 조회.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> findTemplate(@Parameter(description = "답변템플릿 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.templateService.findTemplate(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 등록", description = "답변템플릿을 등록합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "categorySmallId", description = "카테고리 소분류 id", required = true, in = ParameterIn.QUERY, example = "9"),
		@Parameter(name = "ask", description = "질문", required = true, in = ParameterIn.QUERY, example = "질문내용"),
		@Parameter(name = "reply", description = "답변", required = true, in = ParameterIn.QUERY, example = "답변내용"),
		@Parameter(name = "link", description = "링크 url(내부링크, 웹)", required = false, in = ParameterIn.QUERY, example = "scgmsc://bill"),
		@Parameter(name = "linkProtocol", description = "링크 프로토콜", required = false, in = ParameterIn.QUERY, example = "app"),
		@Parameter(name = "imagePath", description = "첨부 이미지 경로", required = false, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "imageName", description = "첨부 이미지명", required = false, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "isFavorite", description = "템플릿 즉시 즐겨찾기에 추가 여부", required = false, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "keywordIds", description = "키워드 id 목록", required = false, in = ParameterIn.QUERY, example = "[598,599]")
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public Map<String, Object> regist(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.templateService.create(params, request);
	}

	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 수정", description = "답변템플릿 수정합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "categorySmallId", description = "카테고리 소분류 id", required = true, in = ParameterIn.QUERY, example = "9"),
		@Parameter(name = "ask", description = "질문", required = true, in = ParameterIn.QUERY, example = "질문내용"),
		@Parameter(name = "reply", description = "답변", required = true, in = ParameterIn.QUERY, example = "답변내용"),
		@Parameter(name = "link", description = "링크 url(내부링크, 웹)", required = false, in = ParameterIn.QUERY, example = "scgmsc://bill"),
		@Parameter(name = "linkProtocol", description = "링크 프로토콜", required = false, in = ParameterIn.QUERY, example = "app"),
		@Parameter(name = "imagePath", description = "첨부 이미지 경로", required = false, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "imageName", description = "첨부 이미지명", required = false, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "isFavorite", description = "템플릿 즉시 즐겨찾기에 추가 여부", required = false, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "keywordIds", description = "키워드 id 목록", required = false, in = ParameterIn.QUERY, example = "[598,599]")
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public Template update(@Parameter(description = "답변템플릿 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.templateService.update(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 삭제", description = "답변템플릿를 삭제합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@ApiResponse(responseCode = "200", description = "success:true, success:result:false")
	public Map<String, Object> delete(@Parameter(description = "답변템플릿 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.templateService.delete(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/findAll", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 전체 조회", description = "답변템플릿 전체 조회", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "checkFavorite", description = "즐겨찾기한 템플릿만 조회", required = false, in = ParameterIn.QUERY, example = "1"),
	})
	public Map<String, Object> findAll(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.templateService.findAll(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/findByCategoryLargeId", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 : 카테고리 대분류", description = "답변템플릿 : 카테고리 대분류", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "checkFavorite", description = "즐겨찾기한 템플릿만 조회", required = false, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "categoryLargeId", description = "대분류 id", required = false, in = ParameterIn.QUERY, example = "13"),
	})
	public Map<String, Object> findByCategoryLargeId(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.templateService.findTemplate(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/findByCategoryMiddleId", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 : 카테고리 중분류", description = "답변템플릿 : 카테고리 중분류", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "checkFavorite", description = "즐겨찾기한 템플릿만 조회", required = false, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "categoryMiddleId", description = "중분류 id", required = false, in = ParameterIn.QUERY, example = "13"),
	})
	public Map<String, Object> findByCategoryMiddleId(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.templateService.findTemplate(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/findByCategorySmallId", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 : 카테고리 소분류", description = "답변템플릿 : 카테고리 소분류", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "checkFavorite", description = "즐겨찾기한 템플릿만 조회", required = false, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "categorySmallId", description = "소분류 id", required = false, in = ParameterIn.QUERY, example = "9"),
	})
	public Map<String, Object> findByCategorySmallId(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.templateService.findTemplate(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/findBtyFavoriteLoginMemberId", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="템플릿 조회 : 내가 즐겨찾기한 템플릿 목록", description = "템플릿 조회 : 내가 즐겨찾기한 템플릿 목록", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> findBtyFavoriteLoginMemberId(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("checkFavorite", 1);
		return this.templateService.findAll(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/favorite", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 즐겨찾기 추가/삭제", description = "답변템플릿 즐겨찾기 추가/삭제", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "value", description = "즐겨찾기 추가/삭제", required = true, in = ParameterIn.QUERY, example = "true")
	})
	@ApiResponse(responseCode = "200", description = "success:true-성공, success:false-실패")
	public Map<String, Object> saveFavorite(@Parameter(description = "답변템플릿 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.templateService.favorite(params, request);
	}
	
	
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "keyword", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="키워드 등록", description = "키워드를 등록합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "name", description = "키워드", required = false, in = ParameterIn.QUERY, example = "")
	})
	public Map<String, Object> saveKeyword(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.templateService.saveKeyword(params, request);
	}
	
}


