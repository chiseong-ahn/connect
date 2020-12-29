package com.scglab.connect.services.member;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Member implements Serializable {
	private static final long serialVersionUID = 1L;

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
	private String deptName;
	private String positionName;
	private String createDate;
	private String updateDate;
	private String updateMemberId;
}
