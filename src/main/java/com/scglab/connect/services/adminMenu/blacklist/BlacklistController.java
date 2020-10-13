package com.scglab.connect.services.adminMenu.blacklist;

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
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/blacklist")
@Tag(name = "관심고객 관리", description = "관리자메뉴 > 관심고객 관리")
public class BlacklistController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	BlacklistService blacklistService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="관심고객 조회(목록)", description = "관심고객 목록을 조회합니다.")
	public Map<String, Object> list(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader String cid) throws Exception {
		params.put("cid", cid);
		return this.blacklistService.list(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="관심고객 등록/해제", description = "관심고객 등록 또는 해제합니다.")
	@Parameters({
		@Parameter(name = "type", description = "지정사유(0-해제, 1,2,3...-지정항목)", required = true, in = ParameterIn.QUERY, example = "0"),
		@Parameter(name = "msg", description = "추가메모", required = true, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "emp", description = "관리자번호", required = true, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "id", description = "고객번호", required = true, in = ParameterIn.QUERY, example = "148")
	})
	@ApiResponse(responseCode = "200", description = "RESULT:true-성공, RESULT:false-실패")
	public  Map<String, Object> update(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(description = "도시가스를 구분하는 기관코드(서울도시가스-1, 인천도시가스-2 ...)", required = true, in = ParameterIn.HEADER, example = "1") @RequestHeader int cid) throws Exception {
		params.put("cid", cid);
		return this.blacklistService.update(params);
	}
}


