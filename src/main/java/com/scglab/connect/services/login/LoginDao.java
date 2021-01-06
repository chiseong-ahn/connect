package com.scglab.connect.services.login;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;
import com.scglab.connect.services.member.Member;

@Repository
public class LoginDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "login.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	public int findCount(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "profileCount", params);
	}
	
	public Member findAdmin(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "findAdmin", params);
	}
	
	public Member findProfile(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "findProfile", params);
	}
	
	public int saveProfile(Map<String, Object> params) {
		return this.sqlSession.insert(this.namespace + "saveProfile", params);
	}
}