package com.scglab.connect.services.customer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class VCustomer {
	private long id;
	private long customerId;
	private String gasappMemberNumber;
	private String name;
	private String telNumber;
	private String createDate;
	private String updateDate;
	private long updateMemberId;
	private String companyId;
	private int isBlock;
	private String blockType;
	private String remark;
	private long blockMemberId;
	private String blockMemberName;
	private long roomId;
	private long speakerId;
	private int swearCount;
	private int insultCount;
	private int state;
	private String blockDate;
	private int roomState;
	private long readLastMessageId;
	private int noReadCount;
	private int	endDays;
}
