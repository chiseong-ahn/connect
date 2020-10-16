package com.scglab.connect.services.talk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.adminMenu.emp.EmpDao;
import com.scglab.connect.services.chat.ChatRoomRepository;
import com.scglab.connect.services.common.auth.AuthService;
import com.scglab.connect.services.common.auth.User;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.HttpUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TalkService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatRoomRepository chatRoomRepository;
	
    @Autowired
    private AuthService authServier;
    
	@Autowired
	private TalkDao talkDao;
	
	@Autowired
	private EmpDao empDao;
	
	public Map<String, Object> minwons(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		User user = this.authServier.getUserInfo(request);
		params.put("cid", user.getCid());
		
		Map<String, Object> data = new HashMap<String, Object>();
		HttpUtils httpUtils = new HttpUtils();
		/*
		 * prj: 'sdtalk',
		  appid: 'sdtadm',
		  cid: '1',
		  schemas: 'minwons',
		  schema: 'minwon',
		  loginUser: { id: 1, name: '서울도시가스', auth: 2, state: 0, ts: 4958, speaker: 177 },
		  customerMobileId: '3769',
		  useContractNum: '6004138300',
		  reqName: '황승연',
		  classCode: '68',
		  transfer: false,
		  'cellPhone.num1': '010',
		  'cellPhone.num2': '2706',
		  'cellPhone.num3': '2529',
		  memo: 'test',
		  employeeId: 'csmaster1',
		  chatId: '143'
		 */
		String url = "https://msc-dev.seoulgas.co.kr/proxy/relay/api/chattalk/minwons";
		StringBuffer sb = new StringBuffer();
		sb	.append("reqName=")
			.append("&useContractNum=")
			.append("&classCode=");
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        
        this.logger.debug("url : " + url);
        this.logger.debug("params : " + sb.toString());
        this.logger.debug("headers : " + httpHeaders.toString());
        
		Map<String, Object> result = httpUtils.postApi(url, sb, httpHeaders);
		this.logger.debug(result.toString());
		
		//POST 
		
		
		return data;
	}
	
	
	public Map<String, Object> today(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		User user = this.authServier.getUserInfo(request);
		params.put("cid", user.getCid());
		
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> today = this.talkDao.today(params);
		data.put("today", today);
		
		return data;
	}
	
	public Map<String, Object> state(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		User user = this.authServier.getUserInfo(request);
		params.put("cid", user.getCid());
		params.put("id", user.getEmp());
				
		Map<String, Object> data = new HashMap<String, Object>();
		
		this.logger.debug("상담상태 변경.");
		int result = this.empDao.update(params);
		
		data.put("result", result > 0 ? true : false);

		return data;
	}
	
	public Map<String, Object> spaces(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		User user = this.authServier.getUserInfo(request);
		params.put("cid", user.getCid());
		
		String keyfield = DataUtils.getObjectValue(params, "keyfield", "");
		String keyword = DataUtils.getObjectValue(params, "keyword", "");
		String startDate = DataUtils.getObjectValue(params, "startDate", "");
		String endDate = DataUtils.getObjectValue(params, "endDate", "");
		String state = DataUtils.getObjectValue(params, "state", "");
		
		params.put("keyfield", keyfield);
		params.put("keyword", keyword);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("state", state);
		
		
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.talkDao.spaces(params);
		
		data.put("total", 100);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> space(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		User user = this.authServier.getUserInfo(request);
		params.put("cid", user.getCid());
		
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> space = this.talkDao.speaker(params);
		data.put("space", space);
		
		return data;
	}
	
	public Map<String, Object> speaker(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		User user = this.authServier.getUserInfo(request);
		params.put("cid", user.getCid());
		
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> speaker = this.talkDao.speaker(params);
		data.put("speaker", speaker);
		
		return data;
	}
	
	public Map<String, Object> speaks(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		User user = this.authServier.getUserInfo(request);
		params.put("cid", user.getCid());
				
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.talkDao.speaks(params);
		data.put("total", 100);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> history(Map<String, Object> params, HttpServletRequest request) throws Exception {
		User user = this.authServier.getUserInfo(request);
		params.put("cid", user.getCid());
		
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.talkDao.history(params);
		data.put("total", 100);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> historySpeaks(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		User user = this.authServier.getUserInfo(request);
		params.put("cid", user.getCid());
				
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.talkDao.historySpeaks(params);
		data.put("total", 100);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> updateManager(Map<String, Object> params, HttpServletRequest request) throws Exception {

		User user = this.authServier.getUserInfo(request);
		params.put("cid", user.getCid());
				
		Map<String, Object> data = new HashMap<String, Object>();
		
		this.logger.debug("상담사 변경처리");
		// 상담사 변경처리.
		// CALL update_space(11, ifnull('9', null), '41', '정해관')
		
		data.put("result", true);

		return data;
	}
	
	/**
     * 채팅방에 메시지 발송
     */
    public void sendChatMessage(ChatMessage chatMessage) {
    	
    	
    	if (ChatMessage.MessageType.JOIN.equals(chatMessage.getType())) {
        	// 상담채팅방 입장.
        	chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
        	chatMessage.setSender("[JOIN]");
        	
        	
        } else if (ChatMessage.MessageType.LEAVE.equals(chatMessage.getType())) {
        	// 채팅방 나가기
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
            chatMessage.setSender("[LEAVE]");
            
            
        } else if (ChatMessage.MessageType.PAYLOAD.equals(chatMessage.getType())) {
        	// 채팅방 나가기
            chatMessage.setMessage("상담목록을 새로고침하여 주십시오.");
            chatMessage.setSender("SYSTEM");
            
            
        } else if (ChatMessage.MessageType.WELCOME.equals(chatMessage.getType())) {
        	// 채팅방 나가기
            chatMessage.setMessage("환영합니다.");
            chatMessage.setSender("SYSTEM");
            
            
        } 
        
//        this.logger.debug("topic : " + this.channelTopic.getTopic());
//        this.logger.debug("Message type : " + chatMessage.getType());
//        this.logger.debug("Message : " + chatMessage.getMessage());
//        this.logger.debug("chatMessage : " + chatMessage.toString());
        
        this.redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
        
    }
    
    // 관리자 - 스페이스 입장처리.
    private void joinForAdmin(ChatMessage chatMessage) { 	
    	this.logger.debug("1. [DB]  Customer, Speaker, Space 변경.");
    	//  - CALL reg_customer(:cid, :userno, :name, :telno)
    	
    	this.logger.debug("2. [DB] SpaceSpeaker 테이블에 상담사 등록.");
    	
    	this.logger.debug("3. [Socket] 다른 상담원들에게 스페이스 목록 갱신요청.");
    	
    	this.logger.debug("4. [DB] 이전 대화내용 조회");
    	
    }
    
    // 관리자 - 스페이스 채팅메세지 전달.
    private void sendMessageForAdmin(ChatMessage chatMessage) {
    	this.logger.debug("1. [DB] 채팅 메세지(speak) 데이터 생성.");
    	//	- main speak.speak-making 
    	
    	this.logger.debug("2. [DB] 채팅의 마지막 메세지 번호(SpaceSpeak.lastid) 갱신.");
    	
    	this.logger.debug("3. [DB] 스페이스가 [종료] 또는 [종료대기] 상태일 경우 [진행중]으로 변경(Space.state = 0).");
    	
    	this.logger.debug("4. [Socket] 생성한 채팅 메세지 발송.");
    			
    	this.logger.debug("5. [Server] 채팅 회원이 온라인상태가 아닐경우 Push 발송(Push API 호출).");
    }
    
    // 관리자 - 스페이스 퇴장처리.
    private void leaveSpaceForAdmin(ChatMessage message) {
    	this.logger.debug("1. [DB] 스페이스 상태 변경.");
    	//	- CALL update_space(1, ifnull('9', null), '1', '서울도시가스')
    	
    	this.logger.debug("2. [DB] 변경된 스페이스 조회.");
    	
    	this.logger.debug("3. [Socket] 상담사에 의한 스페이스 변경메세지 전달.");
    }
    
    
}

