package com.scglab.connect.services.manual;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(name = "매뉴얼 관리", value="/api/manual")
public class ManualController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private ManualService manualService;
	
	@Auth
	@RequestMapping(name="매뉴얼 검색", method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> manuals(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.manualService.manuals(params, request, response);
	}
	
	@Auth
	@RequestMapping(name="매뉴얼 상세", method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Manual manual(@PathVariable int id, @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.manualService.manual(params, request, response);
	}
	
	@Auth
	@RequestMapping(name="매뉴얼 태그 목록", method = RequestMethod.POST, value = "tags", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> tags(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.manualService.tags(params, request, response);
		
	}
	
	
	@Auth
	@RequestMapping(name="매뉴얼 즐겨찾기 추가/삭제", method = RequestMethod.POST, value = "/{id}/favorite", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> favorite(@PathVariable int id, @RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.manualService.favorite(params, request, response);
	}
	
	@Auth
	@RequestMapping(name="매뉴얼 등록", method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Manual regist(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.manualService.regist(params, request, response);
	}
	
	@Auth
	@RequestMapping(name="매뉴얼 수정", method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Manual update(@PathVariable int id, @RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.manualService.update(params, request, response);
	}
	
	@Auth
	@RequestMapping(name="매뉴얼 삭제", method = RequestMethod.DELETE, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> delete(@PathVariable int id, @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.manualService.delete(params, request, response);
	}
	
	@Auth
	@RequestMapping(name="매뉴얼 채번", method = RequestMethod.POST, value = "/getNextPageNumber", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> nextPageNumber(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.manualService.nextPageNumber(params, request, response);
	}
}





	