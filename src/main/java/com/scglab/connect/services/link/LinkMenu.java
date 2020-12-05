package com.scglab.connect.services.link;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(name = "링크메뉴")
@Getter
@Setter
@ToString
public class LinkMenu {
	private long id;
	private int companyId;
	private String createDate;
	private String updateDate;
	private long updateMemberId;
	private String name;
	private List<LinkDetail> childs;
}
