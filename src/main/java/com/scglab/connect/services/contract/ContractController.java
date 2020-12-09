package com.scglab.connect.services.contract;

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
import com.scglab.connect.services.company.external.CompanyInc;
import com.scglab.connect.services.company.external.CompanyScg;
import com.scglab.connect.services.company.external.ICompany;
import com.scglab.connect.utils.DataUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/contract")
@Tag(name = "계약관리", description = "계약정보 관리")
public class ContractController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private ContractService contractService;
	
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="고객의 계약정보 목록", description = "고객의 계약정보 목록")
	public Map<String, Object> contracts(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.contractService.contracts(params, request, response);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/{useContractNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="고객의 계약상세정보", description = "고객의 계약상세정보")
	public Map<String, Object> contractInfo(@Parameter(description = "사용계약번호", example = "6000000486") @PathVariable String useContractNum, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("useContractNum", useContractNum);
		return this.contractService.contractInfo(params, request, response);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/{useContractNum}/bil", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="고객의 결제 상세정보", description = "고객의 결제 상세정보")
	@Parameters({
		@Parameter(name = "requestYm", description = "요청월", required = true, in = ParameterIn.QUERY, example = "202001"),
		@Parameter(name = "deadlineFlag", description = "납기구분", required = true, in = ParameterIn.QUERY, example = "20"),
	})
	public Map<String, Object> contractBil(@Parameter(description = "사용계약번호", example = "6000000486") @PathVariable String useContractNum, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("useContractNum", useContractNum);
		return this.contractService.contractBil(params, request, response);
	}
	
	
}
	