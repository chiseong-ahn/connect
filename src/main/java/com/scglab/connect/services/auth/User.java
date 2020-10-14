package com.scglab.connect.services.auth;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String emp;
	private String cid;
	private String speaker;
	private String name;
	private String auth;
    private String empno;
    private String token;
}