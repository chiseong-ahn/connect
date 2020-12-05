package com.scglab.connect.services.message;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotations.Auth;
import com.scglab.connect.constant.Constant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
@Tag(name = "메세지 관리", description = "메세지 API를 제공합니다.")
public class MessageController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageService messageService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="메세지 조회", description = "메세지 조회", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "roomId", description = "룸id", in = ParameterIn.QUERY, required = true, example = "147"),
		@Parameter(name = "queryId", description = "조회유형 구분<br/>findByRoomIdAll-메세지 전체 조회,<br/>findByRoomIdToSpeaker-메세지 조회(조인한 사용자),<br/>findByRoomIdToAdmin-메세지 조회(관리자, 조회자),<br/> findRangeById-메세지 조회(id 범위)", in = ParameterIn.QUERY, required = true, example = "findByRoomIdAll"),
		@Parameter(name = "startId", description = "조회시작번호", in = ParameterIn.QUERY, required = true, example = "9"),
		@Parameter(name = "endId", description = "조회종료번호", in = ParameterIn.QUERY, required = true, example = "100000"),
	})
	public List<Message> search(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.messageService.getMessages(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="메세지 삭제", description = "메세지 삭제", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> delete(@Parameter(description = "메세지 id") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.messageService.delete(params, request, response);
	}
	
}
	