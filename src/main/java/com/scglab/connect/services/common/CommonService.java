package com.scglab.connect.services.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.company.external.CompanyInc;
import com.scglab.connect.services.company.external.CompanyScg;
import com.scglab.connect.services.company.external.ICompany;

@Service
public class CommonService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private CompanyScg companyScg;
	@Autowired private CompanyInc companyInc;
	
	public ICompany getCompany(String companyId) {
		return companyId.equals("1") ? this.companyScg : this.companyInc;
	}
}
