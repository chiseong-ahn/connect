package com.scglab.connect.services.company;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CompanyService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private CompanyDao companyDao;
	
	public List<Company> getCompanies(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Company> companies = this.companyDao.getCompanies(params);		
		return companies;
	}
	
	public Company getCompany(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Company company = this.companyDao.getCompany(params);	
		return company == null ? new Company() : company;
	}
	
}

