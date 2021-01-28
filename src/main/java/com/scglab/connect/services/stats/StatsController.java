package com.scglab.connect.services.stats;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.base.annotations.Auth;

@RestController
@RequestMapping(name = "통계관리", value="/api/stats")
public class StatsController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private StatsService statsService;
	
	@Auth
	@RequestMapping(name="통계 : 회원 전체 기준", method = RequestMethod.GET, value = "member", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> regist(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.statsService.member(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "myToday", produces = MediaType.APPLICATION_JSON_VALUE)
	public StatsMyToday myToday(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.statsService.myToday(params, request, response);
	}
	
	@Auth
	@RequestMapping(name="통계 : 기간 검색", method = RequestMethod.GET, value = "search", produces = MediaType.APPLICATION_JSON_VALUE)
	public StatsCompany search(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.statsService.search(params, request, response);
	}
	
	@Auth
	@RequestMapping(name="통계 : 상담사별 분석", method = RequestMethod.GET, value = "customer-analysis", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> customerAnalysis(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.statsService.customerAnalysis(params, request, response);
	}
	
	@Auth
	@RequestMapping(name="통계 : 상담 사용 추이", method = RequestMethod.GET, value = "use-history", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<StatsCompany> useHistory(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.statsService.useHistory(params, request, response);
	}
	
	@Auth
	@RequestMapping(name="통계 : 문의 유형별 통계", method = RequestMethod.GET, value = "hashtag", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> hashtag(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.statsService.hashtag(params, request, response);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "review", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> review(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.statsService.review(params, request, response);
	}
	
	
}
	