package com.scglab.connect.services.auth;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class User {
	private String emp;
	private String cid;
	private String speaker;
	private String auth;
    private String empno;
    private String token;
}