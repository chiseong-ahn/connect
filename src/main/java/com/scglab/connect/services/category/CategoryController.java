package com.scglab.connect.services.category;

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
@RequestMapping("/api/category")
@Tag(name = "답변템플릿 카테고리 관리", description = "답변템플릿 카테고리 API를 제공합니다.")
public class CategoryController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CategoryService categoryService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 대분류, 중분류, 소분류 카테고리 조회(목록)", description = "대분류, 중분류, 소분류 카테고리를 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> category(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.categoryService.total(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/large", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 대분류 카테고리 조회(목록)", description = "조건에 맞는 답변템플릿 대분류 카테고리 목록을 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> categoriesLarge(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "lg");
		return this.categoryService.categories(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/large/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 대분류 카테고리 상세조회", description = "조건에 맞는 답변템플릿 대분류 카테고리 상세정보를 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> categoryLarge(@Parameter(description = "대분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "lg");
		params.put("id", id);
		return this.categoryService.category(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/large", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 대분류 카테고리 생성(등록)", description = "답변템플릿 대분류 카테고리를 등록(생성)합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public Map<String, Object> saveLg(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "lg");
		return this.categoryService.save(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/large/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 대분류 카테고리 정보 변경(수정)", description = "답변템플릿 대분류 카테고리 정보를 변경(수정)합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "id", description = "대분류번호", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public  Map<String, Object> updateLg(@Parameter(description = "대분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "lg");
		params.put("id", id);
		return this.categoryService.update(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/large/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 대분류 카테고리 삭제", description = "답변템플릿 소분류 카테고리를 삭제합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "id", description = "대분류번호", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public  Map<String, Object> deleteLg(@Parameter(description = "대분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "lg");
		params.put("id", id);
		return this.categoryService.delete(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/middle", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 중분류 카테고리 조회(목록)", description = "조건에 맞는 답변템플릿 중분류 카테고리 목록을 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> categoriesMiddle(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "md");
		return this.categoryService.categories(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/middle/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 중분류 카테고리 조회(목록)", description = "조건에 맞는 답변템플릿 중분류 카테고리 목록을 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> categoryMiddle(@Parameter(description = "중분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "md");
		params.put("id", id);
		return this.categoryService.category(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/middle", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 중분류 카테고리 생성(등록)", description = "답변템플릿 중분류 카테고리를 등록(생성)합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "catelg", description = "대분류번호", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public Map<String, Object> saveMd(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "md");
		return this.categoryService.save(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/middle/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 중분류 카테고리 정보 변경(수정)", description = "답변템플릿 중분류 카테고리 정보를 변경(수정)합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public  Map<String, Object> updateMd(@Parameter(description = "중분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "md");
		params.put("id", id);
		return this.categoryService.update(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/middle/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 중분류 카테고리 삭제", description = "답변템플릿 중분류 카테고리를 삭제합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public  Map<String, Object> deleteMd(@Parameter(description = "중분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "md");
		params.put("id", id);
		return this.categoryService.delete(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/small", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 소분류 카테고리 조회(목록)", description = "조건에 맞는 답변템플릿 소분류 카테고리 목록을 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> categoriesSmall(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "sm");
		return this.categoryService.categories(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/small/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 소분류 카테고리 조회(목록)", description = "조건에 맞는 답변템플릿 소분류 카테고리 목록을 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> categorySmall(@Parameter(description = "소분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "sm");
		params.put("id", id);
		return this.categoryService.category(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/small", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 소분류 카테고리 생성(등록)", description = "답변템플릿 소분류 카테고리를 등록(생성)합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "catemd", description = "중분류번호", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public Map<String, Object> saveSm(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "sm");
		return this.categoryService.save(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/small/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 소분류 카테고리 정보 변경(수정)", description = "답변템플릿 소분류 카테고리 정보를 변경(수정)합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public  Map<String, Object> updateSm(@Parameter(description = "소분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "sm");
		params.put("id", id);
		return this.categoryService.update(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/small/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 소분류 카테고리 삭제", description = "답변템플릿 소분류 카테고리를 삭제합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public  Map<String, Object> deleteSm(@Parameter(description = "소분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "sm");
		params.put("id", id);
		return this.categoryService.delete(params, request);
	}
}


