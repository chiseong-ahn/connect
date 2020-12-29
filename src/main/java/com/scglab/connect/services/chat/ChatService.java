package com.scglab.connect.services.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.message.Message;
import com.scglab.connect.services.message.MessageDao;
import com.scglab.connect.services.room.RoomDao;

@Service
public class ChatService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private MessageHandler messageService;
	@Autowired private RoomDao roomDao;
	@Autowired private MessageDao messageDao;
	
	public Map<String, Object> roomHistoryByChatId(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		Map<String, Object> spaceHist = this.roomDao.findRoomHistoryByChatId(params);
		
		params.put("roomId", spaceHist.get("space"));
		params.put("startMessageId", spaceHist.get("startid"));
		params.put("endMessageId", spaceHist.get("endid"));
		List<Message> speaks = this.messageDao.findRangeById(params);  
		
		data.put("spaceHist", spaceHist);
		data.put("speak", speaks);
		
		return data;
	}
	
	
	
}