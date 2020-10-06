package com.scglab.connect.services.customer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class CustomerDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "sdtalk.customer.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	public Map<String, Object> customer(Map<String, Object> params){
		String mapperId = getNamespace() + "info";
		return this.sqlSession.selectOne(mapperId, params);
	}
}