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
import com.scglab.connect.services.chat.ChatRoomRepository;
import com.scglab.connect.services.chat.ChatService;
import com.scglab.connect.services.chat.JwtTokenProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/talk")
@Tag(name = "상담 채팅", description = "상담 채팅 API")
public class TalkController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	TalkService talkService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "{cid}/spaces", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="스페이스(상담) 목록 조회", description = "스페이스(상담) 목록을 조회합니다.")
	public Map<String, Object> spaces(@RequestParam Map<String, Object> params, @PathVariable String cid) throws Exception {
		params.put("cid", cid);
		return this.talkService.spaces(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "{cid}/spaces/{spaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="스페이스(상담) 정보 조회", description = "")
	public Map<String, Object> space(@RequestParam Map<String, Object> params, @PathVariable String cid, @PathVariable String spaceId) throws Exception {
		this.logger.debug("spaceId : " + spaceId);
		params.put("cid", cid);
		return null;
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "{cid}/spaces/{spaceId}/speaks", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="스페이스(상담)의 대화내용 목록 조회", description = "")
	public Map<String, Object> speaks(@RequestParam Map<String, Object> params, @PathVariable String cid, @PathVariable String spaceId) throws Exception {
		this.logger.debug("spaceId : " + spaceId);
		params.put("cid", cid);
		params.put("spaceId", spaceId);
		return this.talkService.speaks(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "{cid}/speakers/{speaker}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="스페이스(상담)의 채팅자 조회", description = "")
	public Map<String, Object> speaker(@RequestParam Map<String, Object> params, @PathVariable String cid, @PathVariable String speaker) throws Exception {
		this.logger.debug("speaker : " + speaker);
		params.put("cid", cid);
		params.put("speaker", speaker);
		return this.talkService.speaker(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "{cid}/spaces/{spaceId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="이전 스페이스(상담) 목록 조회", description = "")
	public Map<String, Object> history(@RequestParam Map<String, Object> params, @PathVariable String cid, @PathVariable String spaceId) throws Exception {
		params.put("cid", cid);
		params.put("spaceId", spaceId);
		return this.talkService.history(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "{cid}/spaces/{spaceId}/history/speaks", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="이전 스페이스(상담)의 대화내용 조회", description = "")
	public Map<String, Object> historySpeaks(@RequestParam Map<String, Object> params, @PathVariable String cid, @PathVariable String spaceId) throws Exception {
		params.put("cid", cid);
		params.put("spaceId", spaceId);
		return this.talkService.historySpeaks(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "{cid}/spaces/{spaceId}/manager", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="스페이스(상담) 상담사 변경(이관)", description = "")
	public Map<String, Object> updateManager(@RequestParam Map<String, Object> params, @PathVariable String cid, @PathVariable String spaceId) throws Exception {
		params.put("cid", cid);
		params.put("spaceId", spaceId);
		return this.talkService.updateManager(params);
	}
	
	
	
	
	
	
	
	
	private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomRepository chatRoomRepository;
    //private final ChatService chatService;
	
    
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


