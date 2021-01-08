package com.scglab.connect.services.chat;

import java.util.ArrayList;
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
		List<Map<String, Object>> speaks = new ArrayList<Map<String, Object>>();
		
		if(spaceHist != null) {
			params.put("roomId", spaceHist.get("space"));
			params.put("startId", spaceHist.get("startid"));
			params.put("endId", spaceHist.get("endid"));
			List<Message> list = this.messageDao.findRangeById(params);
			
			if(list != null && list.size() > 0) {
				for(Message message : list) {
					Map<String, Object> speak = new HashMap<String, Object>();
					speak.put("id", message.getId());
					speak.put("createdate", message.getCreateDate());
					speak.put("mtype", message.getMessageType());
					speak.put("msg", message.getMessage());
					speak.put("msgname", message.getSpeakerName());
					speak.put("isemp", message.getIsEmployee());
					
					speaks.add(speak);
				}
			}
		}
		
		data.put("spacehist", spaceHist == null ? new HashMap<String, Object>() : spaceHist);
		data.put("speak", speaks);
		
		return data;
	}
	
	
	
}