package com.scglab.connect.services.template;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class TemplateDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "api.template.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	public List<Template> findAll(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findAll", params);
	}
	
	public Template findTemplate(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "findTemplate", params);
	}
	
	public int regist(Map<String, Object> params) {
		return this.sqlSession.insert(this.namespace + "regist", params);
	}
	
	public int update(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "update", params);
	}
	
	public int delete(Map<String, Object> params) {
		return this.sqlSession.delete(this.namespace + "delete", params);
	}
	
	public int insertFavorite(Map<String, Object> params) {
		return this.sqlSession.insert(this.getNamespace() + "insertFavorite", params);
	}
	
	public int deleteFavorite(Map<String, Object> params) {
		return this.sqlSession.delete(this.getNamespace() + "deleteFavorite", params);
	}
	
	public int insertTemplateKeyword(Map<String, Object> params) {
		return this.sqlSession.insert(this.getNamespace() + "insertTemplateKeyword", params);
	}
	
	public int deleteTemplateKeywords(Map<String, Object> params) {
		return this.sqlSession.delete(this.getNamespace() + "deleteTemplateKeywords", params);
	}
	
	public int insertKeyword(Map<String, Object> params) {
		return this.sqlSession.insert(this.getNamespace() + "insertKeyword", params);
	}
	
	public int selectCountKeyword(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.getNamespace() + "selectCountKeyword", params);
	}
	
	public Map<String, Object> selectKeyword(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.getNamespace() + "selectKeyword", params);
	}
	
}