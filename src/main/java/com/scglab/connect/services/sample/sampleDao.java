package com.scglab.connect.services.sample;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class sampleDao extends CommonDao {
	
	Logger logger = LoggerFactory.getLogger(sampleDao.class);
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "sdtalk.sample.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	public Map<String, Object> custom(Map<String, Object> params){
		String mapperId = getNamespace() + "selectOne";
		return sqlSession.selectOne(mapperId, params);
	}
}