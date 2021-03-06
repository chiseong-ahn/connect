package com.scglab.connect.services.category;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CategoryMiddle {
	private long id;
	private String createDate;
	private String updateDate;
	private long updateMemberId;
	private String companyId;
	private String name;
	private long categoryLargeId;
	private String minwonCode;
	private String minwonName;
	private int sortIndex;
	private String key;
	private String title;
	private int level;
	private List<CategorySmall> children;
}
