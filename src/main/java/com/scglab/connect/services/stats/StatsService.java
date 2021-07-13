package com.scglab.connect.services.stats;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.ErrorService;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.DateUtils;

@Validated
@Service
@ManagedResource(objectName = "batch:name=StatsJob")
public class StatsService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LoginService loginService;
	@Autowired
	private StatsDao statsDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ErrorService errorService;

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
	public Map<String, Object> member(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
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
	 * @return // -신규 접수(newCount) : room 이 오늘 생성됬고 내가 담당자인 경우 // -대기중(readyCount) :
	 *         그냥 기본으로 0건(전체 대기건) // -진행중(ingCount) : room의 담당이 나이고 state가 종료가 아닌 경우
	 *         // -종료건(closeCount) : 히스토리 테이블에서 종료일이 오늘이고 담당이 나인 경우 //
	 *         -이탈건(outCount) : 그냥 기본으로 0건(전체 이탈건) // -최장 고객 대기 시간(maxReadyMinute) :
	 *         방의 담당이 나이고 join 메시지 시작일시가 오늘 이고 조인 메시지보다 큰 것 중 시스템 메시지가 아니고 상담사가 작성한
	 *         메시지의 시간 - 조인 메시지의 시작일시 중 가장 큰 시간 // -최장 상담시간(maxSpeakMinute) : 히스토리의
	 *         종료시간과 조인 메시지의 시작일을 뺀 시간 -> 히스토리의 종료시간에서 상담사 매칭된 시간을 뺀 시간. // -평균 고객
	 *         대기 시간(avgReadyMinute) : 최장 고객 대기 시간의 함수를 avg로 바꿈 // -평균
	 *         상담시간(avgSpeakMinute) //
	 * @throws Exception
	 */
	public StatsMyToday myToday(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Member member = this.loginService.getMember(request);
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);
		params.put("loginId", member.getId());

		StatsMyToday data = this.statsDao.myToday(params);

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
	 * @return // 챗봇 이용 고객(chatbotUseCount) // 채팅상담시스템 인입 고객(talkSystemEnterCount)
	 *         // 신규 접수(newCount) // 대기중(readyCount) // -진행중(ingCount) //
	 *         -종료건(closeCount) // -이탈건(outCount) // -총 상담건수(speakCount) // -종료상담
	 *         건수(closeCount) // -최장 고객 대기시간(maxReadyMinute) // -최장
	 *         상담시간(maxSpeakMinute) // -평균 고객 대기시간(avgReadyMinute) // -평균
	 *         상담시간(avgSpeakMinute) // -상담원 평균 응대 건수(avgMemberSpeakCount) // -전일 대비
	 *         상담 증감(beforeDayPlusCount)
	 * @throws Exception
	 */
	public StatsCompany search(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Member member = this.loginService.getMember(request);
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		String errorParams = "";
		if (!this.commonService.valid(params, "startDate"))
			errorParams = this.commonService.appendText(errorParams, "종료일 검색 시작일(YYYY-MM-DD)-startDate");

		if (!this.commonService.valid(params, "endDate"))
			errorParams = this.commonService.appendText(errorParams, "종료일 검색 종료일(YYYY-MM-DD)-endDate");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		StatsCompany data = this.statsDao.search(params);
		if (data == null) {
			data = new StatsCompany();
			data.setCompanyId(companyId);
		}

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
	 * @return // 신규 접수(newCount) // -진행중(ingCount) // -종료건(closeCount) //
	 *         -이탈건(outCount) // -총 상담건수(speakCount) // -종료상담 건수(closeCount) // -최장
	 *         고객 대기시간(maxReadyMinute) // -최장 상담시간(maxSpeakMinute) // -평균 고객
	 *         대기시간(avgReadyMinute) // -평균 상담시간(avgSpeakMinute) // -상담원 평균 응대
	 *         건수(avgMemberSpeakCount) // -전일 대비 상담 증감(beforeDayPlusCount)
	 * @throws Exception
	 */
	public List<Map<String, Object>> customerAnalysis(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		List<Map<String, Object>> list = this.statsDao.customerAnalysis(params);

		return list == null ? new ArrayList<Map<String, Object>>() : list;
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
	 * @return // 챗봇 이용 고객(chatbotUseCount) // 채팅상담시스템 인입 고객(talkSystemEnterCount)
	 *         // 신규 접수(newCount) // 대기중(readyCount) // -진행중(ingCount) //
	 *         -종료건(closeCount) // -이탈건(outCount) // -총 상담건수(speakCount) // -종료상담
	 *         건수(closeCount) // -최장 고객 대기시간(maxReadyMinute) // -최장
	 *         상담시간(maxSpeakMinute) // -평균 고객 대기시간(avgReadyMinute) // -평균
	 *         상담시간(avgSpeakMinute) // -상담원 평균 응대 건수(avgMemberSpeakCount)
	 * @throws Exception
	 */
	public List<StatsCompany> useHistory(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		String type = DataUtils.getString(params, "type", "day");
		params.put("type", type);

		params.put("today", DateUtils.getToday());
		List<StatsCompany> list = this.statsDao.useHistory(params);

		return list == null ? new ArrayList<StatsCompany>() : list;
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
	public List<Map<String, Object>> hashtag(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		String errorParams = "";
		if (!this.commonService.valid(params, "searchDate"))
			errorParams = this.commonService.appendText(errorParams, "검색일(YYYY-MM-DD)-searchDate");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		List<Map<String, Object>> list = null;
		list = this.statsDao.hashtag(params);

		if (list != null && list.size() > 0) {
			for (Map<String, Object> hashtag : list) {

				String isNew = DataUtils.getString(hashtag, "isNew", "N");
				long beforeDayPlusCount = 0;
				Long currentRank = (Long) hashtag.get("currentRank");

				if (isNew.equals("Y")) {
					hashtag.put("beforeRank", "-");
				} else {
					Long beforeRank = (Long) hashtag.get("beforeRank");
					beforeDayPlusCount = beforeRank - currentRank;
				}
				hashtag.remove("currentRank");
				hashtag.put("rank", currentRank);
				hashtag.put("beforeDayPlusCount", beforeDayPlusCount);
			}
		} else {
			list = new ArrayList<Map<String, Object>>();
		}

		return list;
	}

	public Map<String, Object> review(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		String companyId = DataUtils.getString(params, "companyId", member.getCompanyId());
		params.put("companyId", companyId);

		Map<String, Object> review = this.statsDao.review(params);
		if (review == null) {
			review = new HashMap<String, Object>();
			review.put("score1", 0);
			review.put("score2", 0);
			review.put("score3", 0);
			review.put("score4", 0);
			review.put("score5", 0);
		}
		return review;
	}

	@ManagedOperation(description = "도스가스별 상담내역 수동집계 처리")
	@ManagedOperationParameters({@ManagedOperationParameter(name = "companyId", description = "도시가스 id(1-서울, 2-인천, ..."), @ManagedOperationParameter(name = "targetDate", description = "집계일자(YYYY-MM-DD)")})
	public void statsCompany(final int companyId, final Date targetDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		params.put("companyId", companyId);
		params.put("targetDate", dateFormat.format(targetDate));

		this.statsDao.createStatsCompanyDaily(params);
	}
	
	@ManagedOperation(description = "도스가스별 회원 수동집계 처리")
	@ManagedOperationParameters({@ManagedOperationParameter(name = "companyId", description = "도시가스 id(1-서울, 2-인천, ..."), @ManagedOperationParameter(name = "targetDate", description = "집계일자(YYYY-MM-DD)")})
	public void statsMember(final int companyId, final Date targetDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		params.put("companyId", companyId);
		params.put("targetDate", dateFormat.format(targetDate));

		this.statsDao.createStatsMemberDaily(params);
		
	}
	
	@ManagedOperation(description = "도스가스별 해시태그 수동집계 처리")
	@ManagedOperationParameters({@ManagedOperationParameter(name = "companyId", description = "도시가스 id(1-서울, 2-인천, ..."), @ManagedOperationParameter(name = "targetDate", description = "집계일자(YYYY-MM-DD)")})
	public void statsHashtag(final int companyId, final Date targetDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		params.put("companyId", companyId);
		params.put("targetDate", dateFormat.format(targetDate));

		this.statsDao.createStatsHashtagDaily(params);
	}
	
	

	// 시간별 상담집계
	public void createStatsEveryHour() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("companyId", "1");
		params.put("targetDate", DateUtils.getToday());

		this.statsDao.createStatsHashtagDaily(params);
	}

	// 일일 상담집계
	public void createStatsDaily() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("companyId", "1");
		params.put("targetDate", DateUtils.getYesterday());

		this.statsDao.createStatsCompanyDaily(params);
		this.statsDao.createStatsMemberDaily(params);
		this.statsDao.createStatsHashtagDaily(params);
	}
}