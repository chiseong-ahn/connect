package com.scglab.connect.services.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

@Service
public class NotificationService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String webHookUrl1 = "https://hooks.slack.com";
	private String webHookUrl2 = "/services/T0FMF2XEH/B01P758C2KF/9Ieec5dYR6oEbNqS52iTymZC";

	public void webhookForSlack(String userName, String message) {
		
		String webhookUrl = this.webHookUrl1 + this.webHookUrl2;
		SlackMessage slackMessage = new SlackMessage(userName, message);
		
		this.logger.debug("webhookUrl : " + webhookUrl);
		this.logger.debug("slackMessage : " + slackMessage);
		
		SlackApi api = new SlackApi(webhookUrl);
		api.call(slackMessage);
	}
}
