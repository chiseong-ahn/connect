package com.scglab.connect.services.review;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Review {
	private int id;
	private String createDate;
	private int gasappMemberNumber;
	private int memberId;
	private int reviewScore;
	private String memberName;
	private int customerId;
	private String customerName;
	private String startDate;
	private String endDate;
	private long roomId;
	private long startMessageId;
	private long endMessageId;
}
