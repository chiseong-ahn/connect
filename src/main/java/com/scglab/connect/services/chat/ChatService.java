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

import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.message.Message;
import com.scglab.connect.services.message.MessageDao;
import com.scglab.connect.services.room.RoomDao;
import com.scglab.connect.utils.DataUtils;

@Service
public class ChatService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RoomDao roomDao;
	@Autowired
	private MessageDao messageDao;

	/**
	 * 
	 * @Method Name : roomHistoryByChatId
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : chatid를 통한 상담정보 조회. (기간계 -> 중계 -> 상담톡)
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */

	public Map<String, Object> roomHistoryByChatId(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// 접근한 클라이언트 ip
		String ip = request.getHeader("X-FORWARDED-FOR") == null ? request.getRemoteAddr()
				: request.getHeader("X-FORWARDED-FOR");

		// 접근 허용여부.
		boolean isAccess = false;
		this.logger.info("ip : " + ip);
		for (String allowedIp : Constant.accessIpList) {
			if (ip.matches(allowedIp)) {
				isAccess = true;
				break;
			}
		}

		if (!isAccess) {
			// 접근이 불가능할 경우.
			throw new RuntimeException("error.access.denied");
		}

		Map<String, Object> data = new HashMap<String, Object>();

		Map<String, Object> spaceHist = this.roomDao.findRoomHistoryByChatId(params);
		
		List<Map<String, Object>> speaks = new ArrayList<Map<String, Object>>();

		if (spaceHist != null) {
			params.put("roomId", spaceHist.get("space"));
			params.put("startId", spaceHist.get("startid"));
			params.put("endId", spaceHist.get("endid"));
			
			String createDate = DataUtils.getString(spaceHist, "createdate", "") + ".000Z";
			String endDate = DataUtils.getString(spaceHist, "enddate", "") + ".000Z";
			
			spaceHist.put("createdate", createDate);
			spaceHist.put("enddate", endDate);
			
			List<Message> list = this.messageDao.findRangeById(params);

			if (list != null && list.size() > 0) {
				for (Message message : list) {
					Map<String, Object> speak = new HashMap<String, Object>();
					speak.put("id", message.getId());
					speak.put("createdate", message.getCreateDate2()+ ".000Z");
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