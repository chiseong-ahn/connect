package com.scglab.connect.services.minwon;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class MinwonDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "minwon.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	/**
	 * 
	 * @Method Name : findMinwon
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 민원 조회
	 * @param params
	 * @return
	 */
	public Minwon findMinwon(Map<String, Object> params) {
		return this.sqlSession.selectOne(namespace + "findMinwon", params);
	}
	
	/**
	 * 
	 * @Method Name : insertMember
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 민원등록처리.
	 * @param params
	 * @return
	 */
	public int insertMinwon(Map<String, Object> params) {
		return this.sqlSession.insert(namespace + "insertMinwon", params);
	}
	
	/**
	 * 
	 * @Method Name : findSearchByRoomId
	 * @작성일 : 2021. 1. 5.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 등록된 민원 조회.
	 * @param params
	 * @return
	 */
	public List<Minwon> findSearchByRoomId(Map<String, Object> params) {
		return this.sqlSession.selectList(this.namespace + "findSearchByRoomId", params);
	}
}