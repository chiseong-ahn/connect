package com.scglab.connect.services.admin.category;

import java.util.Map;

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
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/categories")
@Tag(name = "답변템플릿 카테고리 관리", description = "관리자메뉴 > 답변템플릿 카테고리 관리")
public class CategoryController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CategoryService categoryService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/lg", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 대분류 카테고리 조회(목록)", description = "조건에 맞는 답변템플릿 대분류 카테고리 목록을 조회합니다.")
	public Map<String, Object> listLg(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader String cid) throws Exception {
		params.put("cid", cid);
		params.put("type", "lg");
		return this.categoryService.list(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/md", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 중분류 카테고리 조회(목록)", description = "조건에 맞는 답변템플릿 중분류 카테고리 목록을 조회합니다.")
	public Map<String, Object> listMd(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader String cid) throws Exception {
		params.put("cid", cid);
		params.put("type", "md");
		return this.categoryService.list(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/sm", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 소분류 카테고리 조회(목록)", description = "조건에 맞는 답변템플릿 소분류 카테고리 목록을 조회합니다.")
	public Map<String, Object> listSm(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader String cid) throws Exception {
		params.put("cid", cid);
		params.put("type", "sm");
		return this.categoryService.list(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "lg", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 대분류 카테고리 생성(등록)", description = "답변템플릿 대분류 카테고리를 등록(생성)합니다.")
	@Parameters({
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "emp", description = "관리자번호", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public Map<String, Object> saveLg(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader String cid) throws Exception {
		params.put("cid", cid);
		params.put("type", "lg");
		return this.categoryService.save(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/md", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 중분류 카테고리 생성(등록)", description = "답변템플릿 중분류 카테고리를 등록(생성)합니다.")
	@Parameters({
		@Parameter(name = "catelg", description = "대분류번호", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "emp", description = "관리자번호", required = true, in = ParameterIn.QUERY, example = "")
	})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public Map<String, Object> saveMd(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader String cid) throws Exception {
		params.put("cid", cid);
		params.put("type", "md");
		return this.categoryService.save(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/sm", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 소분류 카테고리 생성(등록)", description = "답변템플릿 소분류 카테고리를 등록(생성)합니다.")
	@Parameters({
		@Parameter(name = "catemd", description = "중분류번호", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "emp", description = "관리자번호", required = true, in = ParameterIn.QUERY, example = "")
	})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public Map<String, Object> saveSm(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader String cid) throws Exception {
		params.put("cid", cid);
		params.put("type", "sm");
		return this.categoryService.save(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/lg", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 대분류 카테고리 정보 변경(수정)", description = "답변템플릿 대분류 카테고리 정보를 변경(수정)합니다.")
	@Parameters({
		@Parameter(name = "id", description = "대분류번호", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "emp", description = "관리자번호", required = true, in = ParameterIn.QUERY, example = "")
	})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public  Map<String, Object> updateLg(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader String cid) throws Exception {
		params.put("cid", cid);
		params.put("type", "lg");
		return this.categoryService.update(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/md", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 중분류 카테고리 정보 변경(수정)", description = "답변템플릿 중분류 카테고리 정보를 변경(수정)합니다.")
	@Parameters({
		@Parameter(name = "id", description = "대분류번호", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "emp", description = "관리자번호", required = true, in = ParameterIn.QUERY, example = "")
	})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public  Map<String, Object> updateMd(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader String cid) throws Exception {
		params.put("cid", cid);
		params.put("type", "md");
		return this.categoryService.update(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/sm", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 소분류 카테고리 정보 변경(수정)", description = "답변템플릿 소분류 카테고리 정보를 변경(수정)합니다.")
	@Parameters({
		@Parameter(name = "cid", description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1"),
		@Parameter(name = "id", description = "대분류번호", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "name", description = "분류명", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "emp", description = "관리자번호", required = true, in = ParameterIn.QUERY, example = "")
	})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public  Map<String, Object> updateSm(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader String cid) throws Exception {
		params.put("cid", cid);
		params.put("type", "sm");
		return this.categoryService.update(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/lg", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 대분류 카테고리 정보 변경(수정)", description = "답변템플릿 소분류 카테고리 정보를 변경(수정)합니다.")
	@Parameters({
		@Parameter(name = "cid", description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1"),
		@Parameter(name = "id", description = "대분류번호", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public  Map<String, Object> deleteLg(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader String cid) throws Exception {
		params.put("cid", cid);
		params.put("type", "lg");
		return this.categoryService.delete(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/md", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 중분류 카테고리 정보 변경(수정)", description = "답변템플릿 소분류 카테고리 정보를 변경(수정)합니다.")
	@Parameters({
		@Parameter(name = "cid", description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1"),
		@Parameter(name = "id", description = "대분류번호", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public  Map<String, Object> deleteMd(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader String cid) throws Exception {
		params.put("cid", cid);
		params.put("type", "md");
		return this.categoryService.delete(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/sm", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="답변템플릿 소분류 카테고리 정보 변경(수정)", description = "답변템플릿 소분류 카테고리 정보를 변경(수정)합니다.")
	@Parameters({
		@Parameter(name = "cid", description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1"),
		@Parameter(name = "id", description = "대분류번호", required = true, in = ParameterIn.QUERY, example = ""),
	})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public  Map<String, Object> deleteSm(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader String cid) throws Exception {
		params.put("cid", cid);
		params.put("type", "sm");
		return this.categoryService.delete(params);
	}
}


