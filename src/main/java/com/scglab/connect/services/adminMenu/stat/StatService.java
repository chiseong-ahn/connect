package com.scglab.connect.services.adminMenu.stat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.utils.DataUtils;

@Service
public class StatService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private StatDao statDao;
	
	public Map<String, Object> stat1(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<Map<String, Object>> list = this.statDao.selectStat1(params);
		
		data.put("data", list);
		
		return data;
	}
	
	public Map<String, Object> stat2(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		String startDate = DataUtils.getObjectValue(params, "startDate", "");
		String endDate = DataUtils.getObjectValue(params, "endDate", "");
		
		params.put("startDatetime", startDate + " 00:00");
		params.put("endDatetime", endDate + " 23:59:59.9");
		
		List<Map<String, Object>> list = this.statDao.selectStat2(params);
		
		data.put("data", list);
		
		return data;
	}
}
