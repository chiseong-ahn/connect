package com.scglab.connect.services.customer;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.service.MessageService;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.JwtUtils;

@Service
public class CustomerService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private CustomerDao customerDao;
	
	public Map<String, Object> token(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		this.customerDao.insert(params);
		Map<String, Object> customer = this.customerDao.selectOne(params);
		this.logger.debug("customer : " + customer);
		
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("cid", DataUtils.getInt(customer, "cid", 0));
		claims.put("userno", DataUtils.getString(customer, "userno", ""));
		claims.put("name", DataUtils.getString(customer, "name", ""));
		claims.put("space", DataUtils.getLong(customer, "space", 0));
		claims.put("speaker", DataUtils.getLong(customer, "speaker", 0));
		
		JwtUtils jwtUtils = new JwtUtils();
		String accessToken = jwtUtils.generateToken(claims);
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
