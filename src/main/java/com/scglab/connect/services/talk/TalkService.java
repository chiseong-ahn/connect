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

import com.scglab.connect.services.adminmenu.emp.EmpDao;
import com.scglab.connect.services.common.auth.User;
import com.scglab.connect.services.common.service.JwtService;
import com.scglab.connect.services.common.service.MessageService;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.login.Profile;
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
    private LoginService loginService;
    
	@Autowired
	private TalkDao talkDao;
	
	@Autowired
	private TalkHandler talkHandler;
	
	@Autowired
	private EmpDao empDao;
	
	@Autowired
    private MessageService messageService;
	
	@Autowired
	private JwtService jwtService;
	
	public Map<String, Object> token(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		String token = this.jwtService.generateToken(params);
		data.put("token", token);
		return data;
	}
	
	public Map<String, Object> minwons(Map<String, Object> params, HttpServletRequest request) throws Exception {
		

		Profile profile = this.loginService.getProfile(request);
		params.put("cid", profile.getCompanyId());
		
		//Map<String, Object> data = new HashMap<String, Object>();
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
		
		Map<String, Object> data = new HashMap<String, Object>();
		params.put("reqName", "");
		params.put("useContractNum", "");
		params.put("classCode", "");
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        
        this.logger.debug("url : " + url);
        this.logger.debug("data : " + data.toString());
        this.logger.debug("headers : " + httpHeaders.toString());
        
		Map<String, Object> result = httpUtils.postApi(url, data, httpHeaders);
		this.logger.debug(result.toString());
		
		return data;
	}
	
	
	public Map<String, Object> today(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		Profile profile = this.loginService.getProfile(request);
		params.put("cid", profile.getCompanyId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> today = this.talkDao.today(params);
		data.put("today", today);
		
		return data;
	}
	
	public Map<String, Object> state(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		Profile profile = this.loginService.getProfile(request);
		params.put("cid", profile.getCompanyId());
		params.put("id", profile.getId());
				
		Map<String, Object> data = new HashMap<String, Object>();
		
		this.logger.debug("상담상태 변경.");
		int result = this.empDao.update(params);
		
		data.put("result", result > 0 ? true : false);

		return data;
	}
	
	public Map<String, Object> spaces(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		Profile profile = this.loginService.getProfile(request);
		params.put("cid", profile.getCompanyId());
		
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
		
		data.put("total", list == null ? 0 : list.size());
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> space(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		Profile profile = this.loginService.getProfile(request);
		params.put("cid", profile.getCompanyId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> space = this.talkDao.space(params);
		data.put("space", space);
		
		return data;
	}
	
	public Map<String, Object> speaker(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		Profile profile = this.loginService.getProfile(request);
		params.put("cid", profile.getCompanyId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> speaker = this.talkDao.speaker(params);
		data.put("speaker", speaker);
		
		return data;
	}
	
	public Map<String, Object> speaks(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		Profile profile = this.loginService.getProfile(request);
		params.put("cid", profile.getCompanyId());
				
		Map<String, Object> data = new HashMap<String, Object>();
		List<Speak> list = this.talkDao.speaks(params);
		data.put("total", 100);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> history(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Profile profile = this.loginService.getProfile(request);
		params.put("cid", profile.getCompanyId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.talkDao.history(params);
		data.put("total", 100);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> readyRoomCount(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		int count = 0;
		Map<String, Object> readySpace = null;
		
		Map<String, Object> data = new HashMap<String, Object>();
		count = this.talkDao.selectReadySpaceCount(params);
		
		if(count > 0) {
			this.logger.debug("Step. [DB] 대기상담 정보 조회");
			readySpace = this.talkDao.selectReadySpace(params);
		}
		
		data.put("count", count);
		data.put("readySpace", readySpace);
		
		return data;
	}
	
	public Map<String, Object> historySpeaks(Map<String, Object> params, HttpServletRequest request) throws Exception {
		
		Profile profile = this.loginService.getProfile(request);
		params.put("cid", profile.getCompanyId());
				
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.talkDao.historySpeaks(params);
		data.put("total", 100);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> updateManager(Map<String, Object> params, HttpServletRequest request) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		
		
		
		int emp = DataUtils.getInt(params, "emp", 0);
		int acting = DataUtils.getInt(params, "acting", 0);
		int space = DataUtils.getInt(params, "space", 0);
		String empname = DataUtils.getString(params, "empname", "");
		
		if(emp == 0) {
			// 본인 상담으로 가져오기.
			Profile profile = this.loginService.getProfile(request);
			emp = profile.getId();
			empname = profile.getLoginName();
		}
		
		this.logger.debug("acting : " + acting);
		this.logger.debug("space : " + space);
		this.logger.debug("emp : " + emp);
		this.logger.debug("empname : " + empname);
		this.logger.debug("상담사 변경처리");
		
		params.put("acting", acting);
		params.put("space", space);
		params.put("emp", emp);
		params.put("empname", empname);
		this.talkDao.updateSpace(params);
		
		data.put("isSuccess", true);

		return data;
	}
}