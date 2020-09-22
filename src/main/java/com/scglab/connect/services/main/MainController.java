package com.scglab.connect.services.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	
	@RequestMapping(method = RequestMethod.GET, name = "main", value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("greeting", "Welcome to Connect!");
		return map;
	}
	
	@RequestMapping(method = RequestMethod.GET, name = "Health check", value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
	public void health(HttpServletRequest request, HttpServletResponse response, Model model) {
	}
	
}