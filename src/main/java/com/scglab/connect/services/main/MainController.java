package com.scglab.connect.services.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(method = RequestMethod.GET, name = "main", value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public String main(HttpServletRequest request, HttpServletResponse response) {
		return "auth/login";
	}
	
	@RequestMapping(method = RequestMethod.GET, name = "상담채팅", value = "/talk", produces = MediaType.APPLICATION_JSON_VALUE)
	public String talk(HttpServletRequest request, HttpServletResponse response) {
		return "talk/room";
	}
	
	@RequestMapping(method = RequestMethod.GET, name = "관리자메뉴 > 계정관리", value = "/admin/emp", produces = MediaType.APPLICATION_JSON_VALUE)
	public String adminEmp(HttpServletRequest request, HttpServletResponse response) {
		return "admin/emp";
	}
	
}