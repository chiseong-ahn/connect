package com.scglab.connect.services.category;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CategorySmall {
	private long id;
	private String createDate;
	private String updateDate;
	private long memberId;
	private long updateMemberId;
	private String companyId;
	private String name;
	private long categoryLargeId;
	private String categoryLargeName;
	private long categoryMiddleId;
	private String categoryMiddleName;
	private String minwonCode;
	private String minwonName;
	private String key;
	private String title;
	private int level;
	private int sortIndex;
}
