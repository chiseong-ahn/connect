package com.scglab.connect.services.customer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.service.JwtService;
import com.scglab.connect.services.common.service.MessageService;
import com.scglab.connect.utils.DataUtils;

@Service
public class CustomerService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private JwtService jwtService;
	
	public Map<String, Object> token(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		this.customerDao.insert(params);
		Customer customer = this.customerDao.findCustomer(params);
		this.logger.debug("customer : " + customer);
		
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("cid", customer.getCid());
		claims.put("userno", customer.getUserno());
		claims.put("name", customer.getName());
		claims.put("space", customer.getSpace());
		claims.put("speaker", customer.getSpeaker());
		
		String accessToken = this.jwtService.generateToken(claims);
		this.logger.debug("accessToken : " + accessToken);
		
		data.put("accessToken", accessToken);
		
		return data;
	}
	
	public Map<String, Object> update(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		int id = DataUtils.getInt(params, "id", 0);
		if(id == 0) {
			Object[] args = new String[1];
			args[0] = "id";
			throw new RuntimeException(this.messageService.getMessage("error.parameter1", args));
		}
		int count = this.customerDao.update(params);
		data.put("isSuccess", count > 0 ? true : false);
		
		return data;
	}
}
