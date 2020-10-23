package com.scglab.connect.services.sample;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class SampleDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "samples.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	public Map<String, Object> custom(Map<String, Object> params){
		String mapperId = getNamespace() + "selectOne";
		return this.sqlSession.selectOne(mapperId, params);
	}
}