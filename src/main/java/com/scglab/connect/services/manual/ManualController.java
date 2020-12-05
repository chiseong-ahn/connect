package com.scglab.connect.services.manual;

import java.util.List;
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
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manual")
@SuppressWarnings("unused")
@Tag(name = "매뉴얼 관리", description = "매뉴얼 API를 제공합니다.")
public class ManualController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private ManualService manualService;
	@Autowired private LoginService loginService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="매뉴얼 검색", description = "매뉴얼 목록을 검색합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "manualIndex", description = "매뉴얼 id", required = false, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "checkFavorite", description = "즐겨찾기여부", required = false, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "tag", description = "태그", required = false, in = ParameterIn.QUERY, example = "요금"),
		@Parameter(name = "searchValue", description = "검색어", required = false, in = ParameterIn.QUERY, example = "안녕"),
		@Parameter(name = "page", description = "페이지번호", required = false, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "pageSize", description = "페이지당 노출 수", required = false, in = ParameterIn.QUERY, example = "10"),
	})
	public Map<String, Object> manuals(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		return this.manualService.manuals(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="매뉴얼 상세", description = "매뉴얼 상세정보를 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Manual manual(@Parameter(description = "매뉴얼 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		params.put("id", id);
		Manual manual = this.manualService.manual(params, request, response);
		manual = manual == null ? new Manual() : manual;
		return manual;
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "tags", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="매뉴얼 태그 목록", description = "매뉴얼 태그 목록을 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)}) 
	@Parameters({
		@Parameter(name = "manualIndex", description = "매뉴얼 id", required = true, in = ParameterIn.QUERY, example = "1"),
	})
	public List<String> tags(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		return this.manualService.tags(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/favorite", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="매뉴얼 즐겨찾기 추가/삭제", description = "매뉴얼 즐겨찾기를 추가/삭제회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "value", description = "즐겨찾기 추가/삭제", required = true, in = ParameterIn.QUERY, example = "true"),
	})
	public Map<String, Object> favorite(@Parameter(description = "매뉴얼 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		params.put("id", id);
		return this.manualService.favorite(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="매뉴얼 등록", description = "매뉴얼을 등록합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "manualIndex", description = "매뉴얼 id", required = true, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "pageNo", description = "페이지번호", required = true, in = ParameterIn.QUERY, example = "질문"),
		@Parameter(name = "pageCode", description = "페이지코드", required = true, in = ParameterIn.QUERY, example = "코드"),
		@Parameter(name = "title", description = "제목", required = true, in = ParameterIn.QUERY, example = "제목"),
		@Parameter(name = "content", description = "내용", required = true, in = ParameterIn.QUERY, example = "내용"),
		@Parameter(name = "pdfImagePath", description = "이미지경로", required = true, in = ParameterIn.QUERY, example = "경로.jpg"),
	})
	public Manual regist(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		return this.manualService.regist(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="매뉴얼 수정", description = "매뉴얼을 수정합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "pageNo", description = "페이지번호", required = true, in = ParameterIn.QUERY, example = "질문"),
		@Parameter(name = "pageCode", description = "페이지코드", required = true, in = ParameterIn.QUERY, example = "코드"),
		@Parameter(name = "title", description = "제목", required = true, in = ParameterIn.QUERY, example = "제목"),
		@Parameter(name = "content", description = "내용", required = true, in = ParameterIn.QUERY, example = "내용"),
		@Parameter(name = "pdfImagePath", description = "이미지경로", required = true, in = ParameterIn.QUERY, example = "경로.jpg"),
	})
	public Manual update(@Parameter(description = "매뉴얼 index", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		params.put("id", id);
		return this.manualService.update(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="매뉴얼 삭제", description = "매뉴얼을 삭제합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> delete(@Parameter(description = "매뉴얼 id", example = "1") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		params.put("id", id);
		return this.manualService.delete(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/getNextPageNumber", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="매뉴얼 채번", description = "매뉴얼 채번을 조회합니다.", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> nextPageNumber(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		return this.manualService.nextPageNumber(params, request, response);
	}
	
	
}





	