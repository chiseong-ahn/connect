package com.scglab.connect.services.stats;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StatsCompany {
	private int id;
	private String companyId;
	private String saveDate;
	private int chatbotUseCount;
	private int talkSystemEnterCount;
	private int newCount;
	private int readyCount;
	private int ingCount;
	private int closeCount;
	private int outCount;
	private int speakCount;
	private int maxReadyMinute;
	private int maxSpeakMinute;
	private int avgReadyMinute;
	private int avgSpeakMinute;
	private int avgMemberSpeakCount;
	private int beforeDayPlusCount;
}
