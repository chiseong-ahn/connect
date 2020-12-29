package com.scglab.connect.services.customer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Customer {
	private int id;
	private String createDate;
	private String updateDate;
	private long updateMemberId;
	private String companyId;
	private String gasappMemberNumber;
	private String name;
	private String telNumber;
	private int isBlock;
	private String blockType;
	private String remark;
	private int blockMemberId;
	private int roomId;
	private int speakerId;
	private int swearCount;
	private int insultCount;
	private int state;
	private String blockDate;
}
