package com.scglab.connect.services.socket;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatRoom implements Serializable {

	private static final long serialVersionUID = 6494678977089006639L;

	private String roomId;
	private String name;
	private long userCount; // 채팅방 인원수

	public static ChatRoom create(String name) {
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.roomId = name;
		chatRoom.name = name;
		return chatRoom;
	}
}