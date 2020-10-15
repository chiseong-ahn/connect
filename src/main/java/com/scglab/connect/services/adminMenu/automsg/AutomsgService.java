package com.scglab.connect.services.adminMenu.automsg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.auth.AuthService;
import com.scglab.connect.services.common.auth.User;

@Service
public class AutomsgService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AutomsgDao empDao;
	
	@Autowired
	private AuthService authService;
	
	public Map<String, Object> list(Map<String, Object> params, HttpServletRequest request) throws Exception {
		User user = this.authService.getUserInfo(request);
		params.put("cid", user.getCid());
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<Map<String, Object>> list = this.empDao.selectAll(params);
		int count = list == null ? 0 : list.size();
		
		data.put("total", count);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> save(Map<String, Object> params, HttpServletRequest request) throws Exception {
		User user = this.authService.getUserInfo(request);
		params.put("cid", user.getCid());
		params.put("emp", user.getEmp());
		
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.empDao.insert(params);
		data.put("result", result > 0 ? true : false);
		return data;
	}
	
	public Map<String, Object> update(Map<String, Object> params, HttpServletRequest request) throws Exception {
		User user = this.authService.getUserInfo(request);
		params.put("cid", user.getCid());
		params.put("emp", user.getEmp());
		
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.empDao.update(params);
		data.put("result", result > 0 ? true : false);
		return data;
	}
	
	public Map<String, Object> delete(Map<String, Object> params, HttpServletRequest request) throws Exception {
		User user = this.authService.getUserInfo(request);
		params.put("cid", user.getCid());
		
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.empDao.delete(params);
		data.put("result", result > 0 ? true : false);
		return data;
	}
}
