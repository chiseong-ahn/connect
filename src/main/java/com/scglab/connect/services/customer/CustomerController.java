package com.scglab.connect.services.customer;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotations.Auth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(name = "고객관리", value="/api/customer")
public class CustomerController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CustomerService customerService;
	
	@Auth
	@RequestMapping(name="고급 검색", method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> findAll( @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.customerService.findAll(params);
	}
	
	@Auth
	@RequestMapping(name="관심고객 지정 / 해제", method = RequestMethod.PUT, value = "/{id}/block", produces = MediaType.APPLICATION_JSON_VALUE)
	public VCustomer block(@PathVariable int id, @RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.customerService.block(params, request);
	}
	
	@Auth
	@RequestMapping(name="관심고객 해제 : 초기화", method = RequestMethod.DELETE, value = "/{id}/block", produces = MediaType.APPLICATION_JSON_VALUE)
	public VCustomer unBlock(@PathVariable int id, @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.customerService.unBlock(params, request);
	}
	
	
	@RequestMapping(name="토근조회", method = RequestMethod.POST, value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> token(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.customerService.token(params);
	}
	
	
	@RequestMapping(name="", method = RequestMethod.POST, value = "/token2", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> token2(@RequestBody Customer customer) throws Exception {
		//return this.customerService.token(params);
		return null;
	}
	
	
	@Auth
	@RequestMapping(name="욕설/비속어 사용 카운트 증가", method = RequestMethod.PUT, value = "/{id}/swear", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> swear(@RequestBody Map<String, Object> params, @PathVariable int id, HttpServletRequest request) throws Exception {
		params.put("id", id);
		params.put("swear",  "Y");
		return this.customerService.plusSwearCount(params, request);
	}
	
	
	@Auth
	@RequestMapping(name="부적절한 대화 시도 카운트 증가", method = RequestMethod.PUT, value = "/{id}/insult", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> insult(@RequestBody Map<String, Object> params, @PathVariable int id, HttpServletRequest request) throws Exception {
		params.put("id", id);
		params.put("insult",  "Y");
		return this.customerService.plusInsultCount(params, request);
	}
	
	@Auth
	@RequestMapping(name="고객의 사용계약목록 조회", method = RequestMethod.GET, value = "/{userno}/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> contracts(@RequestParam Map<String, Object> params, @PathVariable String userno, HttpServletRequest request) throws Exception {
		params.put("userno", userno);
		return this.customerService.contracts(params, request);
	}
	
	@Auth
	@RequestMapping(name="고객의 사용계약 상세정보 조회", method = RequestMethod.GET, value = "/{userno}/contracts/{useContractNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> contractInfo(@RequestParam Map<String, Object> params, @PathVariable String userno, @PathVariable String useContractNum, HttpServletRequest request) throws Exception {
		params.put("userno", userno);
		params.put("useContractNum", useContractNum);
		return this.customerService.contractInfo(params, request);
	}
	
	@Auth
	@RequestMapping(name="고객의 사용계약 결제정보 조회", method = RequestMethod.GET, value = "/{userno}/contracts/{useContractNum}/bil", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> contractBilDetail(@RequestParam Map<String, Object> params, @PathVariable String userno, @PathVariable String useContractNum, HttpServletRequest request) throws Exception {
		params.put("userno", userno);
		params.put("useContractNum", useContractNum);
		return this.customerService.contractBilDetail(params, request);
	}
}
	