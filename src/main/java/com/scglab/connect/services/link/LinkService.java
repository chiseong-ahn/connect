package com.scglab.connect.services.link;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.ErrorService;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;

@Service
public class LinkService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LinkDao linkDao;
	@Autowired
	private LoginService loginService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ErrorService errorService;

	/**
	 * 
	 * @Method Name : findMenuAll
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크메뉴 검색.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<LinkMenu> findMenuAll(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());

		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		this.logger.debug("params : " + params.toString());
		return this.linkDao.findMenuAll(params);
	}

	/**
	 * 
	 * @Method Name : linkMenu
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크메뉴 상세조회.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public LinkMenu linkMenu(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());

		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		this.logger.debug("params : " + params.toString());
		LinkMenu linkMenu = this.linkDao.findLinkMenu(params);
		params.put("menuId", linkMenu.getId());

		List<LinkDetail> linkDetails = this.linkDao.findLinkDetailByMenuId(params);
		linkMenu.setChilds(linkDetails);

		return linkMenu;

	}

	/**
	 * 
	 * @Method Name : linkDetail
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크 상세조회.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public LinkDetail linkDetail(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		this.logger.debug("params : " + params.toString());
		LinkDetail linkDetail = this.linkDao.findLinkDetail(params);

		return linkDetail;

	}

	/**
	 * 
	 * @Method Name : findDetail
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크메뉴 목록 조회.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<LinkMenu> findDetail(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());

		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		this.logger.debug("params : " + params.toString());
		return this.linkDao.findByMenuIdAndEnableStatus(params);
	}

	/**
	 * 
	 * @Method Name : findTree
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크메뉴 트리형태로 조회
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<LinkMenu> findTree(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());

		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		List<LinkMenu> linkMenuList = this.linkDao.findMenuAll(params);
		List<LinkDetail> linkDetailList = this.linkDao.findDetailAll(params);

		for (LinkMenu linkMenu : linkMenuList) {
			linkMenu.setChilds(linkDetailList.stream().filter(obj -> obj.getMenuId() == linkMenu.getId())
					.collect(Collectors.toList()));
		}

		return linkMenuList;
	}

	/**
	 * 
	 * @Method Name : createLinkMenu
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크메뉴 등록처리.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public LinkMenu createLinkMenu(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());

		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		String errorParams = "";
		if (!this.commonService.valid(params, "name"))
			errorParams = this.commonService.appendText(errorParams, "메뉴명-name");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		LinkMenu linkMenu = null;
		if (this.linkDao.createLinkMenu(params) > 0) {
			linkMenu = this.linkDao.findLinkMenu(params);
		}

		return linkMenu;
	}

	/**
	 * 
	 * @Method Name : updateLinkMenu
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크메뉴 수정.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public LinkMenu updateLinkMenu(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());

		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		String errorParams = "";
		if (!this.commonService.valid(params, "name"))
			errorParams = this.commonService.appendText(errorParams, "이름-name");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		LinkMenu linkMenu = null;
		if (this.linkDao.updateLinkMenu(params) > 0) {
			linkMenu = this.linkDao.findLinkMenu(params);
		}

		return linkMenu;
	}

	/**
	 * 
	 * @Method Name : deleteLinkMenu
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크메뉴 삭제.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> deleteLinkMenu(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());

		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		Map<String, Object> data = new HashMap<String, Object>();

		// 링크메뉴에 속한 링크서브 삭제.
		this.linkDao.deleteLinkDetailByMenuId(params);

		if (this.linkDao.deleteLinkMenu(params) > 0) {
			data.put("success", true);
		} else {
			data.put("success", false);
		}

		return data;
	}

	/**
	 * 
	 * @Method Name : createLinkDetail
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크 등록.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public LinkDetail createLinkDetail(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());

		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		String errorParams = "";
		if (!this.commonService.validInteger(params, "menuId"))
			errorParams = this.commonService.appendText(errorParams, "메뉴id-menuId");

		if (!this.commonService.valid(params, "linkProtocol"))
			errorParams = this.commonService.appendText(errorParams, "링크프로토콜-linkProtocol");

		if (!this.commonService.valid(params, "linkText"))
			errorParams = this.commonService.appendText(errorParams, "링크명-linkText");

		if (!this.commonService.valid(params, "linkUrl"))
			errorParams = this.commonService.appendText(errorParams, "링크주소-linkUrl");

		if (!params.containsKey("enable"))
			errorParams = this.commonService.appendText(errorParams, "활성화여부(1-활성,0-비활성)-enable");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		LinkDetail linkDetail = null;
		if (this.linkDao.createLinkDetail(params) > 0) {
			linkDetail = this.linkDao.findLinkDetail(params);
		}

		return linkDetail;
	}

	/**
	 * 
	 * @Method Name : updateLinkDetail
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크 수정.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public LinkDetail updateLinkDetail(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());

		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		String errorParams = "";

		if (!this.commonService.valid(params, "linkProtocol"))
			errorParams = this.commonService.appendText(errorParams, "링크프로토콜-linkProtocol");

		if (!this.commonService.valid(params, "linkText"))
			errorParams = this.commonService.appendText(errorParams, "링크명-linkText");

		if (!this.commonService.valid(params, "linkUrl"))
			errorParams = this.commonService.appendText(errorParams, "링크주소-linkUrl");

		if (!params.containsKey("enable"))
			errorParams = this.commonService.appendText(errorParams, "활성화여부(1-활성,0-비활성)-enable");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		LinkDetail linkDetail = null;
		if (this.linkDao.updateLinkDetail(params) > 0) {
			linkDetail = this.linkDao.findLinkDetail(params);
		}

		return linkDetail;
	}

	/**
	 * 
	 * @Method Name : updateLinkDetailEnable
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크 활성화상태 수정.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public LinkDetail updateLinkDetailEnable(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());

		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		String errorParams = "";
		if (!params.containsKey("enable"))
			errorParams = this.commonService.appendText(errorParams, "활성화여부(1-활성,0-비활성)-enable");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		LinkDetail linkDetail = null;
		if (this.linkDao.updateLinkDetailEnable(params) > 0) {
			linkDetail = this.linkDao.findLinkDetail(params);
		}

		return linkDetail;
	}

	/**
	 * 
	 * @Method Name : deleteLinkDetail
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 링크 삭제.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> deleteLinkDetail(Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());

		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		Map<String, Object> data = new HashMap<String, Object>();

		if (this.linkDao.deleteLinkDetail(params) > 0) {
			data.put("success", true);
		} else {
			data.put("success", false);
		}

		return data;
	}

}
