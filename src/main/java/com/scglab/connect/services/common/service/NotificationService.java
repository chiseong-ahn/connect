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

	private String webHookUrl = "https://hooks.slack.com/services/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";

	public void webhookForSlack(String message) {
		webhookForSlack(this.userName, message);
	}

	public void webhookForSlack(String userName, String message) {
		
		webhookForSlack(new SlackMessage(userName, message));
	}

	public void webhookForSlack(SlackMessage slackMessage) {
		webhookForSlack(this.webHookUrl, slackMessage);
	}

	public void webhookForSlack(String webhookUrl, SlackMessage slackMessage) {
		if (use) {
			this.logger.debug("webhookUrl : " + webhookUrl);
			this.logger.debug("slackMessage : " + slackMessage);
			
			SlackApi api = new SlackApi(webHookUrl);
			api.call(slackMessage);
		}
	}

}
