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
	 * XML의 매핑되는 prefix namespace ex. sdtalk.sample.selectList => sdtalk.sample
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
	public List<LinkMenu> findMenuAll(Map<String, Object> params) {
		return this.sqlSession.selectList(this.namespace + "findMenuAll", params);
	}

	/**
	 * 
	 * @Method Name : findMenu
	 * @작성일 : 2020. 12. 7.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크메뉴 조회.
	 * @param params
	 * @return
	 */
	public LinkMenu findLinkMenu(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "findLinkMenu", params);
	}

	/**
	 * 
	 * @Method Name : findLinkDetail
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크 상세조회.
	 * @param params
	 * @return
	 */
	public LinkDetail findLinkDetail(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "findLinkDetail", params);
	}

	/**
	 * 
	 * @Method Name : findLinkDetailByMenuId
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : menuId를 통한 링크목록 조회.
	 * @param params
	 * @return
	 */
	public List<LinkDetail> findLinkDetailByMenuId(Map<String, Object> params) {
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
	public List<LinkDetail> findDetailAll(Map<String, Object> params) {
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

	/**
	 * 
	 * @Method Name : createLinkMenu
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크메뉴 등록.
	 * @param params
	 * @return
	 */
	public int createLinkMenu(Map<String, Object> params) {
		return this.sqlSession.insert(this.namespace + "createLinkMenu", params);
	}

	/**
	 * 
	 * @Method Name : updateLinkMenu
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크메뉴 수정.
	 * @param params
	 * @return
	 */
	public int updateLinkMenu(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "updateLinkMenu", params);
	}

	/**
	 * 
	 * @Method Name : deleteLinkMenu
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크메뉴 삭제.
	 * @param params
	 * @return
	 */
	public int deleteLinkMenu(Map<String, Object> params) {
		return this.sqlSession.delete(this.namespace + "deleteLinkMenu", params);
	}

	/**
	 * 
	 * @Method Name : createLinkDetail
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크 등록.
	 * @param params
	 * @return
	 */
	public int createLinkDetail(Map<String, Object> params) {
		return this.sqlSession.insert(this.namespace + "createLinkDetail", params);
	}

	/**
	 * 
	 * @Method Name : updateLinkDetail
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크 수정.
	 * @param params
	 * @return
	 */
	public int updateLinkDetail(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "updateLinkDetail", params);
	}

	/**
	 * 
	 * @Method Name : updateLinkDetailEnable
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크 활성여부 수정.
	 * @param params
	 * @return
	 */
	public int updateLinkDetailEnable(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "updateLinkDetailEnable", params);
	}

	/**
	 * 
	 * @Method Name : deleteLinkDetail
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크 삭제.
	 * @param params
	 * @return
	 */
	public int deleteLinkDetail(Map<String, Object> params) {
		return this.sqlSession.delete(this.namespace + "deleteLinkDetail", params);
	}

	/**
	 * 
	 * @Method Name : deleteLinkDetailByMenuId
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : menuId를 통한 링크 삭제.
	 * @param params
	 * @return
	 */
	public int deleteLinkDetailByMenuId(Map<String, Object> params) {
		return this.sqlSession.delete(this.namespace + "deleteLinkDetailByMenuId", params);
	}
}