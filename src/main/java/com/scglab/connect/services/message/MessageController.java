package com.scglab.connect.services.message;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotations.Auth;

@RestController
@RequestMapping(name = "메세지 관리", value = "/api/message")
public class MessageController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageService messageService;

	@Auth
	@RequestMapping(name = "메세지 조회", method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Message> search(@RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return this.messageService.getMessages(params, request, response);
	}

	@Auth
	@RequestMapping(name = "메세지 삭제", method = RequestMethod.DELETE, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> delete(@PathVariable int id, @RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.messageService.delete(params, request, response);
	}
	
	@RequestMapping(name="고객의 읽지않은 메시지 카운트 조회", method = RequestMethod.GET, value = "/noreadForCustomer", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> noreadCountForCustomer(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.messageService.noreadCountForCustomer(params, request, response);
	}

}
