package com.scglab.connect.services.stats;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StatsMyToday {
	private String companyId;
	private String memberId;
	private int newCount;
	private int readyCount;
	private int ingCount;
	private int closeCount;
	private int outCount;
	private int maxReadyMinute;
	private int maxSpeakMinute;
	private int avgReadyMinute;
	private int avgSpeakMinute;
}
