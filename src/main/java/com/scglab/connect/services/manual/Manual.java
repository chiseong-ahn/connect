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
	private int id;
	private int companyId;
	private int updateMemberId;
	private int manualIndex;
	private int pageNumber;
	private int pageNo;
	private String pageCode;
	private String title;
	private String tags;
	private String content;
	private String pdfImagePath;
}
