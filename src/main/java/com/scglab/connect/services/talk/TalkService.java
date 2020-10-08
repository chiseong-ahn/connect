package com.scglab.connect.services.talk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.chat.ChatMessage;
import com.scglab.connect.services.chat.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TalkService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatRoomRepository chatRoomRepository;
	
	@Autowired
	private TalkDao talkDao;
	
	public Map<String, Object> spaces(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.talkDao.spaces(params);
		data.put("total", 100);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> speaker(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> speaker = this.talkDao.speaker(params);
		data.put("speaker", speaker);
		
		return data;
	}
	
	public Map<String, Object> speaks(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.talkDao.speaks(params);
		data.put("total", 100);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> history(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.talkDao.history(params);
		data.put("total", 100);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> historySpeaks(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.talkDao.historySpeaks(params);
		data.put("total", 100);
		data.put("list", list);
		
		return data;
	}
	
	/**
     * 채팅방에 메시지 발송
     */
    public void sendChatMessage(ChatMessage chatMessage) {
    	
        chatMessage.setUserCount(chatRoomRepository.getUserCount(chatMessage.getRoomId()));
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
            chatMessage.setSender("[알림]");
        } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
            chatMessage.setSender("[알림]");
        }
        
        this.logger.debug("topic : " + this.channelTopic.getTopic());
        this.logger.debug("Message type : " + chatMessage.getType());
        this.logger.debug("Message : " + chatMessage.getMessage());
        
        this.redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
        
        this.logger.debug("메세지 publish complete!");
        
    }
}
