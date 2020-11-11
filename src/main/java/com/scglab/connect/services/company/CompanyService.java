package com.scglab.connect.services.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.service.MessageService;


@Service
public class CompanyService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private CompanyDao companyDao;
	
	public List<Company> getCompanies(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Company> companies = new ArrayList<Company>();
		
		Company c1 = new Company();
		c1.setId(1);
		c1.setName("서울도시가스");
		
		Company c2 = new Company();
		c2.setId(2);
		c2.setName("인천도시가스");
		
		companies.add(c1);
		companies.add(c2);
		
		return companies;
	}
	
	public Company getCompany(Map<String, Object> params, int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Company company = null;
		if(id > 0) {
			params.put("id", id);
			// todo : company = this.companyDao.findOne("api.company.findCompany", params);
			company = new Company();
			company.setId(id);
			company.setName(id == 1 ? "서울도시가스" : "인천도시가스");	
			
			if(company == null) {
				String message = this.messageService.getMessage("error.data.type2");
				throw new RuntimeException(message);
			}
		}else {
			Object[] errorParams = new String[1];
			errorParams[0] = "id";
			String message = this.messageService.getMessage("error.params.type4", errorParams);
			throw new RuntimeException(message);
		}
		return company;
	}
	
}

