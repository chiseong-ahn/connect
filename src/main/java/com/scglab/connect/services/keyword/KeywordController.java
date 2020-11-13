package com.scglab.connect.services.keyword;

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
@RequestMapping("/api/keyword")
@Tag(name = "키워드", description = "키워드 API")
public class KeywordController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	KeywordService keywordService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="키워드 전체 조회", description = "키워드를 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
	})
	public List<Keyword> findAll(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.keywordService.findAll(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/findByName", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="이름으로 키워드 조회", description = "키워드를 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "name", description = "키워드명", required = true, in = ParameterIn.QUERY, example = "좋은")
	})
	public Keyword findByName(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.keywordService.findByName(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/findByTemplateId", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="템플릿에 속한 키워드 조회", description = "키워드를 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "templateId", description = "답변템플릿 id", required = true, in = ParameterIn.QUERY, example = "1")
	})
	public List<Keyword> findByTemplateId(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.keywordService.findByTemplateId(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="키워드 등록", description = "키워드를 등록합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "name", description = "키워드명", required = true, in = ParameterIn.QUERY, example = "좋은")
	})
	public Keyword regist(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.keywordService.regist(params, request);
	}
	
}


