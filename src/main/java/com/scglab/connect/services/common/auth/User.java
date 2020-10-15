package com.scglab.connect.services.common.auth;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int emp;
	private int cid;
	private int speaker;
	private String name;
	private int auth;
    private String empno;
}