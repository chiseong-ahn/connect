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
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;

@Service
public class LinkService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private LinkDao linkDao;
	@Autowired private MessageHandler messageService;
	@Autowired private LoginService loginService;
	@Autowired private CommonService commonService;
	@Autowired private ErrorService errorService;
	
	public List<LinkMenu> findMenuAll(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());
		
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		this.logger.debug("params : " + params.toString());
		return this.linkDao.findMenuAll(params);
	}
	
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
	
	public LinkDetail linkDetail(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		this.logger.debug("params : " + params.toString());
		LinkDetail linkDetail = this.linkDao.findLinkDetail(params);
		
		return linkDetail;
		
	}
	
	public List<LinkMenu> findDetail(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());
		
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		this.logger.debug("params : " + params.toString());
		return this.linkDao.findByMenuIdAndEnableStatus(params);
	}
	
	public List<LinkMenu> findTree(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());
		
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		List<LinkMenu> linkMenuList = this.linkDao.findMenuAll(params);
		List<LinkDetail> linkDetailList = this.linkDao.findDetailAll(params);
		
		for(LinkMenu linkMenu : linkMenuList) {
			linkMenu.setChilds(
				linkDetailList.stream().filter(obj -> obj.getMenuId() == linkMenu.getId()).collect(Collectors.toList())
			);
		}
		
		return linkMenuList;
	}
	
	public LinkMenu createLinkMenu(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());
		
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		String errorParams = "";
	    if(!this.commonService.validString(params, "name"))
	        errorParams = this.commonService.appendText(errorParams, "메뉴명-name");
	    
	    // 파라미터 유효성 검증.
	    if(!errorParams.equals("")) {
	        // 필수파라미터 누락에 따른 오류 유발처리.
	        this.errorService.throwParameterErrorWithNames(errorParams);
	    }
		
		LinkMenu linkMenu = null;
		if(this.linkDao.createLinkMenu(params) > 0) {
			linkMenu = this.linkDao.findLinkMenu(params);
		}
		
		return linkMenu;
	}
	
	public LinkMenu updateLinkMenu(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());
		
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		String errorParams = "";
	    if(!this.commonService.validString(params, "name"))
	        errorParams = this.commonService.appendText(errorParams, "이름-name");
	    
	    // 파라미터 유효성 검증.
	    if(!errorParams.equals("")) {
	        // 필수파라미터 누락에 따른 오류 유발처리.
	        this.errorService.throwParameterErrorWithNames(errorParams);
	    }
	    
		
		LinkMenu linkMenu = null;
		if(this.linkDao.updateLinkMenu(params) > 0) {
			linkMenu = this.linkDao.findLinkMenu(params);
		}
		
		return linkMenu;
	}
	
	public Map<String, Object> deleteLinkMenu(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());
		
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		// 링크메뉴에 속한 링크서브 삭제.
		this.linkDao.deleteLinkDetailByMenuId(params);
		
		if(this.linkDao.deleteLinkMenu(params) > 0) {
			data.put("success", true);
		}else {
			data.put("success", false);
		}
		
		return data;
	}
	
	public LinkDetail createLinkDetail(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());
		
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		String errorParams = "";
	    if(!this.commonService.validString(params, "menuId"))
	        errorParams = this.commonService.appendText(errorParams, "메뉴id-menuId");
	    
	    if(!this.commonService.validString(params, "linkProtocol"))
	        errorParams = this.commonService.appendText(errorParams, "링크프로토콜-linkProtocol");
	    
	    if(!this.commonService.validString(params, "linkText"))
	        errorParams = this.commonService.appendText(errorParams, "링크명-linkText");
	    
	    if(!this.commonService.validString(params, "linkUrl"))
	        errorParams = this.commonService.appendText(errorParams, "링크주소-linkUrl");
	    
	    if(!this.commonService.validString(params, "enable"))
	        errorParams = this.commonService.appendText(errorParams, "활성화여부(1-활성,0-비활성)-enable");
	    
	    // 파라미터 유효성 검증.
	    if(!errorParams.equals("")) {
	        // 필수파라미터 누락에 따른 오류 유발처리.
	        this.errorService.throwParameterErrorWithNames(errorParams);
	    }
	    
		
		LinkDetail linkDetail = null;
		if(this.linkDao.createLinkDetail(params) > 0) {
			linkDetail = this.linkDao.findLinkDetail(params);
		}
		
		return linkDetail;
	}
	
	public LinkDetail updateLinkDetail(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());
		
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		String errorParams = "";
	    
	    if(!this.commonService.validString(params, "linkProtocol"))
	        errorParams = this.commonService.appendText(errorParams, "링크프로토콜-linkProtocol");
	    
	    if(!this.commonService.validString(params, "linkText"))
	        errorParams = this.commonService.appendText(errorParams, "링크명-linkText");
	    
	    if(!this.commonService.validString(params, "linkUrl"))
	        errorParams = this.commonService.appendText(errorParams, "링크주소-linkUrl");
	    
	    if(!this.commonService.validString(params, "enable"))
	        errorParams = this.commonService.appendText(errorParams, "활성화여부(1-활성,0-비활성)-enable");
	    
	    // 파라미터 유효성 검증.
	    if(!errorParams.equals("")) {
	        // 필수파라미터 누락에 따른 오류 유발처리.
	        this.errorService.throwParameterErrorWithNames(errorParams);
	    }
		
		LinkDetail linkDetail = null;
		if(this.linkDao.updateLinkDetail(params) > 0) {
			linkDetail = this.linkDao.findLinkDetail(params);
		}
		
		return linkDetail;
	}
	
	public LinkDetail updateLinkDetailEnable(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());
		
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		String errorParams = "";
	    if(!this.commonService.validString(params, "enable"))
	        errorParams = this.commonService.appendText(errorParams, "활성화여부(1-활성,0-비활성)-enable");
	    
	    // 파라미터 유효성 검증.
	    if(!errorParams.equals("")) {
	        // 필수파라미터 누락에 따른 오류 유발처리.
	        this.errorService.throwParameterErrorWithNames(errorParams);
	    }
		
		LinkDetail linkDetail = null;
		if(this.linkDao.updateLinkDetailEnable(params) > 0) {
			linkDetail = this.linkDao.findLinkDetail(params);
		}
		
		return linkDetail;
	}
	
	public Map<String, Object> deleteLinkDetail(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("loginId", member.getId());
		
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		if(this.linkDao.deleteLinkDetail(params) > 0) {
			data.put("success", true);
		}else {
			data.put("success", false);
		}
		
		return data;
	}
	
	
}
