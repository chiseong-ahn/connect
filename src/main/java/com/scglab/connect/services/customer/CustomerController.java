package com.scglab.connect.services.customer;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customer")
@Tag(name = "고객관리", description = "고객 API를 제공합니다.")
public class CustomerController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CustomerService customerService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="고객 검색", description = "고객정보를 검색합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
    	@Parameter(name = "searchType", description = "검색 타입", required = true, in = ParameterIn.QUERY, example = "name"),
    	@Parameter(name = "searchValue", description = "검색 값", required = true, in = ParameterIn.QUERY, example = "안치성"),
    	@Parameter(name = "page", description = "페이지 번호", required = true, in = ParameterIn.QUERY, example = "1"),
    	@Parameter(name = "pageSize", description = "한 페이지에 보여줄 목록 갯수", required = true, in = ParameterIn.QUERY, example = "10"),
    })
	public Map<String, Object> findAll( @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.customerService.findAll(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/block", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="관심고객 지정 / 해제", description = "관심고객을 지정/해제합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
    	@Parameter(name = "blockType", description = "관심고객 지정 사유", required = true, in = ParameterIn.QUERY, example = ""),
    	@Parameter(name = "remark", description = "메모", required = false, in = ParameterIn.QUERY, example = ""),
    })
	public Map<String, Object> block(@Parameter(description = "고객id") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.customerService.block(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}/block", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="관심고객 지정 / 해제", description = "관심고객을 지정/해제합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
    	@Parameter(name = "blockType", description = "관심고객 지정 사유", required = true, in = ParameterIn.QUERY, example = ""),
    	@Parameter(name = "remark", description = "메모", required = false, in = ParameterIn.QUERY, example = ""),
    })
	public Map<String, Object> unBlock(@Parameter(description = "고객id") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.customerService.unBlock(params, request);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="고객 토큰발급", description = "", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
    	@Parameter(name = "companyId", description = "기관코드", required = true, in = ParameterIn.QUERY, example = "1"),
    	@Parameter(name = "gasappMemberNumber", description = "가스앱 회원번호", required = true, in = ParameterIn.QUERY, example = "3825"),
    	@Parameter(name = "name", description = "이름", required = true, in = ParameterIn.QUERY, example = "안치성"),
    	@Parameter(name = "telNumber", description = "휴대폰번호", required = true, in = ParameterIn.QUERY, example = "01022820317"),
    })
	public Map<String, Object> token( @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.customerService.token(params);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/token2", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="고객 토큰발급", description = "", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> token2(@RequestBody Customer customer) throws Exception {
		//return this.customerService.token(params);
		return null;
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="고객 회원정보 수정", description = "", security = {@SecurityRequirement(name = "bearer-key")})
    public Map<String, Object> update(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(name = "id", description = "고객관리번호", required = true, in = ParameterIn.PATH, example = "") @PathVariable int id, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.customerService.update(params);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/swear", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="욕설/비속어 사용 카운트 증가", description = "", security = {@SecurityRequirement(name = "bearer-key")})
    public Map<String, Object> swear(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(name = "id", description = "고객관리번호", required = true, in = ParameterIn.PATH, example = "") @PathVariable int id) throws Exception {
		params.put("id", id);
		params.put("swear",  "Y");
		return this.customerService.update(params);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/insult", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="부적절한 대화 시도 카운트 증가", description = "", security = {@SecurityRequirement(name = "bearer-key")})
    public Map<String, Object> insult(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(name = "id", description = "고객관리번호", required = true, in = ParameterIn.PATH, example = "") @PathVariable int id) throws Exception {
		params.put("id", id);
		params.put("insult",  "Y");
		return this.customerService.update(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/{userno}/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="고객의 사용계약목록 조회", description = "", security = {@SecurityRequirement(name = "bearer-key")})
    public List<Map<String, Object>> contracts(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(name = "userno", description = "가스앱 회원번호", required = true, in = ParameterIn.PATH, example = "3825") @PathVariable int userno, HttpServletRequest request) throws Exception {
		params.put("userno", userno);
		return this.customerService.contracts(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/{userno}/contracts/{useContractNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="사용계약번호 상세 정보", description = "", security = {@SecurityRequirement(name = "bearer-key")})
    public Map<String, Object> contractInfo(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(name = "userno", description = "가스앱 회원번호", required = true, in = ParameterIn.PATH, example = "3825") @PathVariable int userno, @Parameter(name = "useContractNum", description = "사용계약번호", required = true, in = ParameterIn.PATH, example = "6000000502") @PathVariable String useContractNum, HttpServletRequest request) throws Exception {
		params.put("userno", userno);
		params.put("useContractNum", useContractNum);
		return this.customerService.contractInfo(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/{userno}/contracts/{useContractNum}/bil", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="사용계약번호 결제 상세 정보", description = "", security = {@SecurityRequirement(name = "bearer-key")})
    public Map<String, Object> contractBilDetail(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(name = "userno", description = "가스앱 회원번호", required = true, in = ParameterIn.PATH, example = "3825") @PathVariable int userno, @Parameter(name = "useContractNum", description = "사용계약번호", required = true, in = ParameterIn.PATH, example = "6000000502") @PathVariable String useContractNum, HttpServletRequest request) throws Exception {
		params.put("userno", userno);
		params.put("useContractNum", useContractNum);
		return this.customerService.contractBilDetail(params, request);
	}
}
	