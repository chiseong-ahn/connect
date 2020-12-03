package com.scglab.connect.services.automessage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "자동메세지 정보")
@Setter
@Getter
@ToString
public class AutoMessage {
	private Long id;
	private String companyId;
	private int type;
	private String message;
	private String createDate;
	private String updateDate;
	private String updateMemberId;
}