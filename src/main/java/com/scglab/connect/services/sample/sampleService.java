package com.scglab.connect.services.sample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class sampleService {
	
	Logger logger = LoggerFactory.getLogger(sampleService.class);
	
	@Autowired
	private sampleDao sampleDao;
	
	public Map<String, Object> list(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.sampleDao.selectAll(params);
		data.put("total", 100);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> object(String id, Map<String, Object> params) throws Exception {
		params.put("id", id);
//		Map<String, Object> object = this.sampleDao.custom(params);
		Map<String, Object> object = this.sampleDao.selectOne(params);
		
		return object;
	}
}
