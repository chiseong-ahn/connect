package com.scglab.connect.services.link;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(name = "링크")
@Getter
@Setter
@ToString
public class Link {
	private int id;
	private int companyId;
	private String createDate;
	private String updateDate;
	private int updateMemberId;
	private String name;
}
