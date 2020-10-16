package com.scglab.connect.services.talk;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class ChatMessage {
	
	// 메시지 타입 : 입장, 퇴장, 채팅
    public enum MessageType {
       JOIN, SPACEINFO, END, LEAVE, LOGIN, LOGOUT, 
       PREHISTORY, MESSAGE, SPEAKS, READS, READSEMP, 
       PAYLOAD, MEBMER_INFO, TOKEN, DISCONNECT, WELCOME
    }

    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String message; // 메시지
    
    private long userCount; // 채팅방 인원수, 채팅방 내에서 메시지가 전달될때 인원수 갱신시 사용
    private int cid;
    private int space;
    private String speaker;
    private int mtype;
    private int notread;
    private String msg;

    public ChatMessage() {
		super();
	}
    
    public ChatMessage(int cid, String roomId, MessageType type) {
		super();
		this.cid = cid;
		this.roomId = roomId;
		this.type = type;
		
	}

	@Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message, long userCount) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.userCount = userCount;
    }

    
}