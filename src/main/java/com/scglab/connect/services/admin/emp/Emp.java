package com.scglab.connect.services.admin.emp;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Emp implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;
	
	private int cid;
	
	private int speaker;
	
	private int auth;
	
	private int profileimg;
	
	private String empno;
	
	private int state;
	
	private LocalDateTime createDate;
	
	private LocalDateTime workDate;
}
