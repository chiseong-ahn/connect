package com.scglab.connect.services.customer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CustomerDao customerDao;
	
	public Map<String, Object> customer(Map<String, Object> params) throws Exception {
		Map<String, Object> customer = this.customerDao.customer(params);
		this.logger.debug("customer : " + customer.toString());
		return customer;
	}
}
