package com.scglab.connect.services.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @FileName : CommonDaoImpl.java
 * @Project : connect
 * @Date : 2020. 9. 23. 
 * @작성자 : anchiseong
 * @변경이력 :
 * @프로그램 설명 : 공통 DAO 클래스 (기본 CRUD 기능 제공 - 메소드명과 xml의 id와 자동매핑)
 */
public class CommonDao {
	
	@Autowired
	protected SqlSession sqlSession;
	
	public String namespace;
	
	private String getMethodName(Class<?> c){
		return c.getEnclosingMethod().getName();
	}
	
	protected String getNamespace() {
		return namespace;
	}
	
	public int selectCount(Map<String, Object> params){		
		return (int)sqlSession.selectOne(getNamespace() + getMethodName(new Object() {}.getClass()), params);
	}
	
	public <E> List<E> selectAll(Map<String, Object> params) {
		return sqlSession.selectList(getNamespace() + getMethodName(new Object() {}.getClass()), params);
	}
	
	public <T> T selectOne(Map<String, Object> params){		
		return sqlSession.selectOne(getNamespace() + getMethodName(new Object() {}.getClass()), params);
	}
	
	public int insert(Map<String, Object> params) {
		return this.sqlSession.insert(getNamespace() + getMethodName(new Object() {}.getClass()), params);
	}
	
	public int update(Map<String, Object> params) {
		return (int)this.sqlSession.update(getNamespace() + getMethodName(new Object() {}.getClass()), params);
	}
	
	public int delete(Map<String, Object> params) {
		return (int)this.sqlSession.delete(getNamespace() + getMethodName(new Object() {}.getClass()), params);
	}
	
	

	
}
