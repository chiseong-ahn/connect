package com.scglab.connect.services.wise;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;

@Service
public class WiseService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WiseDao wiseDao;
	@Autowired
	private LoginService loginService;

	public Wise findWise(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		return this.wiseDao.findWise(params);
	}

}