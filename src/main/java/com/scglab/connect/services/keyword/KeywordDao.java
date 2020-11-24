package com.scglab.connect.services.keyword;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class KeywordDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "keyword.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	/**
	 *
	 * @Method Name : findCount
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 키워드 카운트 조회
	 * @param params
	 * @return
	 */
	public int findCount(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "findCount", params);
	}
	
	/**
	 * 
	 * @Method Name : findAll
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 키워드 조회
	 * @param params
	 * @return
	 */
	public List<Keyword> findAll(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findAll", params);
	}
	
	/**
	 * 
	 * @Method Name : getByTemplateId
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 템플릿에 속한 키워드 조회
	 * @param params
	 * @return
	 */
	public List<Keyword> getByTemplateId(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "getByTemplateId", params);
	}
	
	/**
	 * 
	 * @Method Name : getDetail
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 키워드 상세 조회
	 * @param params
	 * @return
	 */
	public Keyword getDetail(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "getDetail", params);
	}
	
	/**
	 * 
	 * @Method Name : getByName
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 이름으로 키워드 검색
	 * @param params
	 * @return
	 */
	public Keyword getByName(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "getByName", params);
	}
	
	/**
	 * 
	 * @Method Name : insertKeyword
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 키워드 등록
	 * @param params
	 * @return
	 */
	public int insertKeyword(Map<String, Object> params){
		return this.sqlSession.insert(this.namespace + "create", params);
	}
}