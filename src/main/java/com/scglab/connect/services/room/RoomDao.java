package com.scglab.connect.services.room;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class RoomDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "room.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	/**
	 * 
	 * @Method Name : getCurrentTimeStats
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 현재 시간 방 통계 정보.
	 * @param params
	 * @return
	 */
	public Map<String, Object> getCurrentTimeStats(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "getCurrentTimeStats", params);
	}
	
	
	/**
	 * 
	 * @Method Name : findIngState
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 진행중인 방 목록
	 * @param params
	 * @return
	 */
	public List<Room> findIngState(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findIngState", params);
	}
	
	/**
	 * 
	 * @Method Name : findReadyState
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 대기중인 방 목록.
	 * @param params
	 * @return
	 */
	public List<Room> findReadyState(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findReadyState", params);
	}
	
	/**
	 * 
	 * @Method Name : findCloseState
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 종료된 방 목록.
	 * @param params
	 * @return
	 */
	public List<Room> findCloseState(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findCloseState", params);
	}
	
	public List<Map<String, Object>> findByRoomIdAll(Map<String, Object> params){
		return this.sqlSession.selectList(this.namespace + "findByRoomIdAll", params);
	}
	
	/**
	 * 
	 * @Method Name : findSearchJoinHistory
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 이전 상담 검색
	 * @param params
	 * @return
	 */
	public Map<String, Object> findSearchJoinHistory(Map<String, Object> params){
		return this.sqlSession.selectOne(this.namespace + "findSearchJoinHistory", params);
	}
	
	/**
	 * 
	 * @Method Name : closeRoom
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 방 종료
	 * @param params
	 * @return
	 */
	public int closeRoom(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "closeRoom", params);
	}
	
	/**
	 * 
	 * @Method Name : transferRoom
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 방 이관
	 * @param params
	 * @return
	 */
	public int transferRoom(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "transferRoom", params);
	}
	
	/**
	 * 
	 * @Method Name : matchRoom
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 방 상담사 매칭
	 * @param params
	 * @return
	 */
	public int matchRoom(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "matchRoom", params);
	}
	
	/**
	 * 
	 * @Method Name : getDetail
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 방 상세
	 * @param params
	 * @return
	 */
	public Room getDetail(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "getDetail", params);
	}
	
	/**
	 * 
	 * @Method Name : joinRoom
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 방 조인.
	 * @param params
	 * @return
	 */
	public Map<String, Object> joinRoom(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "joinRoom", params);
	}
	
	/**
	 * 
	 * @Method Name : updateOnline
	 * @작성일 : 2020. 11. 16.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 방 온라인 설정
	 * @param params
	 * @return
	 */
	public int updateOnline(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "updateOnline", params);
	}
	
	/**
	 * 
	 * @Method Name : getDetailBySpeakerId
	 * @작성일 : 2020. 11. 17.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 방 상세정보(speaer id기준)
	 * @param params
	 * @return
	 */
	public Room getDetailBySpeakerId(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "getDetailBySpeakerId", params);
	}
	
	/**
	 * 
	 * @Method Name : updateJoinHistory
	 * @작성일 : 2020. 11. 17.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객의 조인 히스토리 정보 최신화
	 * @param params
	 * @return
	 */
	public int updateJoinHistory(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "updateJoinHistory", params);
	}
	
	/**
	 * 
	 * @Method Name : getRoomJoinHstoryDetail
	 * @작성일 : 2020. 11. 17.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 방 종료 상세 정보
	 * @param params
	 * @return
	 */
	public Map<String, Object> getRoomJoinHstoryDetail(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "getRoomJoinHstoryDetail", params);
	}
	
	/**
	 * 
	 * @Method Name : findSearchRangeById
	 * @작성일 : 2020. 11. 17.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 메세지 조회
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findSearchRangeById(Map<String, Object> params) {
		return this.sqlSession.selectList(this.namespace + "findSearchRangeById", params);
	}
	
	/**
	 * 
	 * @Method Name : updateJoinHistoryByChatId
	 * @작성일 : 2020. 11. 17.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : chatid의 카테고리 소분류 정보 수정
	 * @param params
	 * @return
	 */
	public int updateJoinHistoryByChatId(Map<String, Object> params) {
		return this.sqlSession.update(this.namespace + "updateJoinHistoryByChatId", params);
	}
	
	
	
}