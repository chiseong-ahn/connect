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

import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;

@Service
public class LinkService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LinkDao linkDao;
	
	@Autowired
	private MessageHandler messageService;
	
	@Autowired
	private LoginService loginService;
	
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
