package com.scglab.connect.services.stats;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class StatsDao extends CommonDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * XML의 매핑되는 prefix namespace ex. sdtalk.sample.selectList => sdtalk.sample
	 */
	public String namespace = "stats.";

	@Override
	protected String getNamespace() {
		return namespace;
	}

	/**
	 * 
	 * @Method Name : member
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 회원 전체 기준 통계
	 * @param params
	 * @return
	 */
	public Map<String, Object> member(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "member", params);
	}

	/**
	 * 
	 * @Method Name : myToday
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : Today 나의 상담현황 조회.
	 * @param params
	 * @return
	 */
	public StatsMyToday myToday(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "myToday", params);
	}

	public StatsCompany search(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "search", params);
	}

	/**
	 * 
	 * @Method Name : customerAnalysis
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 상담사별 분석
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> customerAnalysis(Map<String, Object> params) {
		return this.sqlSession.selectList(this.namespace + "customerAnalysis", params);
	}

	/**
	 * 
	 * @Method Name : useHistory
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 상담 이용 추이
	 * @param params
	 * @return
	 */
	public List<StatsCompany> useHistory(Map<String, Object> params) {
		return this.sqlSession.selectList(this.namespace + "useHistory", params);
	}

	/**
	 * 
	 * @Method Name : hashtag
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 문의유형별 통계
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> hashtag(Map<String, Object> params) {
		return this.sqlSession.selectList(this.namespace + "hashtag", params);
	}

	/**
	 * 
	 * @Method Name : review
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 고객 만족도 통계
	 * @param params
	 * @return
	 */
	public Map<String, Object> review(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "review", params);
	}

	/**
	 * 
	 * @Method Name : createStatsCompanyDaily
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 일별 회사 상담현황 통계 집계처리.
	 * @param params
	 */
	public void createStatsCompanyDaily(Map<String, Object> params) {
		this.sqlSession.insert(this.namespace + "createStatsCompanyDaily", params);
	}

	/**
	 * 
	 * @Method Name : createStatsHashtagDaily
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 시간별/일별 문의유형별 통계 집계처리.
	 * @param params
	 */
	public void createStatsHashtagDaily(Map<String, Object> params) {
		this.sqlSession.insert(this.namespace + "createStatsHashtagDaily", params);
	}

}