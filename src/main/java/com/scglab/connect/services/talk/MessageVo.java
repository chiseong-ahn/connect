package com.scglab.connect.services.talk;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageVo {

	public enum MsgType {
		TOKEN, JOIN, JOINED, WELCOME, DELAY, MESSAGE, LEAVE, END, RELOAD, PREHISTORY, SPEAKS, READS, READSEMP,
		MEBMER_INFO, READYCOUNT, ASSIGN, ASSIGNED, SPACEINFO, CUSTOMER_INFO 
	}
	
	private MsgType msgType;
	private String roomId;
	private int cid;
	private int isemp;
	private int userno;
	private String token;
	private Object data;
	
	
	
}
