package com.scglab.connect.services.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "로그인 사용자 정보")
@Setter
@Getter
@ToString
public class Member {
	private int id;
	private String companyId;
	private Object companyUseConfigJson;
	private String companyName;
	private int isAdmin;
	private int isCustomer;
	private int authLevel;
	private String loginName;
	private int state;
	private int profileImageId;
	private int speakerId;
	private String name;
	private String createDate;
	private String updateDate;
	private String updateMemberId;
}
