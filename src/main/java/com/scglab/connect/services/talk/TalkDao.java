package com.scglab.connect.services.talk;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class TalkDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "sdtalk.talk.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	public List<Map<String, Object>> spaces(Map<String, Object> params){
		String mapperId = getNamespace() + "spaces";
		return this.sqlSession.selectList(mapperId, params);
	}
	
	public Map<String, Object> speaker(Map<String, Object> params){
		String mapperId = getNamespace() + "speaker";
		return this.sqlSession.selectOne(mapperId, params);
	}
	
	public List<Map<String, Object>> speaks(Map<String, Object> params){
		String mapperId = getNamespace() + "speaks";
		return this.sqlSession.selectList(mapperId, params);
	}
	
	public List<Map<String, Object>> history(Map<String, Object> params){
		String mapperId = getNamespace() + "history";
		return this.sqlSession.selectList(mapperId, params);
	}
	
	public List<Map<String, Object>> historySpeaks(Map<String, Object> params){
		String mapperId = getNamespace() + "historySpeaks";
		return this.sqlSession.selectList(mapperId, params);
	}
	
	
}