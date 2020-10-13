package com.scglab.connect.services.adminMenu.emp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private EmpDao empDao;
	
	public Map<String, Object> list(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<Map<String, Object>> list = null;
	 	int count = this.empDao.selectCount(params);
	 	
		if(count > 0) {
 			list = this.empDao.selectAll(params);
		}
		
		data.put("total", count);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> object(Map<String, Object> params, String id) throws Exception {
		params.put("id", id);
		Map<String, Object> object = this.empDao.selectOne(params);
		
		return object;
	}
	
	public Map<String, Object> save(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.empDao.insert(params);
		data.put("RESULT", result > 0 ? true : false);
		return data;
	}
	
	public Map<String, Object> update(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.empDao.update(params);
		data.put("RESULT", result > 0 ? true : false);
		return data;
	}
	
	public Map<String, Object> delete(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.empDao.delete(params);
		data.put("RESULT", result > 0 ? true : false);
		return data;
	}
}
