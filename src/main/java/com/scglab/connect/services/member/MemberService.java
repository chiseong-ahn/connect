package com.scglab.connect.services.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.service.MessageHandler;

@Service
public class MemberService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageHandler messageService;
	
	@Autowired
	private MemberDao memberDao;
	
	/**
	 * 
	 * @Method Name : members
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 회원 조회.
	 * @param params
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> members(Map<String, Object> params, int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<Member> memberList = this.memberDao.findNotAdminMembers(params);
		data.put("list", memberList);
		
		return data;
	}
	
	/**
	 * 
	 * @Method Name : member
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 회원 상세 조회.
	 * @param params
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> member(Map<String, Object> params, int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		Member member = this.memberDao.findMemberWithId(params);
		data.put("member", member);
		return data;
	}
	
	/**
	 * 
	 * @Method Name : regist
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 회원 등록.
	 * @param params
	 * @param id
	 * @param request
	 * @param response 등록된 회원정보.
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> regist(Map<String, Object> params, int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		int result = this.memberDao.insertMember(params);
		if(result > 0) {
			Member member = this.memberDao.findOne("member.regist", params);
			data.put("member", member);
		}
		return data;
	}
	
	/**
	 * 
	 * @Method Name : update
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 회원 수정.
	 * @param params
	 * @param id
	 * @param request
	 * @param response 수정된 회원정보.
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> update(Map<String, Object> params, int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		int result = this.memberDao.update("member.update", params);
		if(result > 0) {
			Member member = this.memberDao.findMemberWithId(params);
			data.put("member", member);
		}
		return data;
	}

	/**
	 * 
	 * @Method Name : state
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 회원 상태 변경.
	 * @param params
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> state(Map<String, Object> params, int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.memberDao.updateMemberState(params);
		data.put("success", result > 0 ? true : false);
		return data;
	}
}