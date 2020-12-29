package com.scglab.connect.services.link;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LinkDetail {
	private long id;
	private int companyId;
	private String createDate;
	private String updateDate;
	private long updateMemberId;
	private String linkProtocol;
	private String linkText;
	private String linkUrl;
	private int enable;
	private long menuId;
}
