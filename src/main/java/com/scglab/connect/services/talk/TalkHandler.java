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
import com.scglab.connect.services.adminmenu.automsg.Automsg;
import com.scglab.connect.services.adminmenu.automsg.AutomsgDao;
import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.auth.User;
import com.scglab.connect.services.common.service.MessageService;
import com.scglab.connect.services.common.service.PushService;
import com.scglab.connect.services.company.external.ICompany;
import com.scglab.connect.services.customer.Customer;
import com.scglab.connect.services.customer.CustomerDao;
import com.scglab.connect.services.login.Profile;
import com.scglab.connect.services.talk.MessageVo.MsgType;
import com.scglab.connect.services.talk.TalkMessage.MessageType;
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
    private CommonService commonService;
    
    @Autowired
	private TalkDao talkDao;
    
    @Autowired
    private CustomerDao customerDao;
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private PushService pushService;
    
    @Autowired
    private AutomsgDao automsgDao;
    
    /**
     * 
     * @Method Name : getLobbySpace
     * @작성일 : 2020. 11. 6.
     * @작성자 : anchiseong
     * @변경이력 : 
     * @Method 설명 : cid에 맞는 Lobby space명 반환 
     * @param cid
     * @return
     */
    public String getLobbySpace(String companyId) {
    	return "LOBBY" + companyId;
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
	public void join(Profile profile, String roomId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		this.logger.debug("---------------------------------------------------------------------");

		TalkMessage talkMessage;
		
		this.logger.debug("roomId : " + roomId);
		this.logger.debug("lobby : " + getLobbySpace(profile.getCompanyId()));
		
		
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("test1", "111");
//		data.put("test2", "222");
//		data.put("test3", "333");
//		sendData(MsgType.JOINED, roomId, user.getCid(), user.getEmp(), 0, data);
				
		
		if(!roomId.equals(getLobbySpace(profile.getCompanyId()))) {
			
			// 대기실이 아닐경우 
			
			this.logger.debug("[DB] 스페이스 조인처리.");
			params.put("spaceId", roomId);
			params.put("speaker", profile.getSpeakerId());
			this.talkDao.join(params);
			
			// 채팅방 참여자들에게 Join 메세지 전송.
			talkMessage = new TalkMessage();
			talkMessage.setType(MessageType.JOIN);
			talkMessage.setRoomId(roomId);
			talkMessage.setMsg(profile.getSpeakerId() + "님이 연결되었습니다.");
			talkMessage.setUserCount(this.chatRoomRepository.getUserCount(roomId));
			sendPayload(talkMessage);
			
			params.put("emp", profile.getId());
			Map<String, Object> space = this.talkDao.space(params);
			this.logger.debug("space : " + space);
			
			if(profile.getIsAdmin() > 0) {	// 관리자(상담사)일 경우.
				
				this.logger.debug("Step. [Socket] 상담사가 자기 상담에 Join하였다면 다른 상담원들에게 상담목록 갱신요청.");
				if(DataUtils.getLong(space, "emp", 0) == profile.getId()){
					sendReloadMessage(profile);
				}
				
				// 고객 기본정보 전달.
				customer(profile, roomId);
				
				// 상담대화내용 전달.
				speaks(profile, roomId);
				
				// 이전상담 목록 전달.
				prehistory(profile, roomId);
				
				this.logger.debug("Step. [DB] 이전 메세지 읽음 처리.");
				params.put("startid", DataUtils.getLong(space, "oldid", 0));
		    	params.put("lastid", DataUtils.getLong(space, "lastid", 0));
		    	params.put("space", roomId);
		    	params.put("speaker", profile.getSpeakerId());
		    	//this.talkDao.minusNotread(params);
		    	
		    	this.logger.debug("Step. [DB] 스페이스에 상담사 추가.");
		    	//this.talkDao.updateSpaceSpeaker(params);
		    	
		    	this.logger.debug("Step. [Socket] 이전 메세지 읽음 알림.");
		    	talkMessage = new TalkMessage();
				talkMessage.setType(MessageType.READSEMP);
				talkMessage.setSender("SYSTEM");
				talkMessage.setRoomId(roomId);
				talkMessage.setMsg(this.messageService.getMessage("talk.reads"));
				sendPayload(talkMessage);
				
			}else {	// 회원일 경우.
				
				this.logger.debug("채팅방이 최초 또는 종료상태일 경우 시작메세지를 전송.");
				int state = DataUtils.getInt(space, "state", 0);
				this.logger.debug("space state : " + state);
				if(state == 0 || state == 2) {
					params.put("speaker", 0);
					params.put("sysmsg", 1);
					params.put("isemp", 1);
					
					this.logger.debug("Step. 시작메세지 전달.");
				
					this.logger.debug("Step. [Socket] 상담 근수요일 및 근무시간이 아닐경우 안내 메세지 전송.");
			    	ICompany company = this.commonService.getCompany(profile.getCompanyId());
			    	
					// 근무상태 (1-근무 중, 2-근무 외 시간, 3-점심시간.
			    	int isWorkType = company.isWorking();
			    	if(isWorkType == 1) {
			    		// 로비에 상담가능 상담사 존재확인.
			    		isWorkType = this.chatRoomRepository.getUserCount(getLobbySpace(profile.getCompanyId())) > 0 ? 0 : 1;
			    	}
			    	
					String startMessage = this.messageService.getMessage("talk.start.type" + isWorkType);
					talkMessage = new TalkMessage();
					talkMessage.setType(MessageType.MESSAGE);
					talkMessage.setSender("SYSTEM");
					talkMessage.setRoomId(roomId);
					talkMessage.setMsg(startMessage);
					sendPayload(talkMessage);
					
					sendReloadMessage(profile);
				}
				
				this.logger.debug("Step. [DB] 채팅방 상태(회원접속상태)를 온라인으로 변경한다.");
				params.put("isonline", 1);
				params.put("space", roomId);
				this.talkDao.updateOnline(params);
					
			}			
		}else {
			// 로비일 경우. 
			this.logger.debug("[Join] profile : " + profile.toString());
			if(profile.getIsAdmin() > 0) {
				sendReadyRoomCountMessage(profile);
				sendReloadMessage(profile);
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
	public void assign(Profile profile, TalkMessage data) {
		
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
				params.put("emp", profile.getId());
				
				this.logger.debug("Step. [DB] 대기상담 할당처리.");
				this.talkDao.updateSpace(params);
				
				this.logger.debug("Step. [DB] 고객에게 웰컴 메세지 조회.");
				params.put("cid", profile.getCompanyId());
				params.put("type", 0);
				Automsg welcomeMsg = this.automsgDao.selectRandomOne(params);
				
				Speak speak = makeSpeak(profile.getCompanyId(), (int)roomId, profile.getSpeakerId(), 0, 0, welcomeMsg.getMsg(), null, 0, (profile.getIsAdmin() == 1 ? 1 : 0), null);
		    	this.logger.debug("Speak : " + speak.toString());
				
		    	this.logger.debug("Step. [Socket] 고객에게 웰컴 메세지 전송.");
				sendPayload(MessageType.MESSAGE, speak);
				
				this.logger.debug("Step. [Socket] 로비에 있는 상담사들에게 대기상담 건수 및 목록 갱신 메세지 전송.");
				sendReadyRoomCountMessage(profile);
				sendReloadMessage(profile);
				
				this.logger.debug("Step. [Socket] 해당 상담사에게 할당완료 알림.");
				Map<String, Object> data2 = new HashMap<String, Object>();
				data2.put("roomId", roomId);
				TalkMessage talkMessage = new TalkMessage();
				talkMessage.setType(MessageType.ASSIGNED);
				talkMessage.setSender(profile.getLoginName());
				talkMessage.setRoomId(getLobbySpace(profile.getCompanyId()));
				talkMessage.setMsg(this.messageService.getMessage("assigned"));
				talkMessage.setOnlyadm(1);
				talkMessage.setData(data2);
				sendPayload(talkMessage);
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
    public void message(Profile profile, TalkMessage data) {
    	this.logger.debug("---------------------------------------------------------------------");

    	this.logger.debug("Step. [DB] 메세지 생성.");
    	Speak speak = makeSpeak(profile.getCompanyId(), Integer.parseInt(data.getRoomId()), profile.getSpeakerId(), data.getMtype(), data.getSysmsg(), data.getMsg(), null, 0, (profile.getIsAdmin() == 1 ? 1 : 0), null);
    	this.logger.debug("Speak : " + speak.toString());

    	data.setUserno(profile.getId());
    	 
    	
    	this.logger.debug("Step. [Socket] 메세지 전달.");
    	sendPayload(MessageType.MESSAGE, speak);
    	
    	// 고객이 오프라인 and 상담원의 메세지일 경우.
    	if(speak.isIsonline() == false && speak.isIsemp() == true && speak.getOnlyadm() == 0) {
    		// 푸시발송.
    		this.logger.debug("Step. [PUSH] 상담원이 메세지를 보냈을때에 회원이 오프라인이라면 PUSH 메세지를 발송한다.");
    		try {
    			Map<String, Object> params = new HashMap<String, Object>();
    			params.put("space", data.getRoomId());
    			Customer customer = this.customerDao.selectCustomerInSpace(params);
    			
    			this.logger.debug("Step. [PUSH] 고객이 온라인상태가 아닐경우 푸시메세지 발송");
    			//this.pushService.sendPush(Integer.parseInt(DataUtils.getString(customer, "userno", "0")), this.messageService.getMessage("talk.push"));
    		}catch(Exception e) {
    			e.printStackTrace();
    		}
    	}

    	// 고객 메세지일 경우.
    	if(profile.getIsAdmin() == 0) {
    		// 로비에 있는 상담사들에게 대기상담 건수 및 목록 갱신 메세지 전달.
			sendReadyRoomCountMessage(profile);
			sendReloadMessage(profile);
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
    public Speak makeSpeak(String cid, int space, int speaker, int mtype, int sysmsg, String message, String morp, int onlyadm, int isemp, String msgname){
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
	public void leave(Profile profile, TalkMessage message) {
		this.logger.debug("[leave] profile : " + profile);
    	this.logger.debug("[leave] message : " + message);
    	sendReloadMessage(profile);
		
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
	public void end(Profile profile, TalkMessage message) {
		this.logger.debug("[End] profile : " + profile);
    	this.logger.debug("[End] message : " + message);
    	
    	String endMessage = this.messageService.getMessage("talk.end");
    	Speak speak = makeSpeak(profile.getCompanyId(), Integer.parseInt(message.getRoomId()), profile.getSpeakerId(), 0, 1, endMessage, null, 0, profile.getIsAdmin() == 1 ? 1 : 0, null);
    	this.logger.debug("Speak : " + speak.toString());
    	
		// 채팅방 참여자들에게 상담종료 메세지 전달.
		TalkMessage talkMessage = new TalkMessage();
		talkMessage.setType(MessageType.END);
		talkMessage.setRoomId(message.getRoomId());
		talkMessage.setSysmsg(1);
		talkMessage.setMsg(endMessage);
		
		//sendPayload(talkMessage);
		sendPayload(MessageType.END, speak);
			
		this.logger.debug("Step. [DB] 채팅방 바로 종료처리.");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acting", "2");
		params.put("space", message.getRoomId());
		this.talkDao.updateSpace(params);
		
		this.logger.debug("Step. [DB] 다른 상담원들에게 상담목록 및 대기건수 갱신요청 메세지 전송.");
		sendReadyRoomCountMessage(profile);
		sendReloadMessage(profile);
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
	public void prehistory(Profile profile, String roomId) {
		this.logger.debug("[prehistory] profile : " + profile);
    	this.logger.debug("[prehistory] roomId : " + roomId);
    	
		this.logger.debug("Step. [DB] 이전 상담내역 조회");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", profile.getCompanyId());
		params.put("spaceId", roomId);
		List<Map<String, Object>> histories = this.talkDao.history(params);
		
		this.logger.debug("Step. [Socket] 이전 상담내역 데이터 전달.");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("histories", histories);
		sendPayload(MessageType.PREHISTORY, roomId, profile.getLoginName(), "", data);
	}
	
	/**
	 * 
	 * @Method Name : customer
	 * @작성일 : 2020. 11. 6.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객 정보 조회.
	 * @param user
	 * @param roomId
	 */
	public void customer(Profile profile, String roomId) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("space", roomId);
		
		this.logger.debug("Step. [DB] 스페이스에서 참여되어있는 고객정보 조회.");
		Customer customer = this.customerDao.selectCustomerInSpace(params);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("customer", customer);
		
		this.logger.debug("Step. [Socket] 고객정보 전달.");
		sendPayload(MessageType.CUSTOMER_INFO, roomId, profile.getLoginName(), "", data);
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
	public void speaks(Profile profile, String roomId) {
		this.logger.debug("[speaks] profile : " + profile);
    	this.logger.debug("[speaks] roomId : " + roomId);
    	
		this.logger.debug("Step. [DB] 상담 대화내용 조회");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", profile.getCompanyId());
		params.put("spaceId", roomId);
		//List<Map<String, Object>> speaks = this.talkDao.speaks(params);
		List<Speak> speaks = this.talkDao.speaks(params);
		
		this.logger.debug("Step. [Socket] 상담 대화내용 데이터 전달.");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("speaks", speaks);
		sendPayload(MessageType.SPEAKS, roomId, profile.getLoginName(), "", data);
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
	public void disconnected(Profile profile, String roomId) {
		if(profile.getIsAdmin() == 0) {  // 고객일 경우
			// 채팅방을 오프라인 상태로 변경한다.
			this.logger.debug("Step. [DB] 채팅방 상태(회원접속상태)를 오프라인으로 변경한다.");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("isonline", 0);
			params.put("space", roomId);
			this.talkDao.updateOnline(params);
			
			this.logger.debug("Step. [DB] 다른 상담원들에게 상담목록 갱신요청 메세지 전송.");
			sendReloadMessage(profile);
		}
	}
	
	/**
     * 
     * @Method Name : sendReloadMessage
     * @작성일 : 2020. 10. 22.
     * @작성자 : anchiseong
     * @변경이력 : 
     * @Method 설명 : 상담목록 갱신요청 메세지 전송.
     * @param profile
     */
    public void sendReloadMessage(Profile profile) {
    	this.logger.debug("Step. [DB] 다른 상담원들에게 상담목록 갱신요청 메세지 전송.");
    	sendPayload(MessageType.RELOAD, getLobbySpace(profile.getCompanyId()), profile.getLoginName(), this.messageService.getMessage("talk.reload"));
    }
    
    /**
	 * 
	 * @Method Name : sendReadyRoomCountMessage
	 * @작성일 : 2020. 10. 22.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 대기상담 건수 알림.
	 * @param profile
	 * @param data
	 */
	public void sendReadyRoomCountMessage(Profile profile) {
		Map<String, Object> params = new HashMap<String, Object>();
		int count = this.talkDao.selectReadySpaceCount(params);
		if(count > 0) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("count", count);
			
			TalkMessage talkMessage = new TalkMessage();
	    	talkMessage.setType(MessageType.READYCOUNT);
	    	talkMessage.setCompanyId(profile.getCompanyId());
	    	talkMessage.setRoomId(getLobbySpace(profile.getCompanyId()));
	    	talkMessage.setAppid("sdtadm");
	    	talkMessage.setMsg("");
	    	talkMessage.setData(result);
	    	
	    	this.logger.debug("Step. [Socket] 상담원들에게 대기상담 알림.");
	    	sendPayload(talkMessage);
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
		TalkMessage talkMessage = new TalkMessage(type, roomId, sender, message, count, data);
		sendPayload(talkMessage);
	}
	
	// 메세지 발송 모듈.
	public void sendPayload(MessageType type, Speak speak) {
		sendPayload(type, speak, null);	
	}
	
	// 메세지 발송 모듈.
	public void sendPayload(MessageType type, Speak speak, Map<String, Object> data) {
		TalkMessage message = new TalkMessage();
		
		message.setType(type);
		message.setCompanyId(Integer.toString(speak.getCid()));
		message.setId(speak.getId());
		message.setIsemp(speak.isIsemp() ? 1 : 0);
		message.setIsonline(speak.isIsonline() ? 1 : 0);
		message.setMtype(speak.getMtype());
		message.setNotread(speak.getNotread());
		message.setOnlyadm(speak.getOnlyadm());
		message.setRoomId(Long.toString(speak.getSpace()));
		message.setSpeaker(speak.getSpeaker());
		message.setMsg(speak.getMsg());
		message.setSysmsg(speak.getSysmsg());
		//message.setCreatedate(speak.getCreateDate());
		//message.setWorkdadate(speak.getWorkDate());
		message.setData(data);
		
		this.logger.info("Message : " + message);
		
		sendPayload(message);		
	}

	// 메세지 발송 모듈.
	public void sendPayload(TalkMessage message) {
		this.redisTemplate.convertAndSend(channelTopic.getTopic(), message);
	}
	
	// 새로운 메세지 발송 처리.
	public void sendData(MsgType msgType, String roomId, int cid, int isemp, int userno, Object data) {
		MessageVo message = new MessageVo();
		message.setMsgType(msgType);
		message.setRoomId(roomId);
		message.setCid(cid);
		message.setIsemp(isemp);
		message.setUserno(userno);
		message.setData(data);
		
		this.logger.debug("sendData - " + message.toString());
		
		this.redisTemplate.convertAndSend(channelTopic.getTopic(), message);
	}
	
	
}