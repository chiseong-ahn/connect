package com.scglab.connect.services.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.auth.AuthService;
import com.scglab.connect.services.company.Company;
import com.scglab.connect.services.company.CompanyInc;
import com.scglab.connect.services.company.CompanyScg;

@Service
public class CommonService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AuthService authService;
	
	public Company getCompany(int cid) {
		Company company = null;
		
		switch(cid) {
			case 1: 
				company = new CompanyScg();
				break;
			case 2:
				company = new CompanyInc();
				break;
		}
		
		return company;
	}
}
