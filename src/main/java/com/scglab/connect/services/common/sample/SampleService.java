package com.scglab.connect.services.common.sample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SampleDao sampleDao;
	
	public Map<String, Object> list(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.sampleDao.selectAll(params);
		data.put("total", 100);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> object(Map<String, Object> params, String id) throws Exception {
		params.put("id", id);
		Map<String, Object> object = this.sampleDao.selectOne(params);
		
		return object;
	}
	
	public Map<String, Object> save(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.sampleDao.insert(params);
		data.put("RESULT", result > 0 ? true : false);
		return data;
	}
	
	public Map<String, Object> update(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.sampleDao.update(params);
		data.put("RESULT", result > 0 ? true : false);
		return data;
	}
	
	public Map<String, Object> delete(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.sampleDao.delete(params);
		data.put("RESULT", result > 0 ? true : false);
		return data;
	}
}
