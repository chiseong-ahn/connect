package com.scglab.connect.services.talk;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TalkMessage {
	private int id;
	private int cid;
	private String space;
	private int speaker;
	private int mtype;
	private int notread;
	private String msg;
	private int onlyadm;
	private int isemp;
	private String msgname;
	private long userCount;
	private String msgType;
	
}
