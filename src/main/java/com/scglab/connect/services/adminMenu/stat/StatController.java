package com.scglab.connect.services.adminMenu.stat;

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
	@Operation(summary="현재 진행상황 조회", description = "현재 진행상황을 조회합니다.")
	public Map<String, Object> stat1(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader String cid) throws Exception {
		params.put("cid", cid);
		return this.statService.stat1(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "search", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="기간별 통계", description = "기간별 통계를 조회합니다.")
	@Parameters({
		@Parameter(name = "startDate", description = "검색 시작일(YYYY-MM-DD)", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "endDate", description = "검색 종료일(YYYY-MM-DD)", required = true, in = ParameterIn.QUERY, example = ""),
	})
	public Map<String, Object> stat2(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader String cid) throws Exception {
		params.put("cid", cid);
		return this.statService.stat2(params);
	}
	
}


