package com.scglab.connect.services.message;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class MessageDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "message.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	/**
	 * 
	 * @Method Name : findByRoomIdAll
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 채팅메세지 목록
	 * @param params
	 * @return
	 */
	public List<Message> findByRoomIdAll(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findByRoomIdAll", params);
	}
	
	/**
	 * 
	 * @Method Name : findRangeById
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 메시지 조회
	 * @param params
	 * @return
	 */
	public List<Message> findRangeById(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findRangeById", params);
	}
	
	/**
	 * 
	 * @Method Name : findByRoomIdToSpeaker
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 담당자 또는 고객이 보는 메세지
	 * @param params
	 * @return
	 */
	public List<Message> findByRoomIdToSpeaker(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findByRoomIdToSpeaker", params);
	}
	
	/**
	 * 
	 * @Method Name : findByRoomIdToAdmin
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 담당자가 아닌 관리자 또는 조회자 권한이 있는 사용자가 조회시
	 * @param params
	 * @return
	 */
	public List<Message> findByRoomIdToAdmin(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findByRoomIdToAdmin", params);
	}
	
	/**
	 * 
	 * @Method Name : updateMessageNotReadCount
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 메세지 읽음 처리
	 * @param params
	 * @return
	 */
	public int updateMessageNotReadCount(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "updateMessageNotReadCount", params);
	}
	
	/**
	 * 
	 * @Method Name : updateRoomSpeakerReadLastMessage
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 메세지 읽음 처리
	 * @param params
	 * @return
	 */
	public int updateRoomSpeakerReadLastMessage(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "updateRoomSpeakerReadLastMessage", params);
	}
	
	/**
	 * 
	 * @Method Name : updateMessageRead
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 메세지 읽음 처리
	 * @param params
	 * @return
	 */
	public int updateMessageRead(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "updateMessageRead", params);
	}
	
	/**
	 * 
	 * @Method Name : create
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 메세지 생성
	 * @param params
	 * @return
	 */
	public Message create(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "create", params);
	}
	
	/**
	 * 
	 * @Method Name : findRangeByIdNotAdminType
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 메세지 조회(id range) : 메세지 유형이 시스템이 아닌 경우.
	 * @param params
	 * @return
	 */
	public List<Message> findRangeByIdNotAdminType(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findRangeByIdNotAdminType", params);
	}

	/**
	 * 
	 * @Method Name : getReadCountByMessageId
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 읽은 메세지 사용자 수.
	 * @param params
	 * @return
	 */
	public int getReadCountByMessageId(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "getReadCountByMessageId", params);
	}
	
	/**
	 * 
	 * @Method Name : delete
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 메세지 삭제.
	 * @param params
	 * @return
	 */
	public int delete(Map<String, Object> params) {
		return this.sqlSession.delete(this.namespace + "delete", params);
	}
	
	/**
	 * 
	 * @Method Name : deleteMessageRead
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 메세지 읽음 삭제.
	 * @param params
	 * @return
	 */
	public int deleteMessageRead(Map<String, Object> params) {
		return this.sqlSession.delete(this.namespace + "deleteMessageRead", params);
	}
	
	
	
	
	
	
	
	
	
	
	
}