package com.scglab.connect.services.automessage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AutoMessage {
	private Long id;
	private String companyId;
	private int type;
	private String message;
	private String createDate;
	private String updateDate;
	private long updateMemberId;
}