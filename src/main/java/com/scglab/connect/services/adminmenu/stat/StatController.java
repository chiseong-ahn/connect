package com.scglab.connect.services.adminmenu.stat;

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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/stat")
@Tag(name = "현재 진행상황 및 기간별통계 관리", description = "관리자메뉴 > 현재 진행상황 및 기간별통계 관리")
public class StatController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	StatService statService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "current", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="현재 진행상황 조회", description = "현재 진행상황을 조회합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> stat1(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.statService.stat1(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "search", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="기간별 통계", description = "기간별 통계를 조회합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
		@Parameter(name = "startDate", description = "검색 시작일(YYYY-MM-DD)", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "endDate", description = "검색 종료일(YYYY-MM-DD)", required = true, in = ParameterIn.QUERY, example = ""),
	})
	public Map<String, Object> stat2(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.statService.stat2(params, request);
	}
	
}


