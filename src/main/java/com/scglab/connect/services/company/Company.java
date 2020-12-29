package com.scglab.connect.services.company;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Company {
	
	private int id;
	private String createDate;
	private String updateDate;
	private int updateMemberId;
	private String name;
	private String alias;
	private String telNumber;
	private String faxNumber;
	private String callCenterNumber;
	private String email;
	private String address;
	private String homepage;
	private Object useConfigJson;
	
}
