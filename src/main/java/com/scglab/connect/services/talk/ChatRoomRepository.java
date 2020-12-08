package com.scglab.connect.services.talk;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatRoomRepository {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// Redis CacheKeys
    private static final String CHAT_ROOMS = "CSTALK_CHATROOM"; // 채팅룸 저장
    public static final String USER_COUNT = "CSTALK_USERCOUNT"; // 채팅룸에 입장한 클라이언트 수 저장
    public static final String JOIN_INFO = "CSTALK_JOIN_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장
    public static final String SESSION_TOKEN = "CSTALK_SESSION_TOKEN"; // 채팅룸에 입장한 클라이언트의 sessionId와 Token를 맵핑한 정보 저장

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoom> hashOpsChatRoom;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    // 모든 채팅방 조회
    public List<ChatRoom> findAllRoom() {
        return hashOpsChatRoom.values(CHAT_ROOMS);
    }

    // 특정 채팅방 조회
    public ChatRoom findRoomById(String id) {
        return hashOpsChatRoom.get(CHAT_ROOMS, id);
    }

    // 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
    public ChatRoom createChatRoom(String name) {
    	this.logger.info("Create chat room - " + name);
        ChatRoom chatRoom = ChatRoom.create(name);
        hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }
    
    // 채팅방 삭제 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
    public void deleteChatRoom(String roomId) {
    	this.logger.info("Delete chat room - " + roomId);
        this.hashOpsChatRoom.delete(CHAT_ROOMS, roomId);
    }
    
    // 커넥션한 유저의 토큰 저장.
    public void setUserToken(String sessionId, String token) {
    	this.logger.info("mapping session - token : " + sessionId + " - " + token);
    	
        hashOpsEnterInfo.put(SESSION_TOKEN, sessionId, token);
    }
    
    // 커넥션한 유저의 토큰 조회.
    public String getUserToken(String sessionId) {
    	return hashOpsEnterInfo.get(SESSION_TOKEN, sessionId);
    }
    
    // 커넥션한 유저 토큰 삭제.
    public void dropUserToken(String sessionId) {
    	this.hashOpsEnterInfo.delete(SESSION_TOKEN, sessionId);
    }

    // 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
    public void setUserJoinInfo(String sessionId, String roomId) {
    	this.logger.info("mapping session - roomId : " + sessionId + " - " + roomId);
    	this.hashOpsEnterInfo.put(JOIN_INFO, sessionId, roomId);
    }

    // 유저 세션으로 입장해 있는 채팅방 ID 조회
    public String getUserJoinRoomId(String sessionId) {
    	String roomId = hashOpsEnterInfo.get(JOIN_INFO, sessionId);
    	this.logger.info("Search chat room[" + sessionId + "] - " + roomId);
        return roomId;
    }

    // 유저 세션정보와 맵핑된 채팅방ID 삭제
    public void removeUserJoinInfo(String sessionId) {
    	this.hashOpsEnterInfo.delete(JOIN_INFO, sessionId);
    }

    // 채팅방 유저수 조회
    public long getUserCount(String roomId) {
        return Long.valueOf(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
    }

    // 채팅방에 입장한 유저수 +1
    public long plusUserCount(String roomId) {
    	return Optional.ofNullable(valueOps.increment(USER_COUNT + "_" + roomId)).orElse(0L);
    }

    // 채팅방에 입장한 유저수 -1
    public long minusUserCount(String roomId) {
    	long result = 0;
    	if(getUserCount(roomId) > 0) {
    		result = Optional.ofNullable(this.valueOps.decrement(USER_COUNT + "_" + roomId)).filter(count -> count > 0).orElse(0L);
    	}
    	return result;
    }
    
    // 채팅방 유저수 삭제
    public void deleteUserCount(String roomId) {
    	this.valueOps.getOperations().delete(USER_COUNT + "_" + roomId);
    }
}











