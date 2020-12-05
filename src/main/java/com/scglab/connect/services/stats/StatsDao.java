package com.scglab.connect.services.stats;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class StatsDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "stats.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	public Map<String, Object> member(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "member", params);
	}
	
	public Map<String, Object> myToday(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "myToday", params);
	}
	
	public Map<String, Object> search(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "search", params);
	}
	
	public List<Map<String, Object>> customerAnalysis(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "customerAnalysis", params);
	}
	
	public List<Map<String, Object>> useHistory(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "useHistory", params);
	}
	
	public List<Map<String, Object>> hashtag(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "hashtag", params);
	}
	
}