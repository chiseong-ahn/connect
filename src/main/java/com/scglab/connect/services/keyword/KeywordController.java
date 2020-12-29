package com.scglab.connect.services.keyword;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotations.Auth;

@RestController
@RequestMapping(name = "키워드", value="/api/keyword")
public class KeywordController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	KeywordService keywordService;
	
	@Auth
	@RequestMapping(name="키워드 전체 조회", method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Keyword> findAll(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.keywordService.findAll(params, request);
	}
	
	@Auth
	@RequestMapping(name="이름으로 키워드 조회", method = RequestMethod.GET, value = "/findByName", produces = MediaType.APPLICATION_JSON_VALUE)
	public Keyword findByName(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.keywordService.findByName(params, request);
	}
	
	@Auth
	@RequestMapping(name="템플릿에 속한 키워드 조회", method = RequestMethod.GET, value = "/findByTemplateId", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Keyword> findByTemplateId(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.keywordService.findByTemplateId(params, request);
	}
	
	@Auth
	@RequestMapping(name="키워드 등록", method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Keyword regist(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.keywordService.regist(params, request);
	}
	
}


