package com.scglab.connect.services.company.external;

import java.util.List;
import java.util.Map;

import com.scglab.connect.services.common.auth.Contract;
import com.scglab.connect.services.member.Member;

public interface ICompany {
	
	/**
	 * 
	 * @Method Name : getUser
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 로그인
	 * @param id - 로그인 Id
	 * @param password - 비밀번호.
	 * @return
	 */
	public boolean login(String id, String password);
	
	/**
	 * 
	 * @Method Name : getMembers
	 * @작성일 : 2020. 11. 19.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 직원 목록 가져오기
	 * @return
	 */
	public List<Map<String, Object>> getMembers();
	
	/**
	 * 
	 * @Method Name : getMemberInfo
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 도시가스 직원정보 가져오기.
	 * @param userno
	 * @return
	 */
	public Member getMemberInfo(Map<String, Object> params);
	
	/**
	 * 
	 * @Method Name : sendMinwon
	 * @작성일 : 2020. 10. 23.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 민원 등록(전송)
	 * @return
	 */
	public int sendMinwon(Map<String, Object> params);
	
	/**
	 * 
	 * @Method Name : getContractList
	 * @작성일 : 2020. 11. 19.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객의 계약정보 목록 가져오기.
	 * @return
	 */
	public List<Map<String, Object>> getContractList(Map<String, Object> params);
	
	/**
	 * 
	 * @Method Name : getCustomerInfo
	 * @작성일 : 2020. 11. 19.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객 profile 정보.
	 * @param id
	 * @return
	 */
	public Map<String, Object> getCustomerInfo(Map<String, Object> params);
	
	
	
	/**
	 * 
	 * @Method Name : getContractDetail
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 사용계약번호 상세 정보.
	 * @param contractNo
	 * @return
	 */
	public Contract getContractDetail(Map<String, Object> params);
	
	/**
	 * 
	 * @Method Name : getContractMonthlyDetail
	 * @작성일 : 2020. 11. 19.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 사용계약번호 월별 결제 상세정보.
	 * @param params
	 * @return
	 */
	public Map<String, Object> getContractMonthlyDetail(Map<String, Object> params);
	
	/**
	 * 
	 * @Method Name : isHoliday
	 * @작성일 : 2020. 10. 26.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 휴일여부 체크
	 * @return
	 */
	public int isHoliday(Map<String, Object> params);
	
	/**
	 * 
	 * @Method Name : getCompanyId
	 * @작성일 : 2020. 11. 10.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 회사id 조회.
	 * @return
	 */
	public String getCompanyId();
	
	/**
	 * 
	 * @Method Name : getCompanyName
	 * @작성일 : 2020. 11. 10.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 회사명 조회.
	 * @return
	 */
	public String getCompanyName();
	
	/**
	 * 
	 * @Method Name : isWorking
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 상담업무 근무상태 조회(
	 * @return
	 */
	public int isWorking();
	
	
	
	
	
	
	
	
	
	
}
