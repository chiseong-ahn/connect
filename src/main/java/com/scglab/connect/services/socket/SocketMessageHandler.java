package com.scglab.connect.services.socket;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.scglab.connect.base.interceptor.CommonInterceptor;
import com.scglab.connect.services.login.Profile;
import com.scglab.connect.services.socket.SocketService.EventName;
import com.scglab.connect.services.socket.SocketService.Target;

import lombok.RequiredArgsConstructor;

@SuppressWarnings({"rawtypes"})
@RequiredArgsConstructor
@Service
public class SocketMessageHandler {
	
	Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);
	
	private final ChannelTopic channelTopic;
	private final RedisTemplate redisTemplate;

	// 전체에게 메시지 보내기.
	public void sendMessageToBroadcast(EventName eventName, Profile profile, Map<String, Object> data) {
		sendMessage(eventName, profile, data, Target.BROADCAST);
	}
	
	// 멤버에게 메시지 보내기.
	public void sendMessageToMember(EventName eventName, Profile profile, Map<String, Object> data) {
		sendMessage(eventName, profile, data, Target.MEMBER);
	}
	
	// 나를 제외하고 메시지 보내기.(고객일 경우 멤버에게 메시지 발송, 멤버일 경우 고객에게 메시지 발송.)
	public void sendMessageToOther(EventName eventName, Profile profile, Map<String, Object> data) {
		sendMessage(eventName, profile, data, profile.getIsCustomer() == 1 ? Target.MEMBER : Target.CUSTOMER);
	}
	
	// 고객에게 메시지 보내기.
	public void sendMessageToCustomer(EventName eventName, Profile profile, Map<String, Object> data) {
		sendMessage(eventName, profile, data, Target.CUSTOMER);
	}
	
	// 본인에게 메시지 보내기.
	public void sendMessageToSelf(EventName eventName, Profile profile, Map<String, Object> data) {
		sendMessage(eventName, profile, data, Target.SELF);
	}
	
	// 대기실에 메시지 보내기.
	public void sendMessageToLobby(EventName eventName, Profile profile) {
		sendMessage(eventName, profile, null, Target.LOBBY);
	}
	
	// 본인에게 에러 메시지 보내기.
	public void sendErrorMessage(Profile profile, String reason) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("reason", reason);
		sendMessage(EventName.ERROR, profile, data, Target.SELF);
	}
	
	// 메시지 보내는 모듈.
	private void sendMessage(EventName eventName, Profile profile, Map<String, Object> data, Target target) {
		SocketData socketData = new SocketData();
		socketData.setEventName(eventName);
		socketData.setRoomId(profile.getRoomId());
		socketData.setTarget(target);
		socketData.setCompanyId(profile.getCompanyId());
		socketData.setData(data);
		socketData.setSessionId(profile.getSessionId());
		this.redisTemplate.convertAndSend(this.channelTopic.getTopic(), socketData);
	}
	
}
