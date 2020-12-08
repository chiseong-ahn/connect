package com.scglab.connect.services.category;

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
@RequestMapping("/api/category")
@Tag(name = "카테고리 관리", description = "카테고리 API를 제공합니다.")
public class CategoryController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CategoryService categoryService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="카테고리 전체 조회", description = "카테고리 전체를 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> total(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.categoryService.total(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "tree", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="대분류, 중분류, 소분류 카테고리 조회(목록)", description = "대분류, 중분류, 소분류 카테고리를 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public List<CategoryLarge> tree(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.categoryService.tree(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/large", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="대분류 카테고리 조회(목록)", description = "조건에 맞는 대분류 카테고리 목록을 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public List<CategoryLarge> categoriesLarge(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "large");
		return this.categoryService.categoryLargeList(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/large/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="대분류 카테고리 상세조회", description = "조건에 맞는 대분류 카테고리 상세정보를 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public CategoryLarge categoryLarge(@Parameter(description = "대분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "large");
		params.put("id", id);
		return this.categoryService.categoryLarge(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/large", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="대분류 카테고리 생성(등록)", description = "대분류 카테고리를 등록(생성)합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public CategoryLarge saveLg(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.categoryService.saveCategoryLarge(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/large/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="대분류 카테고리 정보 변경(수정)", description = "대분류 카테고리 정보를 변경(수정)합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "id", description = "대분류번호", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public  CategoryLarge updateLg(@Parameter(description = "대분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "large");
		params.put("id", id);
		return this.categoryService.updateCategoryLarge(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/large/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="대분류 카테고리 삭제", description = "소분류 카테고리를 삭제합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "id", description = "대분류번호", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public  Map<String, Object> deleteLg(@Parameter(description = "대분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "large");
		params.put("id", id);
		return this.categoryService.delete(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/middle", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="중분류 카테고리 조회(목록)", description = "조건에 맞는 중분류 카테고리 목록을 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public List<CategoryMiddle> categoriesMiddle(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "middle");
		return this.categoryService.categoryMiddleList(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/middle/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="중분류 카테고리 조회(목록)", description = "조건에 맞는 중분류 카테고리 목록을 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public CategoryMiddle categoryMiddle(@Parameter(description = "중분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.categoryMiddle(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/middle", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="중분류 카테고리 생성(등록)", description = "중분류 카테고리를 등록(생성)합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "categoryLargeId", description = "대분류번호", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public CategoryMiddle saveMd(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.categoryService.saveCategoryMiddle(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/middle/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="중분류 카테고리 정보 변경(수정)", description = "중분류 카테고리 정보를 변경(수정)합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public  CategoryMiddle updateMd(@Parameter(description = "중분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.updateCategoryMiddle(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/middle/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="중분류 카테고리 삭제", description = "중분류 카테고리를 삭제합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public  Map<String, Object> deleteMd(@Parameter(description = "중분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "middle");
		params.put("id", id);
		return this.categoryService.delete(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/small", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="소분류 카테고리 조회(목록)", description = "조건에 맞는 소분류 카테고리 목록을 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public List<CategorySmall> categoriesSmall(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "small");
		return this.categoryService.categorySmallList(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/small/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="소분류 카테고리 조회(목록)", description = "조건에 맞는 소분류 카테고리 목록을 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public CategorySmall categorySmall(@Parameter(description = "소분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.categorySmall(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/small", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="소분류 카테고리 생성(등록)", description = "소분류 카테고리를 등록(생성)합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "categoryMiddleId", description = "중분류번호", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public CategorySmall saveSm(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.categoryService.saveCategorySmall(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/small/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="소분류 카테고리 정보 변경(수정)", description = "소분류 카테고리 정보를 변경(수정)합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "result:true-성공, result:false-실패")
	public  CategorySmall updateSm(@Parameter(description = "소분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.updateCategorySmall(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/small/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="소분류 카테고리 삭제", description = "소분류 카테고리를 삭제합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public  Map<String, Object> deleteSm(@Parameter(description = "소분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "small");
		params.put("id", id);
		return this.categoryService.delete(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/large/{id}/sort-index", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="대분류 카테고리 정렬 정보 수정.", description = "대분류 카테고리 정렬 정보 수정합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "sortIndex", description = "정렬순번", required = true, in = ParameterIn.QUERY, example = ""),
	})
	public CategoryLarge sortIndexLg(@Parameter(description = "대분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.updateSortIndexCategoryLarge(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/middle/{id}/sort-index", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="중분류 카테고리 정렬 정보 수정.", description = "중분류 카테고리 정렬 정보 수정합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "sortIndex", description = "정렬순번", required = true, in = ParameterIn.QUERY, example = ""),
	})
	public CategoryMiddle sortIndexMd(@Parameter(description = "중분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.updateSortIndexCategoryMiddle(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/small/{id}/sort-index", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="소분류 카테고리 정렬 정보 수정.", description = "소분류 카테고리 정렬 정보 수정합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "sortIndex", description = "정렬순번", required = true, in = ParameterIn.QUERY, example = ""),
	})
	public CategorySmall sortIndexSm(@Parameter(description = "소분류 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.updateSortIndexCategorySmall(params, request);
	}
}


