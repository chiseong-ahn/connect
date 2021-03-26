package com.scglab.connect.services.review;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotations.Auth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(name = "고객만족도 관리", value="/api/review")
public class ReviewController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ReviewService reviewService;
	
	@Auth
	@RequestMapping(name="고객만족도 조회", method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> search(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.reviewService.findAll(params, request, response);
	}
	
	@RequestMapping(name="고객만족도 엑셀 다운로드", method = RequestMethod.GET, value = "/excel")
	public void exel(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.reviewService.findAllWithDown(params, request, response);
	}
	
}
	