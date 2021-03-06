package com.scglab.connect.services.minwon;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Minwon {
	private Long id;
	private String companyId;
	private String createDate;
	private String updateDate;
	private int updateMemberId;
	private int categorySmallId;
	private String minwonCode;
	private String gasappMemberNumber;
	private String useContractNum;
	private String telNumber;
	private String memo;
	private Long chatId;
	private int roomId;
	private String createMemberName;
	private String categoryLargeName;
	private String categoryMiddleName;
	private String categorySmallName;
}
