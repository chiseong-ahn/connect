package com.scglab.connect.services.stats;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stats")
@Tag(name = "통계관리", description = "통계 API를 제공합니다.")
public class StatsController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private StatsService statsService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "member", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="통계 : 회원 전체 기준", description = "통계 : 회원 전체 기준", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> regist(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.statsService.member(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "myToday", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="통계 : 오늘의 나의 통계정보", description = "통계 : 오늘의 나의 통계정보", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> myToday(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.statsService.myToday(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "search", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="통계 : 기간 검색", description = "통계 : 기간 검색", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "startDate", description = "종료일 검색 시작일(YYYY-MM-DD)", required = true, in = ParameterIn.QUERY, example = "2020-11-01"),
		@Parameter(name = "endDate", description = "종료일 검색 종료일(YYYY-MM-DD)", required = true, in = ParameterIn.QUERY, example = "2020-11-01"),
	})
	public Map<String, Object> search(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.statsService.search(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "customer-analysis", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="통계 : 상담사별 분석", description = "통계 : 상담사별 분석", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "sort", description = "정렬 정보", required = true, in = ParameterIn.QUERY, example = "recentClose"),
	})
	public List<Map<String, Object>> customerAnalysis(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.statsService.customerAnalysis(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "use-history", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="통계 : 상담 사용 추이", description = "통계 : 상담 사용 추이", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "type", description = "검색 타입", required = true, in = ParameterIn.QUERY, example = "day"),
	})
	public List<Map<String, Object>> useHistory(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.statsService.useHistory(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "hashtag", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="통계 : 문의 유형별 통계", description = "통계 : 문의 유형별 통계", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "searchDate", description = "종료일 검색 시작일(YYYY-MM-DD)", required = true, in = ParameterIn.QUERY, example = "2020-11-01"),
	})
	public List<Map<String, Object>> hashtag(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.statsService.hashtag(params, request, response);
	}
	
	
}
	