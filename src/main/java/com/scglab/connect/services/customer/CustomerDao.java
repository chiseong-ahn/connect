package com.scglab.connect.services.customer;

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
	public String namespace = "sdtalk.customer.";
	
	@Override
	protected String getNamespace() {
		return namespace;
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
	public Map<String, Object> selectCustomerInSpace(Map<String, Object> params){
		String mapperId = getNamespace() + "selectCustomerInSpace";
		return this.sqlSession.selectOne(mapperId, params);
	}
}