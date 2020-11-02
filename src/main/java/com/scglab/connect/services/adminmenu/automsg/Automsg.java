package com.scglab.connect.services.adminmenu.automsg;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Automsg {
	private long id;
	private LocalDateTime createDate;
	private LocalDateTime workDate;
	private int cid;
	private int type;
	private String msg;
	private int emp;
}
