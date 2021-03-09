package com.scglab.connect.services.socket;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.login.Profile;
import com.scglab.connect.utils.SerializeUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatRoomRepository {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// Redis CacheKeys
	private static final String CHAT_ROOMS = "CSTALK_CHATROOM"; // 채팅룸 저장
	public static final String USER_COUNT = "CSTALK_USERCOUNT"; // 채팅룸에 입장한 클라이언트 수 저장
	public static final String JOIN_INFO = "CSTALK_JOIN_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장
	public static final String JOIN_MEMBER = "CSTALK_JOIN_MEMBER"; // 채팅룸에 입장한 채팅룸 id와 멤버 sessionId 저장
	public static final String JOIN_CUSTOMER = "CSTALK_JOIN_CUSTOMER"; // 채팅룸에 입장한 채팅룸 id와 고객 sessionId 저장
	public static final String CSTALK_SESSION_PROFILE = "CSTALK_SESSION_PROFILE"; // 채팅룸에 입장한 클라이언트의 sessionId와 Token를
																					// 맵핑한 정보 저장

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
		this.logger.debug("Create chat room - " + name);
		ChatRoom chatRoom = ChatRoom.create(name);
		hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
		return chatRoom;
	}

	// 채팅방 삭제 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
	public void deleteChatRoom(String roomId) {
		this.logger.debug("Delete chat room - " + roomId);
		this.hashOpsChatRoom.delete(CHAT_ROOMS, roomId);
	}

	// 커넥션한 유저 프로필 저장.
	public void setProfileBySessionId(String sessionId, Profile profile) {
		this.logger.debug("mapping session - token : " + sessionId + " - " + profile.toString());
		String strProfile = SerializeUtils.serialize(profile);
		this.logger.debug("setProfileBySessionId - strProfile : " + strProfile);
		this.hashOpsEnterInfo.put(CSTALK_SESSION_PROFILE, sessionId, strProfile);
	}

	// 커넥션한 유저 프로필 조회.
	public Profile getProfileBySessionId(String sessionId) {
		String strProfile = hashOpsEnterInfo.get(CSTALK_SESSION_PROFILE, sessionId);
		this.logger.debug("getProfileBySessionId - strProfile : " + strProfile);

		if (strProfile == null) {
			return null;
		}
		return (Profile) SerializeUtils.deserialize(strProfile);
	}

	// 커넥션한 유저 토큰 삭제.
	public void dropProfileBySessionId(String sessionId) {
		this.hashOpsEnterInfo.delete(CSTALK_SESSION_PROFILE, sessionId);
	}

	// 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
	public void setUserJoinInfo(String sessionId, String roomId) {
		this.logger.debug("mapping session - roomId : " + sessionId + " - " + roomId);
		this.hashOpsEnterInfo.put(JOIN_INFO, sessionId, roomId);
	}

	public void setMemberJoin(String roomId, String sessionId) {
		this.hashOpsEnterInfo.put(JOIN_MEMBER, roomId, sessionId);
	}

	public String getMemberSessionIdByRoomId(String roomId) {
		return this.hashOpsEnterInfo.get(JOIN_MEMBER, roomId);
	}

	public void setCustomerJoin(String roomId, String sessionId) {
		this.hashOpsEnterInfo.put(JOIN_CUSTOMER, roomId, sessionId);
	}

	public void removeMemberJoin(String roomId) {
		this.hashOpsEnterInfo.delete(JOIN_MEMBER, roomId);
	}

	public String getCustomerSessionIdByRoomId(String roomId) {
		return this.hashOpsEnterInfo.get(JOIN_CUSTOMER, roomId);
	}

	public void removeCustomerJoin(String roomId) {
		this.hashOpsEnterInfo.delete(JOIN_CUSTOMER, roomId);
	}

	// 유저 세션으로 입장해 있는 채팅방 ID 조회
	public String getUserJoinRoomId(String sessionId) {
		String roomId = hashOpsEnterInfo.get(JOIN_INFO, sessionId);
		this.logger.debug("Search chat room[" + sessionId + "] - " + roomId);
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
		if (getUserCount(roomId) > 0) {
			result = Optional.ofNullable(this.valueOps.decrement(USER_COUNT + "_" + roomId)).filter(count -> count > 0)
					.orElse(0L);
		}
		return result;
	}

	// 채팅방 유저수 삭제
	public void deleteUserCount(String roomId) {
		this.valueOps.getOperations().delete(USER_COUNT + "_" + roomId);
	}
}
