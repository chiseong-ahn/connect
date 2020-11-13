package com.scglab.connect.services.manual;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
@SuppressWarnings("unused")
public class ManualDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "api.manual.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	/**
	 * 
	 * @Method Name : findManualCount
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 매뉴얼 카운트 조회.
	 * @param params
	 * @return
	 */
	public int findManualCount(Map<String, Object> params){
		return this.sqlSession.selectOne(namespace + "findSearchCount", params);
	}
	
	/**
	 * 
	 * @Method Name : findManuals
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 매뉴얼 목록 조회.
	 * @param params
	 * @return
	 */
	public List<Manual> findManuals(Map<String, Object> params){
		return this.sqlSession.selectList(namespace + "findSearch", params);
	}
	
	/**
	 * 
	 * @Method Name : findManual
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 매뉴얼 상세 조회.
	 * @param params
	 * @return
	 */
	public Manual findManual(Map<String, Object> params){
		return this.sqlSession.selectOne(namespace + "findSearch", params);
	}
	
	/**
	 * 
	 * @Method Name : findTags
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 등록된 매뉴얼의 태그목록 조회.
	 * @param params
	 * @return
	 */
	public List<String> findTags(Map<String, Object> params){
		return this.sqlSession.selectList(namespace + "findTagAll", params);
	}
	
	/**
	 * 
	 * @Method Name : insertManual
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 매뉴얼 등록.
	 * @param params
	 * @return
	 */
	public int insertManual(Map<String, Object> params) {
		return this.sqlSession.insert(namespace + "insert", params);
	}
	
	/**
	 * 
	 * @Method Name : updateManual
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 매뉴얼 수정.
	 * @param params
	 * @return
	 */
	public int updateManual(Map<String, Object> params) {
		return this.sqlSession.update(namespace + "update", params);
	}
	
	/**
	 * 
	 * @Method Name : deleteManual
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 매뉴얼 삭제.
	 * @param params
	 * @return
	 */
	public int deleteManual(Map<String, Object> params) {
		return this.sqlSession.delete(namespace + "delete", params);
	}
	
	/**
	 * 
	 * @Method Name : findNextPageNumber
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 매뉴얼 채번(다음페이지 번호) 조회.
	 * @param params
	 * @return
	 */
	public Map<String, Object> findNextPageNumber(Map<String, Object> params){
		return this.sqlSession.selectOne(namespace + "findNextPageNumber", params);
	}
	
	
	
	
}