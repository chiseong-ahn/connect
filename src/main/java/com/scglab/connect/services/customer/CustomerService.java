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
	
	/**
	 * 
	 * @Method Name : findAll
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객 검색.
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findAll(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		return data;
	}
	
	/**
	 * 
	 * @Method Name : block
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 관심고객 지정/해제
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> block(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		int result = this.customerDao.enableBlackStatus(params);
		if(result > 0) {
			data = this.customerDao.findCustomer(params);
		}
		
		return data;
	}
	
	/**
	 * 
	 * @Method Name : unBlock
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 관심고객 해제.
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> unBlock(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		int result = this.customerDao.disbleBlackStatus(params);
		if(result > 0) {
			data = this.customerDao.findCustomer(params);
		}
		
		return data;
	}
	
	/**
	 * 
	 * @Method Name : token
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 사용자 토큰 발급.
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> token(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		this.customerDao.insert(params);
		Customer customer = this.customerDao.findByGassappMemberNumber(params);
		this.logger.debug("customer : " + customer);
		
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("cid", customer.getCompanyId());
		claims.put("userno", customer.getGasappMemberNumber());
		claims.put("name", customer.getName());
		claims.put("space", customer.getRoomId());
		claims.put("speaker", customer.getSpeakerId());
		
		String accessToken = this.jwtService.generateToken(claims);
		this.logger.debug("accessToken : " + accessToken);
		
		data.put("accessToken", accessToken);
		
		return data;
	}
	
	/**
	 * 
	 * @Method Name : update
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 :
	 * @param params
	 * @return
	 * @throws Exception
	 */
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
