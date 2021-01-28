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
	 * XML의 매핑되는 prefix namespace ex. sdtalk.sample.selectList => sdtalk.sample
	 */
	public String namespace = "template.";

	@Override
	protected String getNamespace() {
		return namespace;
	}

	/**
	 * 
	 * @Method Name : findAllCount
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 템플릿 전체 카운트 조회.
	 * @param params
	 * @return
	 */
	public int findAllCount(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "findAllCount", params);
	}

	/**
	 * 
	 * @Method Name : search
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 템플릿 검색.
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) {
		return this.sqlSession.selectList(this.namespace + "search", params);
	}

	/**
	 * 
	 * @Method Name : findAll
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 템플릿 전체조회.
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findAll(Map<String, Object> params) {
		return this.sqlSession.selectList(this.namespace + "findAll", params);
	}

	/**
	 * 
	 * @Method Name : getDetail
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 템플릿 상세조회.
	 * @param params
	 * @return
	 */
	public Map<String, Object> getDetail(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "getDetail", params);
	}

	/**
	 * 
	 * @Method Name : create
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 템플릿 등록.
	 * @param params
	 * @return
	 */
	public int create(Map<String, Object> params) {
		return this.sqlSession.insert(this.namespace + "create", params);
	}

	/**
	 * 
	 * @Method Name : update
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 템플릿 수정.
	 * @param params
	 * @return
	 */
	public int update(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "update", params);
	}

	/**
	 * 
	 * @Method Name : delete
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 템플릿 삭제.
	 * @param params
	 * @return
	 */
	public int delete(Map<String, Object> params) {
		return this.sqlSession.delete(this.namespace + "delete", params);
	}

	/**
	 * 
	 * @Method Name : insertFavorite
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 즐겨찾기 등록.
	 * @param params
	 * @return
	 */
	public int insertFavorite(Map<String, Object> params) {
		return this.sqlSession.insert(this.getNamespace() + "createFavorite", params);
	}

	/**
	 * 
	 * @Method Name : deleteFavorite
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 즐겨찾기 해제
	 * @param params
	 * @return
	 */
	public int deleteFavorite(Map<String, Object> params) {
		return this.sqlSession.delete(this.getNamespace() + "deleteFavorite", params);
	}

	/**
	 * 
	 * @Method Name : insertTemplateKeyword
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 템플릿에 키워드 연결
	 * @param params
	 * @return
	 */
	public int insertTemplateKeyword(Map<String, Object> params) {
		return this.sqlSession.insert(this.getNamespace() + "insertTemplateKeyword", params);
	}

	/**
	 * 
	 * @Method Name : deleteTemplateKeywords
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 템플릿에 연결된 키워드 삭제.
	 * @param params
	 * @return
	 */
	public int deleteTemplateKeywords(Map<String, Object> params) {
		return this.sqlSession.delete(this.getNamespace() + "deleteTemplateKeywords", params);
	}

	/**
	 * 
	 * @Method Name : insertKeyword
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 키워드 등록.
	 * @param params
	 * @return
	 */
	public int insertKeyword(Map<String, Object> params) {
		return this.sqlSession.insert(this.getNamespace() + "insertKeyword", params);
	}

	/**
	 * 
	 * @Method Name : selectCountKeyword
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 키워드 카운트 조회.
	 * @param params
	 * @return
	 */
	public int selectCountKeyword(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.getNamespace() + "selectCountKeyword", params);
	}

	/**
	 * 
	 * @Method Name : selectKeyword
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 키워드 조회.
	 * @param params
	 * @return
	 */
	public Map<String, Object> selectKeyword(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.getNamespace() + "selectKeyword", params);
	}

}