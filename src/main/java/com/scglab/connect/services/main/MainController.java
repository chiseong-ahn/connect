package com.scglab.connect.services.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scglab.connect.services.common.service.MessageService;

@Controller
public class MainController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageService messageService;
	
	@RequestMapping(method = RequestMethod.GET, name = "main", value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public String main(HttpServletRequest request, HttpServletResponse response) {
		return "redirect:/page/main";
	}

		/*
	@RequestMapping(method = RequestMethod.GET, name = "greeting", value = "/greeting", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> greeting(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("greeting", this.messageService.getMessage("main.greeting", null));
		return map;
	}
	
	@RequestMapping(method = RequestMethod.GET, name = "Health check", value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
	public void health(HttpServletRequest request, HttpServletResponse response) {
	}
	*/
	
}