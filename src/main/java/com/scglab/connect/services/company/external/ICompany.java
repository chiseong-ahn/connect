package com.scglab.connect.services.company.external;

import java.util.List;
import java.util.Map;

import com.scglab.connect.services.common.auth.Contract;
import com.scglab.connect.services.member.Member;

public interface ICompany {
	
	/**
	 * 
	 * @Method Name : login
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 1. 상담사 로그인
	 * @param id - 로그인 Id
	 * @param password - 비밀번호.
	 * @return
	 */
	public boolean login(String id, String password);
	
	/**
	 * 
	 * @Method Name : employees
	 * @작성일 : 2020. 11. 19.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 2. 직원 목록 가져오기
	 * @return
	 */
	public List<Map<String, Object>> employees();
	
	/**
	 * 
	 * @Method Name : employee
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 3. 도시가스 직원정보 가져오기.
	 * @param userno
	 * @return
	 */
	public Map<String, Object> employee(String id);
	
	/**
	 * 
	 * @Method Name : minwons
	 * @작성일 : 2020. 10. 23.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 4. 민원 등록
	 * @return
	 */
	public int minwons(Map<String, String> params);
	
	/**
	 * 
	 * @Method Name : contractInfo
	 * @작성일 : 2020. 11. 19.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 5. 사용계약번호 상세 정보
	 * @Param useContractNum 사용계약번호 (6000000486)
	 * @return 
	 */
	public Map<String, Object> contractInfo(String useContractNum);
	
	
	
	/**
	 * 
	 * @Method Name : contractBilDetail
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 6. 사용계약번호 결제 상세 정보
	 * @param useContractNum 사용계약번호. (6000000486)
	 * 			requestYm - 요청월. (202001)
	 * 			deadlineFlag - 납기구분. (20)
	 * @return
	 */
	public Map<String, Object> contractBilDetail(String useContractNum, String requestYm, String deadlineFlag);
	
	
	/**
	 * 
	 * @Method Name : getWorkCalendar
	 * @작성일 : 2020. 10. 26.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 7. 휴일 여부 체크
	 * @return
	 */
	public int getWorkCalendar();
	
	
	/**
	 * 
	 * @Method Name : contracts
	 * @작성일 : 2020. 12. 2.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 8. 고객의 계약정보 목록
	 * @param member
	 * @return
	 */
	public List<Map<String, Object>> contracts(long member);
	
	
	/**
	 * 
	 * @Method Name : getProfile
	 * @작성일 : 2020. 11. 19.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 9. 고객 profile 정보.
	 * @param member : 가스앱 회원번호
	 * @return
	 */
	public Map<String, Object> getProfile(long member);
	
	
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
	
	
}
