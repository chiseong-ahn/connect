package com.scglab.connect.services.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.auth.AuthService;
import com.scglab.connect.services.company.external.CompanyInc;
import com.scglab.connect.services.company.external.CompanyScg;
import com.scglab.connect.services.company.external.ICompany;

@Service
public class CommonService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AuthService authService;
	
	public ICompany getCompany(String companyId) {
		ICompany company = null;
		
		switch(companyId) {
			case "1": 
				company = new CompanyScg();
				break;
			case "2":
				company = new CompanyInc();
				break;
		}
		
		return company;
	}
}
