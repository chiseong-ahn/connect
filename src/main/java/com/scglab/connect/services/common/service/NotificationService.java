package com.scglab.connect.services.common.service;

import org.springframework.stereotype.Service;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

@Service
public class NotificationService {
	
	private final String userName = "SYSTEM";
	private final String webHookUrl = "https://hooks.slack.com/services/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";
	
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
		SlackApi api = new SlackApi(webHookUrl);
		api.call(slackMessage);
	}
	
	
}
