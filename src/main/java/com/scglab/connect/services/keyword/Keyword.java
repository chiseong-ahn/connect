package com.scglab.connect.services.keyword;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(name = "키워드")
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
