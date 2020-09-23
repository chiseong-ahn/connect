package com.scglab.connect.services.sample;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/samples")
public class sampleController {
	
	Logger logger = LoggerFactory.getLogger(sampleController.class);
	
	@Autowired
	sampleService sampleService;
	
	@RequestMapping(method = RequestMethod.GET, name = "목록 조회", value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> list(@RequestParam Map<String, Object> params) throws Exception {
		return this.sampleService.list(params);
	}
	
	@RequestMapping(method = RequestMethod.GET, name = "상세 조회", value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> object(@RequestParam Map<String, Object> params, @PathVariable String id) throws Exception {
		return this.sampleService.object(id, params);
	}
	
}


