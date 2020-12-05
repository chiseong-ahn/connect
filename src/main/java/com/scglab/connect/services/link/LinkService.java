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
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		this.logger.debug("params : " + params.toString());
		return this.linkDao.findMenuAll(params);
	}
	
	public List<LinkMenu> findDetail(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		this.logger.debug("params : " + params.toString());
		return this.linkDao.findByMenuIdAndEnableStatus(params);
	}
	
	public List<LinkMenu> findTree(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		List<LinkMenu> linkMenuList = this.linkDao.findMenuAll(params);
		List<LinkDetail> linkDetailList = this.linkDao.findDetailAll(params);
		
		for(LinkMenu linkMenu : linkMenuList) {
			linkMenu.setChilds(
				linkDetailList.stream().filter(obj -> obj.getMenuId() == linkMenu.getId()).collect(Collectors.toList())
			);
		}
		
		return linkMenuList;
	}
	
	
}
