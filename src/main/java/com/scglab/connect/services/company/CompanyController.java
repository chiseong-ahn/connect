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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(name = "회사 관리", value = "/api/company")
public class CompanyController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CompanyService companyService;

	@RequestMapping(name = "회사 목록 조회", method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Company> companys(@RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return this.companyService.getCompanies(params, request, response);
	}

	@RequestMapping(name = "회사 상세 조회", method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Company company(@PathVariable int id, @RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.companyService.getCompany(params, request, response);
	}
}