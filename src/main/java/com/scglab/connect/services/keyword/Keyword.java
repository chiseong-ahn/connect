package com.scglab.connect.services.keyword;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Keyword {
	private int id;
	private String companyId;
	private String name;
	private String updateMemberId;
	private String createDate;
	private String updateDate;
}
