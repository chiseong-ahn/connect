package com.scglab.connect.services.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

@Service
public class NotificationService {

	private final String userName = "SYSTEM";

	@Value("${notification.use}")
	private boolean use;

	@Value("${notification.webhook}")
	private String webHookUrl;

	public void webhookForSlack(String message) {
		webhookForSlack(this.userName, message);
	}

	public void webhookForSlack(String userName, String message) {
		webhookForSlack(new SlackMessage(userName, message));
	}

	public void webhookForSlack(SlackMessage slackMessage) {
		webhookForSlack(this.webHookUrl, slackMessage);
	}

	public void webhookForSlack(String webHookUrl, SlackMessage slackMessage) {
		if (use) {
			SlackApi api = new SlackApi(webHookUrl);
			api.call(slackMessage);
		}
	}

}
