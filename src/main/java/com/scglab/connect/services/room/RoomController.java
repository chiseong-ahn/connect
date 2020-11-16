package com.scglab.connect.services.room;

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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/room")
@Tag(name = "채팅방 관리", description = "방(채팅) API를 제공합니다.")
public class RoomController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RoomService roomService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="채팅방 조회", description = "채팅방 조회", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Object currentTimeStats(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.roomService.getRoomInfo(params, request, response);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="채팅방 상세 조회", description = "채팅방 상세 조회", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Room room(@Parameter(description = "방 id") @PathVariable Long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.roomService.room(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/closeRoom", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="채팅방 종료", description = "채팅방 종료", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> closeRoom(@Parameter(description = "방 id") @PathVariable Long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.roomService.closeRoom(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/transferRoom", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="채팅방 이관", description = "채팅방 이관", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> transferRoom(@Parameter(description = "방 id") @PathVariable Long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.roomService.transferRoom(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/findSearchJoinHistory", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="이전 상담 검색", description = "채팅방 이관", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> findSearchJoinHistory(@Parameter(description = "방 id") @PathVariable Long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.roomService.findSearchJoinHistory(params, request, response);
	}
}
	