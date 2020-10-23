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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.chat.ChatRoomRepository;
import com.scglab.connect.services.common.auth.AuthService;
import com.scglab.connect.services.common.auth.User;
import com.scglab.connect.services.common.service.MessageService;
import com.scglab.connect.services.common.service.PushService;
import com.scglab.connect.services.customer.Customer;
import com.scglab.connect.services.customer.CustomerDao;
import com.scglab.connect.services.external.External;
import com.scglab.connect.services.external.ExternalInc;
import com.scglab.connect.services.external.ExternalScg;
import com.scglab.connect.services.talk.ChatMessage.MessageType;
import com.scglab.connect.utils.DataUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TalkHandler {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatRoomRepository chatRoomRepository;
    
    @Autowired
	private TalkService talkService;
    
    @Autowired
	private TalkDao talkDao;
    
    @Autowired
	private AuthService authService;
    
    @Autowired
    private CustomerDao customerDao;
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private PushService pushService;
    
    
    
    /**
     * 
     * @Method Name : connected
     * @작성일 : 2020. 10. 20.
     * @작성자 : anchiseong
     * @변경이력 : 
     * @Method 설명 : 소켓 연결.
     * @param user
     */
    public void connected(User user) {
    	
    }
    
    public String getLobbySpace(int cid) {
    	return Constant.SPACE_LOBBY + "_" + cid;
    }
    
    
    
    
    /**
     * 
     * @Method Name : join
     * @작성일 : 2020. 10. 20.
     * @작성자 : anchiseong
     * @변경이력 : 
     * @Method 설명 : 스페이스 조인(=구독)
     * @param user
     * @param roomId
     */
	public void join(User user, String roomId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		this.logger.debug("---------------------------------------------------------------------");

		ChatMessage chatMessage;
		
		this.logger.debug("roomId : " + roomId);
		this.logger.debug("lobby : " + getLobbySpace(user.getCid()));
		
		if(!roomId.equals(getLobbySpace(user.getCid()))) {
			
			// 대기실이 아닐경우 
			
			this.logger.debug("[DB] 스페이스 조인처리.");
			params.put("spaceId", roomId);
			params.put("speaker", user.getSpeaker());
			this.talkDao.join(params);
			
			// 채팅방 참여자들에게 Join 메세지 전송.
			chatMessage = new ChatMessage();
			chatMessage.setType(MessageType.JOIN);
			chatMessage.setRoomId(roomId);
			chatMessage.setMsg(user.getSpeaker() + "님이 연결되었습니다.");
			chatMessage.setUserCount(this.chatRoomRepository.getUserCount(roomId));
			sendPayload(chatMessage);
			
			params.put("emp", user.getEmp());
			Map<String, Object> space = this.talkDao.space(params);
			
			if(user.getEmp() > 0) {	// 관리자(상담사)일 경우.
				
				// 상담사가 자기상담에 Join하였다면 다른 상담원들에게 상담목록을 갱신하도록 요청메세지 전송.
				this.logger.debug("Step. [Socket] 다른 상담원들에게 스페이스 목록 갱신요청.");
				sendReloadMessage(user);
				
				// 고객 기본정보 전달.
				customer(user, roomId);
				
				// 상담대화내용 전달.
				speaks(user, roomId);
				
				// 이전상담 목록 전달.
				prehistory(user, roomId);
				
				this.logger.debug("Step. [DB] 이전 메세지 읽음 처리.");
				params.put("startid", DataUtils.getLong(space, "oldid", 0));
		    	params.put("lastid", DataUtils.getLong(space, "lastid", 0));
		    	params.put("space", roomId);
		    	params.put("speaker", user.getSpeaker());
		    	this.talkDao.minusNotread(params);
		    	
		    	this.logger.debug("Step. [DB] 스페이스에 상담사 추가.");
		    	this.talkDao.updateSpaceSpeaker(params);
		    	
		    	this.logger.debug("Step. [Socket] 이전 메세지 읽음 알림.");
		    	chatMessage = new ChatMessage();
				chatMessage.setType(MessageType.READSEMP);
				chatMessage.setSender("SYSTEM");
				chatMessage.setRoomId(roomId);
				chatMessage.setMsg(this.messageService.getMessage("talk.message.reads"));
				sendPayload(chatMessage);
				
			}else {	// 회원일 경우.
				
				this.logger.debug("채팅방이 최초 또는 종료상태일 경우 시작메세지를 전송.");
				int state = DataUtils.getInt(space, "state", 0);
				this.logger.debug("space state : " + state);
				if(state == 0 || state == 2) {
					params.put("speaker", 0);
					params.put("sysmsg", 1);
					params.put("isemp", 1);
					
					this.logger.debug("Step. 시작메세지 전달.");
					
					// 외부 연동클래스.
					External external = null;
					if(user.getCid() == 1) {		// 서울도시가스일 경우.
						external = new ExternalScg();
					
					}else if(user.getCid() == 2) {	// 인천도시가스일 경우.
						external = new ExternalInc();
					}
					int isWorkType = external.isWorking();
					
					if(isWorkType == 1) {	// 근무중일 경우.
						
						// 로비에 상담가능 상담사 존재확인.
						isWorkType = this.chatRoomRepository.getUserCount(getLobbySpace(user.getCid())) > 0 ? 0 : 1;
						
					}
					
					String startMessage = this.messageService.getMessage("talk.message.start.type" + isWorkType);
					chatMessage = new ChatMessage();
					chatMessage.setType(MessageType.READS);
					chatMessage.setSender("SYSTEM");
					chatMessage.setRoomId(roomId);
					chatMessage.setMsg(startMessage);
					sendPayload(chatMessage);
				}
				
				this.logger.debug("Step. [DB] 채팅방 상태(회원접속상태)를 온라인으로 변경한다.");
				params.put("isonline", 1);
				params.put("space", roomId);
				this.talkDao.updateOnline(params);
					
			}			
		}else {
			// 로비일 경우. 
			this.logger.debug("[Join] user : " + user.toString());
			if(user.getEmp() > 0) {
				sendReadyRoomCountMessage(user);
				sendReloadMessage(user);
			}
			
		}
	}
	
	
	
	/**
	 * 
	 * @Method Name : assign
	 * @작성일 : 2020. 10. 21.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 대기상담 할당.
	 * @param user
	 * @param data
	 */
	public void assign(User user, ChatMessage data) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		this.logger.debug("Step. [DB] 대기상담 카운트 조회.");
		int count = this.talkDao.selectReadySpaceCount(params);
		if(count > 0) {
			
			this.logger.debug("Step. [DB] 대기상담 정보 조회");
			Map<String, Object> readySpace = this.talkDao.selectReadySpace(params);
			if(readySpace != null) {
			
				long roomId = DataUtils.getLong(readySpace, "space", 0);
				
				params.put("acting", 0);
				params.put("space", roomId);
				params.put("emp", user.getEmp());
				
				this.logger.debug("Step. [DB] 대기상담 할당처리.");
				this.talkDao.updateSpace(params);
				
				this.logger.debug("Step. [DB] 웰컴 메세지 생성.");
				String startMessage = this.messageService.getMessage("talk.message.welcome");
				
		    	Speak speak = makeSpeak(user.getCid(), (int)roomId, user.getSpeaker(), 0, 0, startMessage, null, 0, (user.getEmp() > 0 ? 1 : 0), null);
		    	this.logger.debug("Speak : " + speak.toString());
				
				ChatMessage chatMessage = new ChatMessage();
				chatMessage.setType(MessageType.MESSAGE);
				chatMessage.setSender(user.getEmpno());
				chatMessage.setRoomId(Long.toString(roomId));
				chatMessage.setMsg(startMessage);
				
				this.logger.debug("Step. [Socket] 고객에게 웰컴 메세지 전송.");
				sendPayload(chatMessage);
				
				this.logger.debug("Step. [Socket] 로비에 있는 상담사들에게 대기상담 건수 및 목록 갱신 메세지 전송.");
				sendReadyRoomCountMessage(user);
				sendReloadMessage(user);
				
				this.logger.debug("Step. [Socket] 해당 상담사에게 할당완료 알림.");
				Map<String, Object> data2 = new HashMap<String, Object>();
				data2.put("roomId", roomId);
				chatMessage = new ChatMessage();
				chatMessage.setType(MessageType.ASSIGNED);
				chatMessage.setSender(user.getEmpno());
				chatMessage.setRoomId(getLobbySpace(user.getCid()));
				chatMessage.setMsg(this.messageService.getMessage("assigned"));
				chatMessage.setData(data2);
				sendPayload(chatMessage);
			}
		}
	}
	
	/**
     * 
     * @Method Name : message
     * @작성일 : 2020. 10. 20.
     * @작성자 : anchiseong
     * @변경이력 : 
     * @Method 설명 : 메세지 수신에 대한 처리.
     * @param user
     * @param data
	 * @throws JsonProcessingException 
     */
    public void message(User user, ChatMessage data) {
    	this.logger.debug("---------------------------------------------------------------------");

    	this.logger.debug("Step. [DB] 메세지 생성.");
    	Speak speak = makeSpeak(user.getCid(), Integer.parseInt(data.getRoomId()), user.getSpeaker(), data.getMtype(), data.getSysmsg(), data.getMsg(), null, 0, (user.getEmp() > 0 ? 1 : 0), null);
    	this.logger.debug("Speak : " + speak.toString());

    	data.setSpeaker(user.getSpeaker());
    	
    	this.logger.debug("Step. [Socket] 메세지 전달.");
    	sendPayload(data);
    	
    	// 고객이 오프라인 and 상담원의 메세지일 경우.
    	if(speak.isIsonline() == false && speak.isIsemp() == true && speak.getOnlyadm() == 0) {
    		// 푸시발송.
    		this.logger.debug("Step. [PUSH] 상담원이 메세지를 보냈을때에 회원이 오프라인이라면 PUSH 메세지를 발송한다.");
    		try {
    			Map<String, Object> params = new HashMap<String, Object>();
    			params.put("space", data.getRoomId());
    			Customer customer = this.customerDao.selectCustomerInSpace(params);
    			
    			this.logger.debug("Step. [PUSH] 고객이 온라인상태가 아닐경우 푸시메세지 발송");
    			//this.pushService.sendPush(Integer.parseInt(DataUtils.getString(customer, "userno", "0")), this.messageService.getMessage("talk.message.push"));
    		}catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    	
    	// 고객 메세지일 경우.
    	if(user.getUserno() > 0) {
    		// 로비에 있는 상담사들에게 대기상담 건수 및 목록 갱신 메세지 전달.
			sendReadyRoomCountMessage(user);
			sendReloadMessage(user);
    	}
    }
    
    /**
     * 
     * @Method Name : makeSpeak
     * @작성일 : 2020. 10. 22.
     * @작성자 : anchiseong
     * @변경이력 : 
     * @Method 설명 : 상담 대화 생성.
     * @param cid
     * @param space
     * @param speaker 
     * @param mtype
     * @param sysmsg
     * @param message
     * @param morp
     * @param onlyadm
     * @param isemp
     * @param msgname
     * @return
     */
    public Speak makeSpeak(int cid, int space, int speaker, int mtype, int sysmsg, String message, String morp, int onlyadm, int isemp, String msgname){
    	this.logger.debug("Step. [DB] 메세지 생성.");
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("cid", cid);
    	params.put("spaceId", space);
    	params.put("speaker", speaker);
    	params.put("mtype", mtype);		// 0:일반,1:img,2:mv,3:attach, 4:link
    	params.put("sysmsg", sysmsg);	// 0:일반 대화 메세지, 1:시스템 메세지
    	params.put("message", message);	// 메세지.
    	params.put("morp", morp);		// 형태소 분석결
    	params.put("onlyadm", onlyadm);	// sys msg only adm (0:false, 1:noti, 2:warn)
    	params.put("isemp", isemp);		// 직원 작성여부.
    	params.put("msgname", msgname);	// 첨부파일/링크이름.
    	//CALL make_speak(#{cid}, #{spaceId}, #{speaker}, #{mtype}, #{sysmsg}, #{message}, #{morp}, #{onlyadm}, #{isemp}, #{msgname})
    	Speak speak = this.talkDao.makeMessage(params);
    	
    	return speak;
    }
	
	/**
	 * 
	 * @Method Name : leave
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 대화방 나가기.
	 * @param user
	 * @param roomId
	 */
	public void leave(User user, ChatMessage message) {
		this.logger.debug("[leave] user : " + user);
    	this.logger.debug("[leave] message : " + message);
    	sendReloadMessage(user);
		
	}
	
	/**
	 * 
	 * @Method Name : end
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 상담 종료.
	 * @param user
	 * @param message
	 */
	public void end(User user, ChatMessage message) {
		this.logger.debug("[End] user : " + user);
    	this.logger.debug("[End] message : " + message);
    	
		if(user.getEmp() > 0) {	// 상담사의 상담종료일 경우.
			
			
			
		}else {	// 고객의 상담종료일 경우.
			this.logger.debug("Step. [DB] 채팅방 바로 종료처리.");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("acting", "2");
			params.put("space", message.getRoomId());
			this.talkDao.updateSpace(params);
			
			this.logger.debug("Step. [DB] 다른 상담원들에게 상담목록 갱신요청 메세지 전송.");
			sendPayload(MessageType.RELOAD, getLobbySpace(user.getCid()), user.getEmpno(), this.messageService.getMessage("talk.message.reload"));
			
			// 로비에 있는 상담사들에게 대기상담 건수 및 목록 갱신 메세지 전달.
			sendReadyRoomCountMessage(user);
			sendReloadMessage(user);
		}
	}
	
	/**
	 * 
	 * @Method Name : prehistory
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 이전 상담내역 조회.
	 * @param user
	 * @param message
	 */
	public void prehistory(User user, String roomId) {
		this.logger.debug("[prehistory] user : " + user);
    	this.logger.debug("[prehistory] roomId : " + roomId);
    	
		this.logger.debug("Step. [DB] 이전 상담내역 조회");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("spaceId", roomId);
		List<Map<String, Object>> histories = this.talkDao.history(params);
		
		this.logger.debug("Step. [Socket] 이전 상담내역 데이터 전달.");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("histories", histories);
		sendPayload(MessageType.PREHISTORY, roomId, user.getEmpno(), "", data);
	}
	
	public void customer(User user, String roomId) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("space", roomId);
		
		this.logger.debug("Step. [DB] 스페이스에서 참여되어있는 고객정보 조회.");
		Customer customer = this.customerDao.selectCustomerInSpace(params);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("customer", customer);
		
		this.logger.debug("Step. [Socket] 고객정보 전달.");
		sendPayload(MessageType.CUSTOMER_INFO, roomId, user.getEmpno(), "", data);
	}
	
	
	/**
	 * 
	 * @Method Name : speaks
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 상담 대화내용 조회
	 * @param user
	 * @param message
	 */
	public void speaks(User user, String roomId) {
		this.logger.debug("[speaks] user : " + user);
    	this.logger.debug("[speaks] roomId : " + roomId);
    	
		this.logger.debug("Step. [DB] 상담 대화내용 조회");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("spaceId", roomId);
		//List<Map<String, Object>> speaks = this.talkDao.speaks(params);
		List<Speak> speaks = this.talkDao.speaks(params);
		
		this.logger.debug("Step. [Socket] 상담 대화내용 데이터 전달.");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("speaks", speaks);
		sendPayload(MessageType.SPEAKS, roomId, user.getEmpno(), "", data);
	}
	
	/**
	 * 
	 * @Method Name : disconnected
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 소켓 연결해제.
	 * @param user
	 * @param roomId
	 */
	public void disconnected(User user, String roomId) {
		if(user.getUserno() > 0) {  // 고객일 경우
			// 채팅방을 오프라인 상태로 변경한다.
			this.logger.debug("Step. [DB] 채팅방 상태(회원접속상태)를 오프라인으로 변경한다.");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("isonline", 0);
			params.put("space", roomId);
			this.talkDao.updateOnline(params);
			
			this.logger.debug("Step. [DB] 다른 상담원들에게 상담목록 갱신요청 메세지 전송.");
			sendPayload(MessageType.RELOAD, roomId, user.getEmpno(), this.messageService.getMessage("talk.message.reload"));
			sendReloadMessage(user);
		}
	}
	
	/**
     * 
     * @Method Name : sendReloadMessage
     * @작성일 : 2020. 10. 22.
     * @작성자 : anchiseong
     * @변경이력 : 
     * @Method 설명 : 상담목록 갱신요청 메세지 전송.
     * @param user
     */
    public void sendReloadMessage(User user) {
    	this.logger.debug("Step. [DB] 다른 상담원들에게 상담목록 갱신요청 메세지 전송.");
    	sendPayload(MessageType.RELOAD, getLobbySpace(user.getCid()), user.getEmpno(), this.messageService.getMessage("talk.message.reload"));
    }
    
    /**
	 * 
	 * @Method Name : sendReadyRoomCountMessage
	 * @작성일 : 2020. 10. 22.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 대기상담 건수 알림.
	 * @param user
	 * @param data
	 */
	public void sendReadyRoomCountMessage(User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		int count = this.talkDao.selectReadySpaceCount(params);
		if(count > 0) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("count", count);
			
			ChatMessage chatMessage = new ChatMessage();
	    	chatMessage.setType(MessageType.READYCOUNT);
	    	chatMessage.setCid(user.getCid());
	    	chatMessage.setRoomId(getLobbySpace(user.getCid()));
	    	chatMessage.setAppid("sdtadm");
	    	chatMessage.setMsg("");
	    	chatMessage.setData(result);
	    	
	    	this.logger.debug("Step. [Socket] 상담원들에게 대기상담 알림.");
	    	sendPayload(chatMessage);
		}
	}
	
	
	// 메세지 발송 모듈.
	public void sendPayload(MessageType type, String roomId, String sender, String message) {
		Map<String, Object> data = null;
		sendPayload(type, roomId, sender, message, data);
	}
	
	// 메세지 발송 모듈.
	public void sendPayload(MessageType type, String roomId, String sender, String message, Map<String, Object> data) {
		long count = this.chatRoomRepository.getUserCount(roomId);
		sendPayload(type, roomId, sender, message, count, data);
	}
	
	public void sendPayload(MessageType type, String roomId, String sender, String message, long count, Map<String, Object> data) {
		ChatMessage chatMessage = new ChatMessage(type, roomId, sender, message, count, data);
		sendPayload(chatMessage);
	}

	// 메세지 발송 모듈.
	public void sendPayload(ChatMessage message) {
		this.redisTemplate.convertAndSend(channelTopic.getTopic(), message);
	}
}