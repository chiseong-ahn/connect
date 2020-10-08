package com.scglab.connect.services.talk;

import java.util.Map;

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
import com.scglab.connect.services.chat.ChatMessage;
import com.scglab.connect.services.chat.ChatRoomRepository;
import com.scglab.connect.services.chat.ChatService;
import com.scglab.connect.services.chat.JwtTokenProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/talk")
@Api(tags = "3. 상담톡 API")
public class TalkController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	TalkService talkService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "{cid}/spaces", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "스페이스 목록조회")
	public Map<String, Object> spaces(@ApiParam(hidden=true) @RequestParam Map<String, Object> params, @ApiParam(name = "기관코드", required = true) @PathVariable String cid) throws Exception {
		params.put("cid", cid);
		return this.talkService.spaces(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "{cid}/spaces/{spaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "스페이스 조회")
	public Map<String, Object> space(@ApiParam(hidden=true) @RequestParam Map<String, Object> params, @ApiParam(name = "기관코드", required = true) @PathVariable String cid, @ApiParam(name = "스페이스번호", required = true) @PathVariable String spaceId) throws Exception {
		this.logger.debug("spaceId : " + spaceId);
		params.put("cid", cid);
		return null;
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "{cid}/spaces/{spaceId}/speaks", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "이전글 목록조회")
	public Map<String, Object> speaks(@ApiParam(hidden=true) @RequestParam Map<String, Object> params, @ApiParam(name = "기관코드", required = true) @PathVariable String cid, @ApiParam(name = "스페이스번호", required = true) @PathVariable String spaceId) throws Exception {
		this.logger.debug("spaceId : " + spaceId);
		params.put("cid", cid);
		params.put("spaceId", spaceId);
		return this.talkService.speaks(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "{cid}/speakers/{speaker}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "채팅자 조회")
	public Map<String, Object> speaker(@ApiParam(hidden=true) @RequestParam Map<String, Object> params, @ApiParam(name = "기관코드", required = true) @PathVariable String cid, @ApiParam(name = "대화자", required = true) @PathVariable String speaker) throws Exception {
		this.logger.debug("speaker : " + speaker);
		params.put("cid", cid);
		params.put("speaker", speaker);
		return this.talkService.speaker(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "{cid}/spaces/{spaceId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "이전 상담목록 조회")
	public Map<String, Object> history(@ApiParam(hidden=true) @RequestParam Map<String, Object> params, @ApiParam(name = "기관코드", required = true) @PathVariable String cid, @ApiParam(name = "스페이스번호", required = true) @PathVariable String spaceId) throws Exception {
		params.put("cid", cid);
		params.put("spaceId", spaceId);
		return this.talkService.history(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "{cid}/spaces/{spaceId}/history/speaks", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "이전 상담 상세내용 조회")
	public Map<String, Object> historySpeaks(@ApiParam(hidden=true) @RequestParam Map<String, Object> params, @ApiParam(name = "기관코드", required = true) @PathVariable String cid, @ApiParam(name = "스페이스번호", required = true) @PathVariable String spaceId) throws Exception {
		params.put("cid", cid);
		params.put("spaceId", spaceId);
		return this.talkService.historySpeaks(params);
	}
	
	private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;
	
    
    @MessageMapping("/talk/message")		// /pub/chat/message로 전송된 메세지를 받으
    public void message(ChatMessage message, @Header("token") String token) {
        String nickname = jwtTokenProvider.getUserNameFromJwt(token);
        // 로그인 회원 정보로 대화명 설정
        message.setSender(nickname);
        // 채팅방 인원수 세팅
        message.setUserCount(chatRoomRepository.getUserCount(message.getRoomId()));
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        this.logger.debug("sendChatMessage : " + message);
        //chatService.sendChatMessage(message);
        talkService.sendChatMessage(message);
    }
	
	
	
	
	
	
}


