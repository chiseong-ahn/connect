package com.scglab.connect.services.chat;

import java.io.Serializable;
import java.util.UUID;

import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Api(tags = "스페이스")
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
	
    private String name;
	
    private long userCount; // 채팅방 인원수

    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }
}