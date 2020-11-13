package com.scglab.connect.services.talk;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotations.Auth;
import com.scglab.connect.services.common.auth.AuthService;
import com.scglab.connect.services.common.auth.User;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.services.talk.TalkMessage.MessageType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/talk")
@Tag(name = "상담 채팅", description = "상담 채팅")
public class TalkController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TalkService talkService;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private TalkHandler talkHandler;
    
    @Auth
	@RequestMapping(method = RequestMethod.POST, value = "/minwons", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="민원등록", description = "", security = {@SecurityRequirement(name = "bearer-key")})
    @Parameters({
    	@Parameter(name = "customer", description = "고객 회원번호(가스앱)", required = true, in = ParameterIn.QUERY, example = "3769"),
    	@Parameter(name = "classCode", description = "민원분류코드", required = true, in = ParameterIn.QUERY, example = "68"),
    	@Parameter(name = "memo", description = "민원 내용", required = true, in = ParameterIn.QUERY, example = "민원등록 테스트"),
    	@Parameter(name = "chatId", description = "ChatID", required = true, in = ParameterIn.QUERY, example = "143"),
    })
	public Map<String, Object> minwon(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.talkService.minwons(params, request);
	}
	
    @Auth
	@RequestMapping(method = RequestMethod.GET, value = "/today", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="오늘의 상담 현황", description = "오늘 상담 현황(신규, 진행, 종료)을 조회합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> today(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.talkService.today(params, request);
	}
    
    @Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/state", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="상담 상태변경", description = "상담 상태를 변경한다.", security = {@SecurityRequirement(name = "bearer-key")})
    @Parameters({
		@Parameter(name = "state", description = "상담 상태코드(0-상담중, 1-휴식중, 2-회의중, 3-콜집중, 5-퇴근, 6-점심, 9-기타)", required = true, in = ParameterIn.QUERY, example = "0"),
    })
	public Map<String, Object> state(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.talkService.state(params, request);
	}
    
    @Auth
	@RequestMapping(method = RequestMethod.GET, value = "/spaces", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="스페이스(상담) 목록 조회", description = "스페이스(상담) 목록을 조회합니다.", security = {@SecurityRequirement(name = "bearer-key")})
    @Parameters({
		@Parameter(name = "state", description = "상담 상태(2-종료)", required = false, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "keyfield", description = "검색항목(customer-고객명, msg-대화내용, emp-상담사)", required = false, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "keyword", description = "검색어", required = false, in = ParameterIn.QUERY, example = ""),
		@Parameter(name = "startDate", description = "검색시작일(YYYY-MM-DD)", required = false, in = ParameterIn.QUERY, example = "2020-01-01"),
		@Parameter(name = "endDate", description = "검색종료일(YYYY-MM-DD)", required = false, in = ParameterIn.QUERY, example = "2020-10-16")
    })
	public Map<String, Object> spaces(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.talkService.spaces(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/spaces/{spaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="스페이스(상담) 정보 조회", description = "", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> space(@RequestParam Map<String, Object> params, HttpServletRequest request, @Parameter(description = "스페이스 번호", required = true, in = ParameterIn.PATH, example = "112") @PathVariable String spaceId) throws Exception {
		this.logger.debug("spaceId : " + spaceId);
		params.put("spaceId", spaceId);
		return this.talkService.space(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/spaces/{spaceId}/speaks", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="스페이스(상담)의 대화내용 목록 조회", description = "", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> speaks(@RequestParam Map<String, Object> params, HttpServletRequest request, @Parameter(description = "스페이스 번호", required = true, in = ParameterIn.PATH, example = "112") @PathVariable String spaceId) throws Exception {
		this.logger.debug("spaceId : " + spaceId);
		params.put("spaceId", spaceId);
		return this.talkService.speaks(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/speakers/{speaker}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="스페이스(상담)의 채팅자 조회", description = "", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> speaker(@RequestParam Map<String, Object> params, HttpServletRequest request, @Parameter(description = "스페이스 번호", required = true, in = ParameterIn.PATH, example = "112") @PathVariable String speaker) throws Exception {
		this.logger.debug("speaker : " + speaker);
		params.put("speaker", speaker);
		return this.talkService.speaker(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/spaces/{spaceId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="이전 스페이스(상담) 목록 조회", description = "", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> history(@RequestParam Map<String, Object> params, HttpServletRequest request, @Parameter(description = "스페이스 번호", required = true, in = ParameterIn.PATH, example = "112") @PathVariable String spaceId) throws Exception {
		params.put("spaceId", spaceId);
		return this.talkService.history(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/spaces/{spaceId}/history/speaks", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="이전 스페이스(상담)의 대화내용 조회", description = "", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> historySpeaks(@RequestParam Map<String, Object> params, HttpServletRequest request, @Parameter(description = "스페이스 번호", required = true, in = ParameterIn.PATH, example = "112") @PathVariable String spaceId) throws Exception {
		params.put("spaceId", spaceId);
		return this.talkService.historySpeaks(params, request);
	}
	
	
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/spaces/readyroom", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="대기상담 조회", description = "", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> readyRoomCount(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.talkService.readyRoomCount(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/spaces/{space}/manager", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="스페이스(상담) 상담사 변경(이관)", description = "", security = {@SecurityRequirement(name = "bearer-key")})
	@Parameters({
		@Parameter(schema = @Schema(name="emp", description = "관리자 관리번호", type="int", allowableValues = {"1", "2"})),
		@Parameter(schema = @Schema(name="empname", description = "상담자 명", type="string", allowableValues = {"서울도시가스", "홍길동"})),
		@Parameter(schema = @Schema(name="acting", description = "변경유형(0-당겨오기, 1-준비상태로 변경, 2-종료상태 변경, 11-상담이관)", type="int", allowableValues = {"0", "1", "2", "11"}))
	})
	public Map<String, Object> updateManager(@RequestBody Map<String, Object> params, HttpServletRequest request, @Parameter(description = "스페이스 번호", required = true, in = ParameterIn.PATH, example = "112") @PathVariable int space) throws Exception {
		params.put("space", space);
		return this.talkService.updateManager(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/chatbot", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="챗봇대화 조회", description = "", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> chatbot(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		//return this.talkService.chatbot(params, request);
		return null;
	}
	
	
	
	@MessageMapping("/talk/message")
    public void talkMessage(TalkMessage message) {
		
		//User user = this.authService.getUserInfo(message.getToken());
		Member profile = this.loginService.getMember(message.getToken());
		
		if(message.getType().equals(MessageType.ASSIGN)) {
			this.talkHandler.assign(profile, message);
			
		}else if(message.getType().equals(MessageType.MESSAGE)) {
        	this.talkHandler.message(profile, message);
        	
		}else if(message.getType().equals(MessageType.LEAVE)) {
        	this.talkHandler.leave(profile, message);
        	
		}else if(message.getType().equals(MessageType.END)) {
			this.talkHandler.end(profile, message);
			
		}else if(message.getType().equals(MessageType.PREHISTORY)) {
			this.talkHandler.prehistory(profile, message.getRoomId());
			
		}else if(message.getType().equals(MessageType.SPEAKS)) {
			this.talkHandler.speaks(profile, message.getRoomId());
			
		}
    }
}


