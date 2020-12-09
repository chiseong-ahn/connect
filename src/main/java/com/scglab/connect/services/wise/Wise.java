package com.scglab.connect.services.wise;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "민원 정보")
@Setter
@Getter
@ToString
public class Wise {
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
}
