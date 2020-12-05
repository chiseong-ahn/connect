package com.scglab.connect.services.talk;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TalkMessage {

	// 메시지 타입
	public enum MessageType {
		READYCOUNT,
		JOIN, JOINED, WELCOME, DELAY,
		ASSIGN, ASSIGNED,
		PREHISTORY, SPEAKS,
		MESSAGE,
		READS, READ_BY_EMP, READ_BY_CUSOMTER, READSEMP,
		MEBMER_INFO, SPACEINFO, CUSTOMER_INFO,
		LEAVE,
		END, END_BY_EMP, END_BY_CUSTOMER,
		RELOAD
	}

	private MessageType type;			// 메시지 타입
	
	private long id;
	private String roomId;				// 방번호
	private String sender;				// 메시지 보낸사람
	private long userCount;				// 채팅방 인원수, 채팅방 내에서 메시지가 전달될때 인원수 갱신시 사용
	private String prj;					// 프로젝트 구분.
	private String appid;				// 어플리케이션 구분(sdtapp-app, sdtadmin-admin)
	private String companyId;					// 기관구분(1-SCG, 2-INC)
	private int isemp;					// 상담사여부 (1-상담사)
	private int isonline;
	private int startid;
	private long speaker;				// 채팅참여자 관리번호.
	private int mtype;					// 미디어타입(1-일반텍스트)
	private int sysmsg;					// 1-시스템메세지, 2-일반메세지
	private int notread;				// 메세지 읽지않은 카운트
	private int onlyadm;
	private String msg;					// 메세지 내용.
	private long userno;
	private String token;				// 인증 토큰.
	//private LocalDateTime createdate;
	//private LocalDateTime workdadate;
	private Map<String, Object> data;	// 기타 데이터.

	public TalkMessage() {
		super();
	}

	public TalkMessage(MessageType type, String companyId, String roomId) {
		super();
		this.companyId = companyId;
		this.roomId = roomId;
		this.type = type;
	}

	public TalkMessage(MessageType type, String companyId, String roomId, String message) {
		super();
		this.companyId = companyId;
		this.roomId = roomId;
		this.type = type;
		this.msg = message;
	}

	public TalkMessage(MessageType type, String companyId, String roomId, String msg, Map<String, Object> data) {
		super();
		this.companyId = companyId;
		this.roomId = roomId;
		this.type = type;
		this.msg = msg;
		this.data = data;
	}

	@Builder
	public TalkMessage(MessageType type, String roomId, String sender, String msg, long userCount) {
		this.type = type;
		this.roomId = roomId;
		this.sender = sender;
		this.msg = msg;
		this.userCount = userCount;
	}

	@Builder
	public TalkMessage(MessageType type, String roomId, String sender, String msg, long userCount,
			Map<String, Object> data) {
		this.type = type;
		this.roomId = roomId;
		this.sender = sender;
		this.msg = msg;
		this.userCount = userCount;
		this.data = data;
	}

}