package com.scglab.connect.services.example;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/examples")
public class ExampleController {
	
	Logger logger = LoggerFactory.getLogger(ExampleController.class);
	
	@Autowired
	ExampleService exampleService;
	
	@RequestMapping(method = RequestMethod.GET, name = "목록 조회", value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> list(@RequestParam Map<String, Object> params) {
		return this.exampleService.list(params);
	}
	
}


