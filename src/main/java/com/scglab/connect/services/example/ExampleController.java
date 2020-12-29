package com.scglab.connect.services.example;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotations.Auth;

@RestController
@RequestMapping(name = "CRUD 예제", value="/api/example")
public class ExampleController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private ExampleService exampleService;
	
	@RequestMapping(name="목록 조회", method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> list(@RequestParam Map<String, Object> params) throws Exception {
		this.logger.debug("DEBUG LOGGER TEST");
		return this.exampleService.selectAll(params);
	}
	
	@RequestMapping(name="상세정보 조회", method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> object(@RequestParam Map<String, Object> params, @PathVariable String id) throws Exception {
		params.put("id", id);
		return this.exampleService.selectOne(params, id);
	}
	
	@RequestMapping(name="게시물 생성(등록)", method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> save(@RequestBody Map<String, Object> params) throws Exception {
		return this.exampleService.insert(params);
	}
	
	@RequestMapping(name="게시물 정보 변경(수정)", method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> update(@PathVariable long id, @RequestBody Map<String, Object> params) throws Exception {
		params.put("id", id);
		return this.exampleService.update(params);
	}
	
	@RequestMapping(name="게시물 삭제", method = RequestMethod.DELETE, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> delete(@PathVariable long id, @RequestParam Map<String, Object> params) throws Exception {
		params.put("id", id);
		return this.exampleService.delete(params);
	}
	
	
	@RequestMapping(name="로그인 테스트", method = RequestMethod.POST, value = "login", produces = MediaType.APPLICATION_JSON_VALUE)
	public void login(@RequestParam Map<String, Object> params) throws Exception {
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/api/needLogin", produces = MediaType.APPLICATION_JSON_VALUE)
	public String needLogin(@RequestParam Map<String, Object> params) throws Exception {
	    return "인증이 유효합니다."; 
	}
}


