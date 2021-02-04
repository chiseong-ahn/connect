package com.scglab.connect.services.contract;

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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(name = "계약관리", value = "/api/contract")
public class ContractController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ContractService contractService;

	@Auth
	@RequestMapping(name = "고객의 계약정보 목록", method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> contracts(@RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return this.contractService.contracts(params, request, response);
	}

	@Auth
	@RequestMapping(name = "고객의 계약상세정보", method = RequestMethod.GET, value = "/{useContractNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> contractInfo(@PathVariable String useContractNum,
			@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		params.put("useContractNum", useContractNum);
		return this.contractService.contractInfo(params, request, response);
	}

	@Auth
	@RequestMapping(name = "고객의 결제 상세정보", method = RequestMethod.GET, value = "/{useContractNum}/bill", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> contractBil(@PathVariable String useContractNum,
			@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		params.put("useContractNum", useContractNum);
		return this.contractService.contractBil(params, request, response);
	}

}
