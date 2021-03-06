package com.scglab.connect.services.example;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class ExampleDao extends CommonDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public String namespace = "samples.";

	@Override
	protected String getNamespace() {
		return namespace;
	}

	public Map<String, Object> custom(Map<String, Object> params) {
		String mapperId = getNamespace() + "selectOne";
		return this.sqlSession.selectOne(mapperId, params);
	}
}