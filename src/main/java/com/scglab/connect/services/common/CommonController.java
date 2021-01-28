package com.scglab.connect.services.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.services.common.service.NotificationService;

@RestController
@RequestMapping(name = "공통", value = "/commons")
public class CommonController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CommonService commonService;
	@Autowired
	NotificationService notiService;

	@RequestMapping(name = "서버 헬스체크", method = RequestMethod.GET, value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
	public void health() throws Exception {
		this.logger.debug("Health check!");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/server", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> server(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("client", request.getRemoteAddr());
		res.put("serverName", request.getServerName());

		return res;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/slack/noti", produces = MediaType.APPLICATION_JSON_VALUE)
	public int slackNoti(HttpServletRequest request, HttpServletResponse response) {

		String webhookUrl = "https://hooks.slack.com/services/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";
		String message = "슬랙으로 메세지 보내기";
		this.notiService.webhookForSlack(webhookUrl, message);

		return 1;
	}
}
