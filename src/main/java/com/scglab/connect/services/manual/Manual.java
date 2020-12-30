package com.scglab.connect.services.manual;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Manual {
	private long id;
	private String createDate;
	private String updateDate;
	private String companyId;
	private long updateMemberId;
	private int manualIndex;
	private int pageNumber;
	private String pageNo;
	private String pageCode;
	private String title;
	private String tags;
	private String content;
	private String pdfImagePath;
	private long previousPage;
	private long nextPage;
	private int isFavorite;
}
