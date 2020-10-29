package com.scglab.connect.services.external;

import com.scglab.connect.services.common.auth.Contract;
import com.scglab.connect.services.common.auth.User;

public interface External {
	
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
	 * @param id
	 * @param passwd
	 * @return
	 */
	public User login(String id, String passwd);
	
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
	public User getMemberInfo(int userno);
	
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
