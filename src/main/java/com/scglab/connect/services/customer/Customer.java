package com.scglab.connect.services.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(name = "고객")
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
