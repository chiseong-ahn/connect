package com.scglab.connect.services.room;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotations.Auth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(name = "채팅방 관리", value = "/api/room")
public class RoomController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RoomService roomService;

	@Auth
	@RequestMapping(name = "방 조회", method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object currentTimeStats(@RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return this.roomService.getRoomInfo(params, request, response);
	}

	@Auth
	@RequestMapping(name = "방 상세 조회", method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Room room(@PathVariable Long id, @RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.roomService.room(params, request, response);
	}

	@Auth
	@RequestMapping(name = "방 상담사 매칭시키기", method = RequestMethod.POST, value = "/{id}/matchRoom", produces = MediaType.APPLICATION_JSON_VALUE)
	public Room matchRoom(@PathVariable Long id, @RequestBody Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		params.put("id", id);
		params.put("roomId", id);
		return this.roomService.matchRoom(params, request, response);
	}

	@Auth
	@RequestMapping(name = "방 종료", method = RequestMethod.POST, value = "/{id}/closeRoom", produces = MediaType.APPLICATION_JSON_VALUE)
	public Room closeRoom(@PathVariable Long id, @RequestBody Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		params.put("id", id);
		params.put("roomId", id);
		return this.roomService.closeRoom(params, request, response);
	}

	@Auth
	@RequestMapping(name = "방 이관", method = RequestMethod.POST, value = "/{id}/transferRoom", produces = MediaType.APPLICATION_JSON_VALUE)
	public Room transferRoom(@PathVariable Long id, @RequestBody Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		params.put("id", id);
		params.put("roomId", id);
		return this.roomService.transferRoom(params, request, response);
	}

	@Auth
	@RequestMapping(name = "이전 상담 검색", method = RequestMethod.GET, value = "/{id}/findSearchJoinHistory", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> findSearchJoinHistory(@PathVariable Long id,
			@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		params.put("id", id);
		params.put("roomId", id);
		return this.roomService.findSearchJoinHistory(params, request, response);
	}
}
