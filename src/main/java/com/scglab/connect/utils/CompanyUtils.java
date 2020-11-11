package com.scglab.connect.utils;

import com.scglab.connect.services.company.external.CompanyInc;
import com.scglab.connect.services.company.external.CompanyScg;
import com.scglab.connect.services.company.external.ICompany;

public class CompanyUtils {
	
	public static ICompany getCompany(String companyId) {
		ICompany company = null;
		if(companyId.equals("1")) {
			company = new CompanyScg();
		}else if(companyId.equals("2")) {
			company = new CompanyInc();
		}
		
		return company;
	}
}
