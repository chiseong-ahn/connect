package com.scglab.connect.services.adminmenu.blacklist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.auth.AuthService;
import com.scglab.connect.services.common.auth.User;
import com.scglab.connect.services.customer.Customer;
import com.scglab.connect.services.customer.CustomerDao;

@Service
public class BlacklistService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BlacklistDao blacklistDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private AuthService authService;
	
	public Map<String, Object> list(Map<String, Object> params, HttpServletRequest request) throws Exception {
		User user = this.authService.getUserInfo(request);
		params.put("cid", user.getCid());
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<Map<String, Object>> list = this.blacklistDao.selectAll(params);
		int count = list == null ? 0 : list.size();
		
		data.put("total", count);
		data.put("list", list);
		
		return data;
	}
	
	
	public Map<String, Object> update(Map<String, Object> params, HttpServletRequest request) throws Exception {
		User user = this.authService.getUserInfo(request);
		params.put("cid", user.getCid());
		params.put("emp", user.getEmp());
		
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.blacklistDao.update(params);
		if(result > 0) {
			Map<String, Object> customer = this.customerDao.findCustomer(params);
			data.put("customer", customer);
		}
		
		data.put("isSuccess", result > 0 ? true : false);
		return data;
	}
}
