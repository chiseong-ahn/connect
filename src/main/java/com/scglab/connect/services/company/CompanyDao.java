package com.scglab.connect.services.company;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class CompanyDao extends CommonDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * XML의 매핑되는 prefix namespace ex. sdtalk.sample.selectList => sdtalk.sample
	 */
	public String namespace = "company.";

	@Override
	protected String getNamespace() {
		return namespace;
	}

	/**
	 * 
	 * @Method Name : getCompanies
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 회사 목록 조회.
	 * @param params
	 * @return
	 */
	public List<Company> getCompanies(Map<String, Object> params) {
		return this.sqlSession.selectList(this.namespace + "findAll", params);
	}

	/**
	 * 
	 * @Method Name : getCompany
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 회사상세정보 조회.
	 * @param params
	 * @return
	 */
	public Company getCompany(Map<String, Object> params) {
		return this.sqlSession.selectOne(this.namespace + "getDetail", params);
	}
}