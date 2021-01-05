package com.scglab.connect.services.minwon;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotations.Auth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(name = "민원관리", value="/api/minwon")
public class MinwonController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MinwonService minwonService;
	
	@Auth
	@RequestMapping(name="민원코드 조회", method = RequestMethod.GET, value = "/codes", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> codes(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.minwonService.codes(params, request, response);
	}
	
	@Auth
	@RequestMapping(name="민원 등록", method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Minwon regist(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.minwonService.regist(params, request, response);
	}
	
	@Auth
	@RequestMapping(name="민원 조회", method = RequestMethod.GET, value = "/findSearchByRoomId", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Minwon> findSearchByRoomId(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.minwonService.findSearchByRoomId(params, request, response);
	}
}
	