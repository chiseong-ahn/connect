package com.scglab.connect.services.wise;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class WiseDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String namespace = "wise.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	public Wise findWise(Map<String, Object> params) {
		return this.sqlSession.selectOne(namespace + "findWise", params);
	}
}