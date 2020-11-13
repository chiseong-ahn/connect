package com.scglab.connect.services.company.external;

import com.scglab.connect.services.common.auth.Contract;
import com.scglab.connect.services.common.auth.User;
import com.scglab.connect.services.member.Member;

public interface ICompany {
	
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
	
	/**
	 * 
	 * @Method Name : getUser
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 로그인 및 회원정보 조회
	 * @param loginName - 로그인 Id
	 * @param password - 비밀번호.
	 * @return
	 */
	public Member login(String loginName, String password);
	
	/**
	 * 
	 * @Method Name : getMemberInfo
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 회원정보 조회.
	 * @param userno
	 * @return
	 */
	public Member getMemberInfo(int userno);
	
	/**
	 * 
	 * @Method Name : getMemberContractDetail
	 * @작성일 : 2020. 10. 20.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 결제내역 상세조회.
	 * @param contractNo
	 * @return
	 */
	public Contract getMemberContractDetail(int contractNo);
	
	/**
	 * 
	 * @Method Name : sendMinwon
	 * @작성일 : 2020. 10. 23.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 민원 등록(전송)
	 * @return
	 */
	public int sendMinwon();
	
	/**
	 * 
	 * @Method Name : getHoliday
	 * @작성일 : 2020. 10. 26.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 기관 휴일여부 확인.
	 * @return
	 */
	public int getHoliday();
}
