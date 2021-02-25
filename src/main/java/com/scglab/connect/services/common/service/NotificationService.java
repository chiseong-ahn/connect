package com.scglab.connect.services.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

@Service
public class NotificationService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final String userName = "SYSTEM";

	@Value("${notification.use}")
	private boolean use;

	private String webHookUrl1 = "https://hooks.slack.com";
	private String webHookUrl2 = "/services/T0FMF2XEH/B01P758C2KF/9Ieec5dYR6oEbNqS52iTymZC";

	public void webhookForSlack(String message) {
		webhookForSlack(this.userName, message);
	}

	public void webhookForSlack(String userName, String message) {
		
		webhookForSlack(new SlackMessage(userName, message));
	}

	public void webhookForSlack(SlackMessage slackMessage) {
		webhookForSlack(this.webHookUrl1 + this.webHookUrl2, slackMessage);
	}

	public void webhookForSlack(String webHookUrl, SlackMessage slackMessage) {
		if (use) {
			this.logger.debug("webhookUrl : " + webHookUrl);
			this.logger.debug("slackMessage : " + slackMessage);
			
			SlackApi api = new SlackApi(webHookUrl);
			api.call(slackMessage);
		}
	}

}
