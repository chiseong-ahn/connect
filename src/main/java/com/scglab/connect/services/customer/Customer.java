package com.scglab.connect.services.customer;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Customer {
	private long id;
	private LocalDateTime createDate;
	private LocalDateTime workDate;
	private String userno;
	private long speaker;
	private int state;
	private int blocktype;
	private LocalDateTime blockdt;
	private long blockemp;
	private String remark;
	private long space;
	private int cid;
	private String name;
	private int swear;
	private int insult;
}
