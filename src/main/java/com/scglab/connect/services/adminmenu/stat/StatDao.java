package com.scglab.connect.services.adminmenu.stat;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.scglab.connect.services.common.dao.CommonDao;

@Repository
public class StatDao extends CommonDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * XML의 매핑되는 prefix namespace
	 * ex. sdtalk.sample.selectList => sdtalk.sample 
	 */
	public String namespace = "sdtalk.admin.stat.";
	
	@Override
	protected String getNamespace() {
		return namespace;
	}
	
	public List<Map<String, Object>> selectStat1(Map<String, Object> params){
		String mapperId = getNamespace() + "selectStat1";
		return this.sqlSession.selectList(mapperId, params);
	}
	
	public List<Map<String, Object>> selectStat2(Map<String, Object> params){
		String mapperId = getNamespace() + "selectStat2";
		return this.sqlSession.selectList(mapperId, params);
	}
}