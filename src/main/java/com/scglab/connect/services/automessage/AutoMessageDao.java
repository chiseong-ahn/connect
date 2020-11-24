package com.scglab.connect.services.automessage;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class AutoMessageDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String namespace = "automessage.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	/**
	 * 
	 * @Method Name : findAutoMessageAll
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 자동메세지 목록 조회
	 * @param params
	 * @return
	 */
	public List<AutoMessage> findAutoMessageAll(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findAutoMessageAll", params);
	}
	
	/**
	 * 
	 * @Method Name : getAutoMessage
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 자동메세지 상세 조회
	 * @param params
	 * @return
	 */
	public AutoMessage getAutoMessage(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "getAutoMessage", params);
	}
	
	/**
	 * 
	 * @Method Name : createAutoMessage
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 자동메세지 등록
	 * @param params
	 * @return
	 */
	public int createAutoMessage(Map<String, Object> params){
		return this.sqlSession.insert(this.namespace + "createAutoMessage", params);
	}
	
	/**
	 * 
	 * @Method Name : updateAutoMessage
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 자동메세지 수정
	 * @param params
	 * @return
	 */
	public int updateAutoMessage(Map<String, Object> params){
		return this.sqlSession.update(this.namespace + "updateAutoMessage", params);
	}
	
	/**
	 * 
	 * @Method Name : deleteAutoMessage
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 자동메세지 삭제
	 * @param params
	 * @return
	 */
	public int deleteAutoMessage(Map<String, Object> params){
		return this.sqlSession.delete(this.namespace + "deleteAutoMessage", params);
	}
	
	/**
	 * 
	 * @Method Name : getAutoMessageWelcome
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 신규대화 시작시 인사 메세지 랜덤조회.
	 * @param params
	 * @return
	 */
	public AutoMessage getAutoMessageWelcome(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "getAutoMessageWelcome", params);
	}
	
	/**
	 * 
	 * @Method Name : getAutoMessageByMatchWait
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 배송지연 자동메세지 랜덤 조회.
	 * @param params
	 * @return
	 */
	public AutoMessage getAutoMessageByMatchWait(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "getAutoMessageByMatchWait", params);
	}
	
	/**
	 * 
	 * @Method Name : getAutoMessageByReplyWait
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 답변지연 자동메세지 랜덤 조회
	 * @param params
	 * @return
	 */
	public AutoMessage getAutoMessageByReplyWait(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "getAutoMessageByReplyWait", params);
	}
	
	
	
	
}