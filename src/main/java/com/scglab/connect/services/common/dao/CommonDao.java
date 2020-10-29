package com.scglab.connect.services.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @FileName : CommonDaoImpl.java
 * @Project : connect
 * @Date : 2020. 9. 23. 
 * @작성자 : anchiseong
 * @변경이력 :
 * @프로그램 설명 : 공통 DAO 추상 클래스 (기본 CRUD 기능 제공 - 메소드명과 xml의 id와 자동매핑)
 */
public abstract class CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected SqlSession sqlSession;
	
	public String namespace;
	
	private String getMethodName(Class<?> c){
		return c.getEnclosingMethod().getName();
	}
	
	protected abstract String getNamespace();
	
	public int selectCount(Object params){		
		return (int)this.sqlSession.selectOne(this.getNamespace() + this.getMethodName(new Object() {}.getClass()), params);
	}
	
	public <E> List<E> selectAll(Object params) {
		return this.sqlSession.selectList(this.getNamespace() + this.getMethodName(new Object() {}.getClass()), params);
	}
	
	public <T> T selectOne(Object params){
		this.logger.debug("selectOne : " + this.getNamespace() + this.getMethodName(new Object() {}.getClass()));
		return this.sqlSession.selectOne(this.getNamespace() + this.getMethodName(new Object() {}.getClass()), params);
	}
	
	public int insert(Object params) {
		return this.sqlSession.insert(this.getNamespace() + this.getMethodName(new Object() {}.getClass()), params);
	}
	
	public int update(Object params) {
		return (int)this.sqlSession.update(this.getNamespace() + this.getMethodName(new Object() {}.getClass()), params);
	}
	
	public int delete(Object params) {
		return (int)this.sqlSession.delete(this.getNamespace() + this.getMethodName(new Object() {}.getClass()), params);
	}
	
	public int selectCount(Map<String, Object> params){		
		return (int)this.sqlSession.selectOne(this.getNamespace() + this.getMethodName(new Object() {}.getClass()), params);
	}
	
	public <E> List<E> selectAll(Map<String, Object> params) {
		return this.sqlSession.selectList(this.getNamespace() + this.getMethodName(new Object() {}.getClass()), params);
	}
	
	public <T> T selectOne(Map<String, Object> params){		
		return this.sqlSession.selectOne(this.getNamespace() + this.getMethodName(new Object() {}.getClass()), params);
	}
	
	public int insert(Map<String, Object> params) {
		return this.sqlSession.insert(this.getNamespace() + this.getMethodName(new Object() {}.getClass()), params);
	}
	
	public int update(Map<String, Object> params) {
		return (int)this.sqlSession.update(this.getNamespace() + this.getMethodName(new Object() {}.getClass()), params);
	}
	
	public int delete(Map<String, Object> params) {
		return (int)this.sqlSession.delete(this.getNamespace() + this.getMethodName(new Object() {}.getClass()), params);
	}
	
	

	
}
