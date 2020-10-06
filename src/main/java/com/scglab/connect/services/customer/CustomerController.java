package com.scglab.connect.services.customer;

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

import com.scglab.connect.base.annotatios.Auth;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CustomerService customerService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, name = "고객정보조회", value = "/{speaker}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> customer(@RequestParam Map<String, Object> params, @PathVariable String speaker) throws Exception {
		this.logger.debug("speaker : " + speaker);
		params.put("speaker", speaker);
		return this.customerService.customer(params);
	}
	
	
	
}


