package com.scglab.connect.services.template;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping(name = "답변템플릿", value = "/api/template")
public class TemplateController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TemplateService templateService;

	@Auth
	@RequestMapping(name = "답변템플릿 검색", method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> search(@RequestParam Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		return this.templateService.search(params, request);
	}

	@Auth
	@RequestMapping(name = "답변템플릿 상세정보 조회", method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> findTemplate(@PathVariable int id, @RequestParam Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.templateService.findTemplate(params, request);
	}

	@Auth
	@RequestMapping(name = "답변템플릿 등록", method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> regist(@RequestBody Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		return this.templateService.create(params, request);
	}

	@Auth
	@RequestMapping(name = "답변템플릿 수정", method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> update(@PathVariable int id, @RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.templateService.update(params, request);
	}

	@Auth
	@RequestMapping(name = "답변템플릿 삭제", method = RequestMethod.DELETE, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> delete(@PathVariable int id, @RequestParam Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.templateService.delete(params, request);
	}

	@Auth
	@RequestMapping(name = "답변템플릿 전체 조회", method = RequestMethod.POST, value = "/findAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> findAll(@RequestBody Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		return this.templateService.findAll(params, request);
	}

	@Auth
	@RequestMapping(name = "답변템플릿 : 카테고리 대분류", method = RequestMethod.POST, value = "/findByCategoryLargeId", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> findByCategoryLargeId(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		return this.templateService.findAll(params, request);
	}

	@Auth
	@RequestMapping(name = "답변템플릿 : 중분류", method = RequestMethod.POST, value = "/findByCategoryMiddleId", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> findByCategoryMiddleId(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		return this.templateService.findAll(params, request);
	}

	@Auth
	@RequestMapping(name = "답변템플릿 : 소분류", method = RequestMethod.POST, value = "/findByCategorySmallId", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> findByCategorySmallId(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		return this.templateService.findAll(params, request);
	}

	@Auth
	@RequestMapping(name = "템플릿 조회 : 내가 즐겨찾기한 템플릿 목록", method = RequestMethod.POST, value = "/findByFavoriteLoginMemberId", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> findByFavoriteLoginMemberId(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		params.put("checkFavorite", 1);
		return this.templateService.findAll(params, request);
	}

	@Auth
	@RequestMapping(name = "답변템플릿 즐겨찾기 추가/삭제", method = RequestMethod.POST, value = "/{id}/favorite", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> saveFavorite(@PathVariable int id, @RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		params.put("id", id);
		params.put("templateId", id);
		return this.templateService.favorite(params, request);
	}

	@Auth
	@RequestMapping(name = "키워드 등록", method = RequestMethod.POST, value = "keyword", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> saveKeyword(@RequestBody Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		return this.templateService.saveKeyword(params, request);
	}

}
