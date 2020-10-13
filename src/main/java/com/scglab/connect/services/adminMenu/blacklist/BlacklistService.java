package com.scglab.connect.services.adminMenu.blacklist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlacklistService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BlacklistDao blacklistDao;
	
	public Map<String, Object> list(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<Map<String, Object>> list = this.blacklistDao.selectAll(params);
		int count = list == null ? 0 : list.size();
		
		data.put("total", count);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> object(Map<String, Object> params, String id) throws Exception {
		Map<String, Object> object = this.blacklistDao.selectOne(params);
		return object;
	}
	
	public Map<String, Object> save(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.blacklistDao.insert(params);
		data.put("RESULT", result > 0 ? true : false);
		return data;
	}
	
	public Map<String, Object> update(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.blacklistDao.update(params);
		data.put("RESULT", result > 0 ? true : false);
		return data;
	}
	
	public Map<String, Object> delete(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.blacklistDao.delete(params);
		data.put("RESULT", result > 0 ? true : false);
		return data;
	}
}
