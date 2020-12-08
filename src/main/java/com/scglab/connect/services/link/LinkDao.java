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
	 * @Method Name : findMenu
	 * @작성일 : 2020. 12. 7.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 링크상세
	 * @param params
	 * @return
	 */
	public LinkMenu findLinkMenu(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "findLinkMenu", params);
	}
	
	public LinkDetail findLinkDetail(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "findLinkDetail", params);
	}
	
	public List<LinkDetail> findLinkDetailByMenuId(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findLinkDetailByMenuId", params);
	}
	
	/**
	 * 
	 * @Method Name : findDetailAll
	 * @작성일 : 2020. 12. 5.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 링크상세 전체 조회.
	 * @param params
	 * @return
	 */
	public List<LinkDetail> findDetailAll(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findDetailAll", params);
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
		return this.sqlSession.selectList(this.namespace + "findDetailByMenuIdAndEnableStatus", params);
	}
	
	public int createLinkMenu(Map<String, Object> params) {
		return this.sqlSession.insert(this.namespace + "createLinkMenu", params);
	}
	
	public int updateLinkMenu(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "updateLinkMenu", params);
	}
	
	public int deleteLinkMenu(Map<String, Object> params) {
		return this.sqlSession.delete(this.namespace + "deleteLinkMenu", params);
	}
	
	public int createLinkDetail(Map<String, Object> params) {
		return this.sqlSession.insert(this.namespace + "createLinkDetail", params);
	}
	
	public int updateLinkDetail(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "updateLinkDetail", params);
	}
	
	public int updateLinkDetailEnable(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "updateLinkDetailEnable", params);
	}
	
	public int deleteLinkDetail(Map<String, Object> params) {
		return this.sqlSession.delete(this.namespace + "deleteLinkDetail", params);
	}
	
	public int deleteLinkDetailByMenuId(Map<String, Object> params) {
		return this.sqlSession.delete(this.namespace + "deleteLinkDetailByMenuId", params);
	}
}