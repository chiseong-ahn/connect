package com.scglab.connect.services.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "메세지 정보")
@Setter
@Getter
@ToString
public class Message {
	private long id;					// 방id
	private long joinMessageId;
	
}