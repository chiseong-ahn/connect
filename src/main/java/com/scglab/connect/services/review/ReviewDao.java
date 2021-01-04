package com.scglab.connect.services.review;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class ReviewDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String namespace = "review.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	public int regist(Map<String, Object> params) {
		return this.sqlSession.insert(namespace + "regist", params);
	}
}