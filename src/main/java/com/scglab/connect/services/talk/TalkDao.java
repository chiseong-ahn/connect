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
	
	public int join(Map<String, Object> params){
		String mapperId = getNamespace() + "join";
		return this.sqlSession.update(mapperId, params);
	}
	
	public int updateOnline(Map<String, Object> params){
		String mapperId = getNamespace() + "updateOnline";
		return this.sqlSession.update(mapperId, params);
	}
	
	public int minusNotread(Map<String, Object> params){
		String mapperId = getNamespace() + "minus-notread";
		return this.sqlSession.update(mapperId, params);
	}
	
	public int updateSpace(Map<String, Object> params) {
		String mapperId = getNamespace() + "updateSpace";
		return this.sqlSession.update(mapperId, params);
	}
	
	public int updateSpaceSpeaker(Map<String, Object> params) {
		String mapperId = getNamespace() + "updateSpaceSpeaker";
		return this.sqlSession.update(mapperId, params);
	}
	
	public int selectReadySpaceCount(Map<String, Object> params){
		String mapperId = getNamespace() + "selectReadySpaceCount";
		return this.sqlSession.selectOne(mapperId, params);
	}
	
	public Map<String, Object> selectReadySpace(Map<String, Object> params){
		String mapperId = getNamespace() + "selectReadySpace";
		return this.sqlSession.selectOne(mapperId, params);
	}
	
	
	
}