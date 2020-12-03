package com.scglab.connect.services.link;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class LinkDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "link.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	/**
	 * 
	 * @Method Name : findMenuAll
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 링크 메뉴 전체 조회.
	 * @param params
	 * @return
	 */
	public List<LinkMenu> findMenuAll(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findMenuAll", params);
	}
	
	/**
	 * 
	 * @Method Name : findByMenuIdAndEnableStatus
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 활성화 되어있는 메뉴에 속한 링크 상세 조회.
	 * @param params
	 * @return
	 */
	public List<LinkMenu> findByMenuIdAndEnableStatus(Map<String, Object> params) {
		return this.sqlSession.selectList(this.namespace + "findByMenuIdAndEnableStatus", params);
	}
	
	/**
	 * 
	 * @Method Name : findTree
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 링크 전체 목록 : 트리
	 * @param params
	 * @return
	 */
	public List<Link> findTree(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findTree", params);
	}
}