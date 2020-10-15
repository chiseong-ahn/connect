package com.scglab.connect.services.talk;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotatios.Auth;
import com.scglab.connect.services.chat.ChatRoomRepository;
import com.scglab.connect.services.chat.JwtTokenProvider;
import com.scglab.connect.services.common.auth.AuthService;
import com.scglab.connect.services.common.auth.User;
import com.scglab.connect.utils.JwtUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/talk")
@Tag(name = "상담 채팅", description = "상담 채팅")
public class TalkController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	TalkService talkService;
	
	@Autowired
	AuthService authService;
	
	private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomRepository chatRoomRepository;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/spaces", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="스페이스(상담) 목록 조회", description = "스페이스(상담) 목록을 조회합니다.", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> spaces(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.talkService.spaces(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/spaces/{spaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="스페이스(상담) 정보 조회", description = "", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> space(@RequestParam Map<String, Object> params, HttpServletRequest request, @Parameter(description = "스페이스 번호", required = true, in = ParameterIn.PATH, example = "112") @PathVariable String spaceId) throws Exception {
		this.logger.debug("spaceId : " + spaceId);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/spaces/{spaceId}/manager", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="스페이스(상담) 상담사 변경(이관)", description = "", security = {@SecurityRequirement(name = "bearer-key")})
	public Map<String, Object> updateManager(@RequestParam Map<String, Object> params, HttpServletRequest request, @Parameter(description = "스페이스 번호", required = true, in = ParameterIn.PATH, example = "112") @PathVariable String spaceId) throws Exception {
		params.put("spaceId", spaceId);
		return this.talkService.updateManager(params, request);
	}
    
    @MessageMapping("/talk/message")		// /pub/chat/message로 전송된 메세지를 받으
    public void message(ChatMessage message, @Header("token") String token) {
    	
    	this.logger.debug("token : " + token);
    	
    	JwtUtils jwtUtils = new JwtUtils();
    	Map<String, Object> claims = jwtUtils.getJwtData(token);
    	User user = this.authService.getUserInfo(claims);
    	
//        String nickname = jwtTokenProvider.getUserNameFromJwt(token);
        // 로그인 회원 정보로 대화명 설정
        message.setSender(user.getEmpno());
        // 채팅방 인원수 세팅
        message.setUserCount(chatRoomRepository.getUserCount(message.getRoomId()));
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        this.logger.debug("sendChatMessage : " + message);
        //chatService.sendChatMessage(message);
        talkService.sendChatMessage(message);
    }
}


