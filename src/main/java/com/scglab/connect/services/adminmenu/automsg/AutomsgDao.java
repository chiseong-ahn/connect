package com.scglab.connect.services.adminmenu.automsg;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;
import com.scglab.connect.services.customer.Customer;

@Repository
public class AutomsgDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "sdtalk.admin.automsg.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	public Automsg selectRandomOne(Map<String, Object> params){
		String mapperId = getNamespace() + "selectRandomOne";
		return this.sqlSession.selectOne(mapperId, params);
	}
}