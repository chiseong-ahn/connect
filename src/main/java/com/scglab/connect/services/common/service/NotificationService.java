package com.scglab.connect.services.common.service;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fcibook.quick.http.ResponseBody;
import com.scglab.connect.utils.HttpUtils;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

@Service
public class NotificationService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String webHookUrl1 = "https://hooks.slack.com";
	private String webHookUrl2 = "/services/T0FMF2XEH/B01P758C2KF/9Ieec5dYR6oEbNqS52iTymZC";

	public void webhookForSlack(String userName, String message) {
		
		String webhookUrl = this.webHookUrl1 + this.webHookUrl2;
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", userName);
		params.put("text", message);
		
		//ResponseBody response = HttpUtils.postForResponseBody(webhookUrl, params);
		
		String jsonContent = JSONObject.toJSONString(params);
		ResponseBody response = HttpUtils.requestForPostwithBodyContent(webhookUrl, jsonContent);
		
		//this.logger.debug("response code : " + obj.toString());
	}
	
	public void webhookForSlack2(String userName, String message) {
		
		String webhookUrl = this.webHookUrl1 + this.webHookUrl2;
		SlackMessage slackMessage = new SlackMessage(userName, message);
		
		this.logger.debug("webhookUrl : " + webhookUrl);
		this.logger.debug("slackMessage : " + slackMessage);
		
		SlackApi api = new SlackApi(webhookUrl);
		api.call(slackMessage);
	}
}
