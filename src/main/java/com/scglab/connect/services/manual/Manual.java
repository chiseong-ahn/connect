package com.scglab.connect.services.manual;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "메뉴얼 정보")
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
	private long favoriteId;
}
