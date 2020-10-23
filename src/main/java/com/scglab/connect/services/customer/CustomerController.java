package com.scglab.connect.services.customer;

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

import com.scglab.connect.base.annotatios.Auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
@Tag(name = "고객관리", description = "고객관리")
public class CustomerController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="고객 토큰발급", description = "", security = {@SecurityRequirement(name = "bearer-key")})
    @Parameters({
    	@Parameter(name = "cid", description = "기관코드", required = true, in = ParameterIn.QUERY, example = "1"),
    	@Parameter(name = "userno", description = "회원번호", required = true, in = ParameterIn.QUERY, example = "3825"),
    	@Parameter(name = "name", description = "이름", required = true, in = ParameterIn.QUERY, example = "안치성"),
    	@Parameter(name = "telno", description = "휴대폰번호", required = true, in = ParameterIn.QUERY, example = "01022820317"),
    })
	public Map<String, Object> token(@Parameter(hidden = true) @RequestParam Map<String, Object> params) throws Exception {
		return this.customerService.token(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="고객 회원정보 수정", description = "", security = {@SecurityRequirement(name = "bearer-key")})
    public Map<String, Object> update(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(name = "id", description = "고객관리번호", required = true, in = ParameterIn.PATH, example = "") @PathVariable int id) throws Exception {
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
	
}
	