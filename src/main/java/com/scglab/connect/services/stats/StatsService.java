package com.scglab.connect.services.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.ErrorService;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.DateUtils;

@Service
public class StatsService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private MessageHandler messageService;
	@Autowired private LoginService loginService;
	@Autowired private StatsDao statsDao;
	@Autowired private CommonService commonService;
	@Autowired private ErrorService errorService;
	
	/**
	 * 
	 * @Method Name : member
	 * @작성일 : 2020. 11. 23.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 통계 : 회원 전체 기준
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> member(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		Map<String, Object> data = null;
		data = this.statsDao.member(params);
		
		return data;
	}
	
	/**
	 * 
	 * @Method Name : myToday
	 * @작성일 : 2020. 11. 23.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 통계 : 오늘의 나의 통계정보
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 	// -신규 접수(newCount) : room 이 오늘 생성됬고 내가 담당자인 경우
		// -대기중(readyCount) : 그냥 기본으로 0건(전체 대기건)
		// -진행중(ingCount) : room의 담당이 나이고 state가 종료가 아닌 경우
		// -종료건(closeCount) : 히스토리 테이블에서 종료일이 오늘이고 담당이 나인 경우
		// -이탈건(outCount) : 그냥 기본으로 0건(전체 이탈건)
		// -최장 고객 대기 시간(maxReadyMinute) : 방의 담당이 나이고 join 메시지 시작일시가 오늘 이고 조인 메시지보다 큰 것 중 시스템 메시지가 아니고 상담사가 작성한 메시지의 시간 - 조인 메시지의 시작일시 중 가장 큰 시간
		// -최장 상담시간(maxSpeakMinute) : 히스토리의 종료시간과 조인 메시지의 시작일을 뺀 시간 -> 히스토리의 종료시간에서 상담사 매칭된 시간을 뺀 시간.
		// -평균 고객 대기 시간(avgReadyMinute) : 최장 고객 대기 시간의 함수를 avg로 바꿈
		// -평균 상담시간(avgSpeakMinute)
		// 
	 * @throws Exception
	 */
	public Map<String, Object> myToday(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		Map<String, Object> data = null;
		//data = this.statsDao.myToday(params);
		
		data = new HashMap<String, Object>();
		data.put("companyId", "1");
		data.put("newCount", 10);
		data.put("readyCount", 5);
		data.put("ingCount", 3);
		data.put("closeCount", 10);
		data.put("outCount", 0);
		data.put("maxReadyMinute", 125);
		data.put("maxSpeakMinute", 1);
		data.put("avgReadyMinute", 20);
		data.put("avgSpeakMinute", 5);
		
		return data;
	}
	
	
	/**
	 * 
	 * @Method Name : search
	 * @작성일 : 2020. 11. 23.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 통계 : 기간 검색
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 	// 챗봇 이용 고객(chatbotUseCount)
		// 채팅상담시스템 인입 고객(talkSystemEnterCount)
		// 신규 접수(newCount)
		// 대기중(readyCount)
		// -진행중(ingCount)
		// -종료건(closeCount)
		// -이탈건(outCount)
		// -총 상담건수(speakCount)
		// -종료상담 건수(closeCount)
		// -최장 고객 대기시간(maxReadyMinute)
		// -최장 상담시간(maxSpeakMinute)
		// -평균 고객 대기시간(avgReadyMinute)
		// -평균 상담시간(avgSpeakMinute)
		// -상담원 평균 응대 건수(avgMemberSpeakCount)
		// -전일 대비 상담 증감(beforeDayPlusCount)
	 * @throws Exception
	 */
	public Map<String, Object> search(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		String errorParams = "";
	    if(!this.commonService.valid(params, "startDate"))
	        errorParams = this.commonService.appendText(errorParams, "종료일 검색 시작일(YYYY-MM-DD)-startDate");
	    
	    if(!this.commonService.valid(params, "endDate"))
	        errorParams = this.commonService.appendText(errorParams, "종료일 검색 종료일(YYYY-MM-DD)-endDate");
	    
	    // 파라미터 유효성 검증.
	    if(!errorParams.equals("")) {
	        // 필수파라미터 누락에 따른 오류 유발처리.
	        this.errorService.throwParameterErrorWithNames(errorParams);
	    }
		
		
		
		Map<String, Object> data = null;
		//data = this.statsDao.search(params);
		
		data = new HashMap<String, Object>();
		data.put("companyId", "1");
		data.put("chatbotUseCount", 0);
		data.put("talkSystemEnterCount", 10);
		data.put("newCount", 10);
		data.put("readyCount", 5);
		data.put("ingCount", 3);
		data.put("closeCount", 10);
		data.put("outCount", 0);
		data.put("speakCount", 10);
		data.put("maxReadyMinute", 125);
		data.put("maxSpeakMinute", 1);
		data.put("avgReadyMinute", 20);
		data.put("avgSpeakMinute", 5);
		data.put("avgMemberSpeakCount", 2);
		data.put("beforeDayPlusCount", 3);
		
		return data;
	}
	
	/**
	 * 
	 * @Method Name : customerAnalysis
	 * @작성일 : 2020. 11. 23.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 통계 : 상담사별 분석
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 	// 신규 접수(newCount)
		// -진행중(ingCount)
		// -종료건(closeCount)
		// -이탈건(outCount)
		// -총 상담건수(speakCount)
		// -종료상담 건수(closeCount)
		// -최장 고객 대기시간(maxReadyMinute)
		// -최장 상담시간(maxSpeakMinute)
		// -평균 고객 대기시간(avgReadyMinute)
		// -평균 상담시간(avgSpeakMinute)
		// -상담원 평균 응대 건수(avgMemberSpeakCount)
		// -전일 대비 상담 증감(beforeDayPlusCount)
	 * @throws Exception
	 */
	public List<Map<String, Object>> customerAnalysis(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		List<Map<String, Object>> list = null;
		//data = this.statsDao.customerAnalysis(params);
		
		list = new ArrayList<Map<String, Object>>();
		Map<String, Object> data1 = new HashMap<String, Object>();
		data1.put("companyId", "1");
		data1.put("memberId", 1);
		data1.put("state", 1);
		data1.put("memberName", "안용성");
		data1.put("saveDate", "2020-11-01");
		data1.put("newCount", 10);
		data1.put("ingCount", 3);
		data1.put("closeCount", 10);
		data1.put("speakCount", 10);
		data1.put("maxReadyMinute", 125);
		data1.put("maxSpeakMinute", 1);
		data1.put("avgReadyMinute", 20);
		data1.put("avgSpeakMinute", 5);
		data1.put("beforeDayPlusCount", 3);
		data1.put("recentCloseCount", 10);
		list.add(data1);
		
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("companyId", "1");
		data2.put("memberId", 1);
		data2.put("state", 1);
		data2.put("memberName", "안치성");
		data2.put("saveDate", "2020-11-01");
		data2.put("newCount", 10);
		data2.put("ingCount", 3);
		data2.put("closeCount", 10);
		data2.put("speakCount", 10);
		data2.put("maxReadyMinute", 125);
		data2.put("maxSpeakMinute", 1);
		data2.put("avgReadyMinute", 20);
		data2.put("avgSpeakMinute", 5);
		data2.put("beforeDayPlusCount", 3);
		data2.put("recentCloseCount", 10);
		list.add(data2);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : useHistory
	 * @작성일 : 2020. 11. 23.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 통계 : 상담 사용 추이
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 	// 챗봇 이용 고객(chatbotUseCount)
		// 채팅상담시스템 인입 고객(talkSystemEnterCount)
		// 신규 접수(newCount)
		// 대기중(readyCount)
		// -진행중(ingCount)
		// -종료건(closeCount)
		// -이탈건(outCount)
		// -총 상담건수(speakCount)
		// -종료상담 건수(closeCount)
		// -최장 고객 대기시간(maxReadyMinute)
		// -최장 상담시간(maxSpeakMinute)
		// -평균 고객 대기시간(avgReadyMinute)
		// -평균 상담시간(avgSpeakMinute)
		// -상담원 평균 응대 건수(avgMemberSpeakCount)
	 * @throws Exception
	 */
	public List<Map<String, Object>> useHistory(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		String errorParams = "";
	    if(!this.commonService.valid(params, "type"))
	        errorParams = this.commonService.appendText(errorParams, "검색유형-type");
	    
	    // 파라미터 유효성 검증.
	    if(!errorParams.equals("")) {
	        // 필수파라미터 누락에 따른 오류 유발처리.
	        this.errorService.throwParameterErrorWithNames(errorParams);
	    }
		
		List<Map<String, Object>> list = null;
		//list = this.statsDao.useHistory(params);
		
		list = new ArrayList<Map<String, Object>>();
		Map<String, Object> data1 = new HashMap<String, Object>();
		data1.put("companyId", "1");
		data1.put("saveDate", "2020-11-01");
		data1.put("month", "2020-11");
		data1.put("chatbotUseCount", 0);
		data1.put("talkSystemEnterCount", 10);
		data1.put("newCount", 10);
		data1.put("readyCount", 5);
		data1.put("ingCount", 3);
		data1.put("closeCount", 10);
		data1.put("outCount", 0);
		data1.put("speakCount", 10);
		data1.put("maxReadyMinute", 125);
		data1.put("maxSpeakMinute", 1);
		data1.put("avgReadyMinute", 20);
		data1.put("avgSpeakMinute", 5);
		data1.put("avgMemberSpeakCount", 2);
		list.add(data1);
		
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("companyId", "1");
		data2.put("saveDate", "2020-11-01");
		data2.put("month", "2020-11");
		data2.put("chatbotUseCount", 0);
		data2.put("talkSystemEnterCount", 10);
		data2.put("newCount", 10);
		data2.put("readyCount", 5);
		data2.put("ingCount", 3);
		data2.put("closeCount", 10);
		data2.put("outCount", 0);
		data2.put("speakCount", 10);
		data2.put("maxReadyMinute", 125);
		data2.put("maxSpeakMinute", 1);
		data2.put("avgReadyMinute", 20);
		data2.put("avgSpeakMinute", 5);
		data2.put("avgMemberSpeakCount", 2);
		list.add(data2);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : hashtag
	 * @작성일 : 2020. 11. 23.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 통계 : 문의 유형별 통계
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> hashtag(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		
		String errorParams = "";
	    if(!this.commonService.valid(params, "searchDate"))
	        errorParams = this.commonService.appendText(errorParams, "검색일(YYYY-MM-DD)-searchDate");
	    
	    // 파라미터 유효성 검증.
	    if(!errorParams.equals("")) {
	        // 필수파라미터 누락에 따른 오류 유발처리.
	        this.errorService.throwParameterErrorWithNames(errorParams);
	    }
		
		List<Map<String, Object>> list = null;
		//list = this.statsDao.hashtag(params);
		
		list = new ArrayList<Map<String, Object>>();
		Map<String, Object> data1 = new HashMap<String, Object>();
		data1.put("companyId", "1");
		data1.put("rank", 1);
		data1.put("name", "요금");
		data1.put("isNew", 0);
		data1.put("beforeDayPlusCount", 5);
		data1.put("beforeRank", 2);
		list.add(data1);
		
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("companyId", "1");
		data2.put("rank", 1);
		data2.put("name", "요금");
		data2.put("isNew", 0);
		data2.put("beforeDayPlusCount", 5);
		data2.put("beforeRank", 2);
		list.add(data2);
		
		return list;
	}
	
	// 일일 상담집계
	public void createStatsCompanyDaily() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("companyId", "1");
		params.put("targetDate", DateUtils.getYesterday());
		this.statsDao.createStatsCompanyDaily(params);
	}
}