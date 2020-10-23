package com.scglab.connect.services.talk;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Speak implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private LocalDateTime createDate;
	private LocalDateTime workDate;
	private int cid;
	private long space;
	private long speaker;
	private int mtype;
	private int notread;
	private int sysmsg;
	private String msg;
	private String morp;
	private int onlyadm;
	private boolean isonline;
	private boolean isemp;
	private boolean iscustomer;
	private String msgname;	
}
