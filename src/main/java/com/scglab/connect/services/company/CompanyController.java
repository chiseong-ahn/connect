package com.scglab.connect.services.company;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/company")
@Tag(name = "회사 관리", description = "")
public class CompanyController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CompanyService companyService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="회사 목록 조회", description = "회사 목록을 조회한다.", security = {@SecurityRequirement(name = "bearer-key")})
	public List<Company> companys(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.companyService.getCompanies(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="회사 상세 조회", description = "회사 정보를 조회한다.", security = {@SecurityRequirement(name = "bearer-key")})
	public Company company(@Parameter(hidden = true) @RequestParam Map<String, Object> params,@Parameter(description = "회사 id") @PathVariable int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.companyService.getCompany(params, id, request, response);
	}
}