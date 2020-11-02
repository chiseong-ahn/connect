package com.scglab.connect.services.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.services.customer.Customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/commons")
@Tag(name = "공통", description = "공통 API")
public class CommonController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CommonService commonService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="서버 헬스체크", description = "")
	public void health(@RequestBody Customer customer) throws Exception {
		this.logger.debug("Health check!");
	}
	
}


