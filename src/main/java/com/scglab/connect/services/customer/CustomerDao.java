package com.scglab.connect.services.customer;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class CustomerDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "api.customer.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	/**
	 * 
	 * @Method Name : findCustomer
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객정보 조회
	 * @param params
	 * @return
	 */
	public Map<String, Object> findCustomer(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "getDetail", params);
	}
	
	/**
	 * 
	 * @Method Name : findBySpeakerId
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : SpeakerId를 통해 고객정보 조회
	 * @param params
	 * @return
	 */
	public Customer findBySpeakerId(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "getBySpeakerId", params);
	}
	
	/**
	 * 
	 * @Method Name : findByGassappMemberNumber
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 가스앱 회원번호로 고객정보 조회
	 * @param params
	 * @return
	 */
	public Customer findByGassappMemberNumber(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "getByGasappMemberNumber", params);
	}
	
	/**
	 * 
	 * @Method Name : regist
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객 등록.
	 * @param params
	 * @return
	 */
	public int regist(Map<String, Object> params) {
		return this.sqlSession.insert(this.namespace + "regist", params);
	}
	
	/**
	 * 
	 * @Method Name : updateSwearInsultCount
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객 정보 수정(욕설, 부적잘한 메세지 사용 최신화)
	 * @param params
	 * @return
	 */
	public int updateSwearInsultCount(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "updateSwearInsultCount", params);
	}
	
	/**
	 * 
	 * @Method Name : enableBlackStatus
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객 관심고객으로 수정
	 * @param params
	 * @return
	 */
	public int enableBlackStatus(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "enableBlackStatus", params);
	}
	
	/**
	 * 
	 * @Method Name : disbleBlackStatus
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객 관심고객 해제
	 * @param params
	 * @return
	 */
	public int disbleBlackStatus(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "disbleBlackStatus", params);
	}
	
	/**
	 * 
	 * @Method Name : plusSwearCount
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객의 욕설 count 증가
	 * @param params
	 * @return
	 */
	public int plusSwearCount(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "plusSwearCount", params);
	}
	
	/**
	 * 
	 * @Method Name : plusInsultCount
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객의 부적절한 count 증가
	 * @param params
	 * @return
	 */
	public int plusInsultCount(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "plusInsultCount", params);
	}
	
	/**
	 * 
	 * @Method Name : findBlockMember
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 블록인 사용자 목록 조회.
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findBlockMember(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findBlockMember", params);
	}
	
	/**
	 * 
	 * @Method Name : getDetailRoom
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객 상세 정보(방 정보 포함)
	 * @param params
	 * @return
	 */
	public Map<String, Object> getDetailRoom(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "getDetailRoom", params);
	}
	
	/**
	 * 
	 * @Method Name : selectCustomerInSpace
	 * @작성일 : 2020. 10. 21.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 상담대화에 참여중인 고객정보 조회.
	 * @param params
	 * @return
	 */
	public Customer selectCustomerInSpace(Map<String, Object> params){
		String mapperId = getNamespace() + "selectCustomerInSpace";
		return this.sqlSession.selectOne(mapperId, params);
	}
}