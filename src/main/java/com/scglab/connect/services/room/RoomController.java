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
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
	@Operation(summary="방 조회", description = "방 조회", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "queryId", description = "조회항목<br>"
				+ "1.현재 시간 방 통계 정보 : getCurrentTimeStats<br>" + 
				"  2.진행인 방 목록 : findIngState<br>" + 
				"  3.대기인 방 목록 : findReadyState<br>" + 
				"  4.종료인 방 검색 : findSearchCloseState", required = true, in = ParameterIn.QUERY, example = "getCurrentTimeStats"),
		@Parameter(name = "searchType", description = "검색유형", required = false, in = ParameterIn.QUERY, example = "message"),
		@Parameter(name = "searchValue", description = "검색어", required = false, in = ParameterIn.QUERY, example = ""),
	})
	public Object currentTimeStats(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.roomService.getRoomInfo(params, request, response);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="방 상세 조회", description = "방 상세 조회", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Room room(@Parameter(description = "방 id") @PathVariable Long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.roomService.room(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/matchRoom", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="채팅방 종료", description = "방 상담사 매칭시키기", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Room matchRoom(@Parameter(description = "방 id") @PathVariable Long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.roomService.matchRoom(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/closeRoom", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="방 종료", description = "방 종료", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Room closeRoom(@Parameter(description = "방 id") @PathVariable Long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.roomService.closeRoom(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/transferRoom", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="방 이관", description = "방 이관", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Room transferRoom(@Parameter(description = "방 id") @PathVariable Long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.roomService.transferRoom(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/findSearchJoinHistory", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="이전 상담 검색", description = "이전 상담 검색", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> findSearchJoinHistory(@Parameter(description = "방 id") @PathVariable Long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.roomService.findSearchJoinHistory(params, request, response);
	}
}
	