package com.scglab.connect.services.common.auth;


import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "사용자")
@ToString
@Getter
@Setter
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "관리자번호", defaultValue = "0")
	private int emp;
	
	@Schema(description = "기관코드", defaultValue = "0")
	private int cid;
	
	@Schema(description = "대화참여번호", defaultValue = "0")
	private int speaker;
	
	@Schema(description = "이름")
	private String name;
	
	@Schema(description = "권한", defaultValue = "0")
	private int auth;
	
	@Schema(description = "관리자아이디", defaultValue = "0")
    private String empno;
	
	@Schema(description = "회원번호", defaultValue = "0")
    private int userno;
}