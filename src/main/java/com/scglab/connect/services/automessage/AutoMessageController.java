package com.scglab.connect.services.automessage;

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
@RequestMapping(name = "자동메세지 관리", value = "/api/auto-message")
public class AutoMessageController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AutoMessageService autoMessageService;

	@Auth
	@RequestMapping(name = "자동메세지 목록", method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AutoMessage> findAll(@RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return this.autoMessageService.findAll(params, request, response);
	}

	@Auth
	@RequestMapping(name = "자동메세지 상세", method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AutoMessage detail(@PathVariable int id, @RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.autoMessageService.getDetail(params, request, response);
	}

	@Auth
	@RequestMapping(name = "자동메세지 등록", method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public AutoMessage regist(@RequestBody Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return this.autoMessageService.regist(params, request, response);
	}

	@Auth
	@RequestMapping(name = "자동메세지 수정", method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AutoMessage update(@PathVariable int id, @RequestBody Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.autoMessageService.update(params, request, response);
	}

	@Auth
	@RequestMapping(name = "자동메세지 삭제", method = RequestMethod.DELETE, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> delete(@PathVariable int id, @RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.autoMessageService.delete(params, request, response);
	}

}
