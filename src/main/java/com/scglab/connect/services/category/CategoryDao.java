package com.scglab.connect.services.category;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class CategoryDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "api.category.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	/**
	 * 
	 * @Method Name : findCategoryLarge
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 대분류 카테고리 조회
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findCategoryLarge(Map<String, Object> params){
		return this.sqlSession.selectList(namespace + "findCategoryLarge", params);
	}
	
	/**
	 * 
	 * @Method Name : getCategoryLarge
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 대분류 카테고리 상세조회
	 * @param params
	 * @return
	 */
	public Map<String, Object> getCategoryLarge(Map<String, Object> params){
		return this.sqlSession.selectOne(namespace + "getCategoryLarge", params);
	}
	
	/**
	 * 
	 * @Method Name : createCategoryLarge
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 대분류 카테고리 등록
	 * @param params
	 * @return
	 */
	public int createCategoryLarge(Map<String, Object> params){
		return this.sqlSession.insert(namespace + "createCategoryLarge", params);
	}
	
	/**
	 * 
	 * @Method Name : updateCategoryLarge
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 대분류 카테고리 수정.
	 * @param params
	 * @return
	 */
	public int updateCategoryLarge(Map<String, Object> params){
		return this.sqlSession.update(namespace + "updateCategoryLarge", params);
	}
	
	/**
	 * 
	 * @Method Name : deleteCategoryLarge
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 대분류 카테고리 삭제.
	 * @param params
	 * @return
	 */
	public int deleteCategoryLarge(Map<String, Object> params){
		return this.sqlSession.delete(namespace + "deleteCategoryLarge", params);
	}
	
	/**
	 * 
	 * @Method Name : findCategoryMiddle
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 중분류 카테고리 조회.
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findCategoryMiddle(Map<String, Object> params){
		return this.sqlSession.selectList(namespace + "findCategoryMiddle", params);
	}
	
	/**
	 * 
	 * @Method Name : getCategoryMiddle
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 중분류 카테고리 상세조회
	 * @param params
	 * @return
	 */
	public Map<String, Object> getCategoryMiddle(Map<String, Object> params){
		return this.sqlSession.selectOne(namespace + "getCategoryMiddle", params);
	}
	
	/**
	 * 
	 * @Method Name : createCategoryMiddle
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 중분류 카테고리 등록
	 * @param params
	 * @return
	 */
	public int createCategoryMiddle(Map<String, Object> params){
		return this.sqlSession.insert(namespace + "createCategoryMiddle", params);
	}
	
	/**
	 * 
	 * @Method Name : updateCategoryMiddle
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 중분류 카테고리 수정
	 * @param params
	 * @return
	 */
	public int updateCategoryMiddle(Map<String, Object> params){
		return this.sqlSession.update(namespace + "updateCategoryMiddle", params);
	}
	
	/**
	 * 
	 * @Method Name : deleteCategoryMiddle
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 중분류 카테고리 삭제
	 * @param params
	 * @return
	 */
	public int deleteCategoryMiddle(Map<String, Object> params){
		return this.sqlSession.delete(namespace + "deleteCategoryMiddle", params);
	}
	
	/**
	 * 
	 * @Method Name : findCategorySmall
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 소분류 카테고리 조회
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findCategorySmall(Map<String, Object> params){
		return this.sqlSession.selectList(namespace + "findCategorySmall", params);
	}
	
	/**
	 * 
	 * @Method Name : getCategorySmall
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 소분류 카테고리 상세조회
	 * @param params
	 * @return
	 */
	public Map<String, Object> getCategorySmall(Map<String, Object> params){
		return this.sqlSession.selectOne(namespace + "getCategorySmall", params);
	}
	
	/**
	 * 
	 * @Method Name : createCategorySmall
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 소분류 카테고리 등록
	 * @param params
	 * @return
	 */
	public int createCategorySmall(Map<String, Object> params){
		return this.sqlSession.insert(namespace + "createCategorySmall", params);
	}
	
	/**
	 * 
	 * @Method Name : updateCategorySmall
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 소분류 카테고리 수정
	 * @param params
	 * @return 
	 */
	public int updateCategorySmall(Map<String, Object> params){
		return this.sqlSession.update(namespace + "updateCategorySmall", params);
	}
	
	/**
	 * 
	 * @Method Name : deleteCategorySmall
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 소분류 카테고리 삭제
	 * @param params
	 * @return
	 */
	public int deleteCategorySmall(Map<String, Object> params){
		return this.sqlSession.delete(namespace + "deleteCategorySmall", params);
	}
	
}