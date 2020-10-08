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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/samples")
@Api(tags = "1. CRUD 예제 API")
public class SampleController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	SampleService sampleService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "샘플 목록 조회", notes = "모든 게시물을 조회한다.")
	public Map<String, Object> list(@ApiParam(hidden=true) @RequestParam Map<String, Object> params) throws Exception {
		this.logger.debug("DEBUG LOGGER TEST");
		return this.sampleService.list(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "샘플 상세 조회", notes = "게시물의 상세내용을 조회한다.")
	public Map<String, Object> object(@ApiParam(hidden=true) @RequestParam Map<String, Object> params, @ApiParam(value="식별번호 아이디", required=true) @PathVariable String id) throws Exception {
		return this.sampleService.object(params, id);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "샘플 등록", notes = "게시물을 등록한다.")
	public Map<String, Object> save(@ApiParam(hidden=true) @RequestParam Map<String, Object> params) throws Exception {
		return this.sampleService.save(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "샘플 수정", notes = "게시물의 정보를 수정한다.")
	public Map<String, Object> update(@ApiParam(hidden=true) @RequestParam Map<String, Object> params) throws Exception {
		return this.sampleService.update(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "샘플 삭제", notes = "게시물을 삭제한다.")
	public Map<String, Object> delete(@ApiParam(hidden=true) @RequestParam Map<String, Object> params) throws Exception {
		return this.sampleService.delete(params);
	}
	
}


