package com.scglab.connect.services.chat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "상담조회", value = "apis/chat")
public class ChatController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ChatService chatService;

	@RequestMapping(name = "중계서버에서 chatid로 상담조회", method = RequestMethod.GET, value = "{chatId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> chat(@PathVariable int chatId, @RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("chatId", chatId);

		return this.chatService.roomHistoryByChatId(params, request, response);
	}

}
