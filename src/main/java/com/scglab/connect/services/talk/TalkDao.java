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
	
	public Map<String, Object> today(Map<String, Object> params){
		String mapperId = getNamespace() + "today";
		return this.sqlSession.selectOne(mapperId, params);
	}
	
	public int state(Map<String, Object> params){
		String mapperId = getNamespace() + "state";
		return this.sqlSession.selectOne(mapperId, params);
	}
	
	public List<Map<String, Object>> spaces(Map<String, Object> params){
		String mapperId = getNamespace() + "spaces";
		return this.sqlSession.selectList(mapperId, params);
	}
	
	public Map<String, Object> space(Map<String, Object> params){
		String mapperId = getNamespace() + "space";
		return this.sqlSession.selectOne(mapperId, params);
	}
	
	public Map<String, Object> speaker(Map<String, Object> params){
		String mapperId = getNamespace() + "speaker";
		return this.sqlSession.selectOne(mapperId, params);
	}
	
	public List<Map<String, Object>> speaks(Map<String, Object> params){
		String mapperId = getNamespace() + "speaks";
		return this.sqlSession.selectList(mapperId, params);
	}
	
	public Map<String, Object> makeMessage(Map<String, Object> params){
		String mapperId = getNamespace() + "makeMessage";
		return this.sqlSession.selectOne(mapperId, params);
	}
	
	public List<Map<String, Object>> history(Map<String, Object> params){
		String mapperId = getNamespace() + "history";
		return this.sqlSession.selectList(mapperId, params);
	}
	
	public List<Map<String, Object>> historySpeaks(Map<String, Object> params){
		String mapperId = getNamespace() + "historySpeaks";
		return this.sqlSession.selectList(mapperId, params);
	}
	
	public void join(Map<String, Object> params){
		String mapperId = getNamespace() + "join";
		this.sqlSession.update(mapperId, params);
	}
	
	public void updateOnline(Map<String, Object> params){
		String mapperId = getNamespace() + "updateOnline";
		this.sqlSession.update(mapperId, params);
	}
	
	public void minusNotread(Map<String, Object> params){
		String mapperId = getNamespace() + "minus-notread";
		this.sqlSession.update(mapperId, params);
	}
	
	public void updateSpace(Map<String, Object> params) {
		String mapperId = getNamespace() + "updateSpace";
		this.sqlSession.update(mapperId, params);
	}
	
	public void updateSpaceSpeaker(Map<String, Object> params) {
		String mapperId = getNamespace() + "updateSpaceSpeaker";
		this.sqlSession.update(mapperId, params);
	}
	
}