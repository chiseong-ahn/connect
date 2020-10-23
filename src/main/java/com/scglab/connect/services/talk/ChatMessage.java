package com.scglab.connect.services.talk;

import java.util.Map;

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
       JOIN, SPACEINFO, END, LEAVE, LOGIN, LOGOUT, RELOAD,
       PREHISTORY, MESSAGE, SPEAKS, READS, READSEMP, 
       PAYLOAD, MEBMER_INFO, TOKEN, DISCONNECT, READYCOUNT, ASSIGN, ASSIGNED, WELCOME
    }

    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String message; // 메시지
    
    private long userCount; // 채팅방 인원수, 채팅방 내에서 메시지가 전달될때 인원수 갱신시 사용
    private String prj;		// 프로젝트 구분.
    private String appid;	// 어플리케이션 구분(sdtapp-app, sdtadmin-admin)
    private int cid;
    private int space;
    private int speaker;
    private int mtype;
    private int notread;
    private String msg;
    private String token;
    private Map<String, Object> data;
    
    

    public ChatMessage() {
		super();
	}
    
    public ChatMessage(MessageType type, int cid, String roomId) {
		super();
		this.cid = cid;
		this.roomId = roomId;
		this.type = type;
	}
    
    public ChatMessage(MessageType type, int cid, String roomId, String message) {
		super();
		this.cid = cid;
		this.roomId = roomId;
		this.type = type;
		this.message = message;
	}
    
    public ChatMessage(MessageType type, int cid, String roomId, String message, Map<String, Object> data) {
		super();
		this.cid = cid;
		this.roomId = roomId;
		this.type = type;
		this.message = message;
		this.data = data;
	}
    
    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message, long userCount) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.userCount = userCount;
    }
    
    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message, long userCount, Map<String, Object> data) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.userCount = userCount;
        this.data = data;
    }

    
}