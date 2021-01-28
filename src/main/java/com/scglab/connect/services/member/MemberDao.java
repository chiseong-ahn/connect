package com.scglab.connect.services.member;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class MemberDao extends CommonDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public String namespace = "member.";

	@Override
	protected String getNamespace() {
		return namespace;
	}

	/**
	 * 
	 * @Method Name : findAllCount
	 * @작성일 : 2020. 11. 24.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 회원 목록 카운트 조회
	 * @param params
	 * @return
	 */
	public int findAllCount(Map<String, Object> params) {
		return this.sqlSession.selectOne(namespace + "findAllCount", params);
	}

	/**
	 * 
	 * @Method Name : findAll
	 * @작성일 : 2020. 11. 24.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 회원 목록 조회.
	 * @param params
	 * @return
	 */
	public List<Member> findAll(Map<String, Object> params) {
		return this.sqlSession.selectList(namespace + "findAll", params);
	}

	/**
	 * 
	 * @Method Name : findNotAdminMembers
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 회원목록(관리자가 아닌 회원)
	 * @param params
	 * @return
	 */
	public List<Member> findNotAdminMembers(Map<String, Object> params) {
		return this.sqlSession.selectList(namespace + "findNotAdmin", params);
	}

	/**
	 * 
	 * @Method Name : findCanJoinMembers
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 회원목록(관리자가 아니고 상담을 할 수 있는 회원)
	 * @param params
	 * @return
	 */
	public List<Member> findCanJoinMembers(Map<String, Object> params) {
		return this.sqlSession.selectList(namespace + "findCanJoin", params);
	}

	/**
	 * 
	 * @Method Name : findMemberWithLoginName
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 회원 상세 조회
	 * @param params
	 * @return
	 */
	public Member findMemberWithLoginName(Map<String, Object> params) {
		return this.sqlSession.selectOne(namespace + "getByLoginName", params);
	}

	/**
	 * 
	 * @Method Name : findMemberWithId
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 회원 상세 조회.
	 * @param params
	 * @return
	 */
	public Member findMemberWithId(Map<String, Object> params) {
		return this.sqlSession.selectOne(namespace + "getDetail", params);
	}

	/**
	 * 
	 * @Method Name : insertMember
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 회원 등록.
	 * @param params
	 * @return
	 */
	public int insertMember(Map<String, Object> params) {
		return this.sqlSession.insert(namespace + "regist", params);
	}

	/**
	 * 
	 * @Method Name : updateMember
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 회원 수정.
	 * @param params
	 * @return
	 */
	public int updateMember(Map<String, Object> params) {
		return this.sqlSession.update(namespace + "update", params);
	}

	/**
	 * 
	 * @Method Name : updateMemberState
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 회원 상태 변경.
	 * @param params
	 * @return
	 */
	public int updateMemberState(Map<String, Object> params) {
		return this.sqlSession.update(namespace + "updateState", params);
	}

}