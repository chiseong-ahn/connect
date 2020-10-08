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

import com.scglab.connect.base.annotatios.Auth;

@RestController
@RequestMapping("/samples")
public class SampleController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	SampleService sampleService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> list(@RequestParam Map<String, Object> params) throws Exception {
		this.logger.debug("DEBUG LOGGER TEST");
		return this.sampleService.list(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> object(@RequestParam Map<String, Object> params, @PathVariable String id) throws Exception {
		return this.sampleService.object(params, id);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> save(@RequestParam Map<String, Object> params) throws Exception {
		return this.sampleService.save(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> update(@RequestParam Map<String, Object> params) throws Exception {
		return this.sampleService.update(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> delete(@RequestParam Map<String, Object> params) throws Exception {
		return this.sampleService.delete(params);
	}
	
}


