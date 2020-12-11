package com.scglab.connect.services.common;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.company.external.CompanyInc;
import com.scglab.connect.services.company.external.CompanyScg;
import com.scglab.connect.services.company.external.ICompany;
import com.scglab.connect.utils.DataUtils;

@Service
public class CommonService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private CompanyScg companyScg;
	@Autowired private CompanyInc companyInc;
	
	public ICompany getCompany(String companyId) {
		return companyId.equals("1") ? this.companyScg : this.companyInc;
	}
	
	public String appendText(String str, String text) {
		if(str == null) str = "";
		if(text == null) text = "";
		
		str += str.equals("") ? text : "," + text;
		return str;
	}
	
	public String validString(Map<String, Object> parameters, String[] names) {
		String str = "";
		for(String name : names) {
			if(!validString(parameters, name)) {
				str += str.equals("") ? name : "," + name;
			}
		}
		
		return str;
	}
	
	public boolean validString(Map<String, Object> parameters, String name) {
		try {
			if(!DataUtils.getString(parameters, name, "").equals("")) {
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean validInteger(Map<String, Object> parameters, String name) {
		try {
			if(DataUtils.getInt(parameters, name, 0) != 0) {
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public boolean validLong(Map<String, Object> parameters, String name) {
		try {
			if(DataUtils.getLong(parameters, name, 0) != 0) {
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
}
