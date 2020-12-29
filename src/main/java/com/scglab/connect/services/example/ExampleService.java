package com.scglab.connect.services.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.utils.DataUtils;

@Service
public class ExampleService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ExampleDao exampleDao;
	
	@Autowired
	private MessageHandler messageService;
	
	public Map<String, Object> selectAll(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.exampleDao.selectAll(params);
		data.put("total", 100);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> selectOne(Map<String, Object> params, String id) throws Exception {
		return this.exampleDao.selectOne(params);
	}
	
	public Map<String, Object> insert(Map<String, Object> params) throws Exception {
		Map<String, Object> resultData = new HashMap<String, Object>();
		String name = DataUtils.getString(params, "name", "");
		
		// 파라미터(name)이 없을 경우.
		if(name.equals("")) {
			Object[] errorParams = new String[1];
			errorParams[0] = "name";		// 오류를 유발한 파라미터 명.
			
			// 오류 사유
			String reason = messageService.getMessage("error.params.type1", errorParams);
			
			// 오류 유발.
			throw new RuntimeException(reason);
			
		// 파라미터(name)의 길이가 허용범위가 아닐경우.
		}else if(name.length() < 2 || name.length() > 4) {
			Object[] errorParams = new String[2];
			errorParams[0] = "name";
			errorParams[1] = "2~4자";
			
			// 오류 사유.
			String reason = messageService.getMessage("error.parameter2", errorParams);
			
			// 오류 유발.
			throw new RuntimeException(reason);
		}
		
		// 등록이 성공한 경우에 게시물정보 조회.
		if(this.exampleDao.insert(params) > 0) {
			
			// 등록된 게시물 정보 조회.
			return this.exampleDao.selectOne(params);
		}
		
		return null;
	}
	
	public Map<String, Object> update(Map<String, Object> params) throws Exception {
		Map<String, Object> resultData = new HashMap<String, Object>();
		long id = DataUtils.getLong(params, "id", 0);
		String name = DataUtils.getString(params, "name", "");
		
		// 파라미터(name)이 없을 경우.
		if(id == 0 || name.equals("")) {
			Object[] errorParams = new String[1];
			errorParams[0] = id == 0 ? "id" : "name";		// 오류를 유발한 파라미터 명.
			
			// 오류 사유
			String reason = messageService.getMessage("error.params.type1", errorParams);
			
			// 오류 유발.
			throw new RuntimeException(reason);
			
		// 파라미터(name)의 길이가 허용범위가 아닐경우.
		}else if(name.length() < 2 || name.length() > 4) {
			Object[] errorParams = new String[2];
			errorParams[0] = "name";
			errorParams[1] = "2~4자";
			
			// 오류 사유.
			String reason = messageService.getMessage("error.parameter2", errorParams);
			
			// 오류 유발.
			throw new RuntimeException(reason);
		}
		
		if(this.exampleDao.update(params) > 0) {
			return this.exampleDao.selectOne(params);
		}
		
		return null;
	}
	
	public Map<String, Object> delete(Map<String, Object> params) throws Exception {
		Map<String, Object> resultData = new HashMap<String, Object>();
		long id = DataUtils.getLong(params, "id", 0);
		
		// 파라미터(name)이 없을 경우.
		if(id == 0) {
			Object[] errorParams = new String[1];
			errorParams[0] = "id";		// 오류를 유발한 파라미터 명.
			
			// 오류 사유
			String reason = messageService.getMessage("error.params.type1", errorParams);
			
			// 오류 유발.
			throw new RuntimeException(reason);
		}
		
		if(this.exampleDao.delete(params) > 0) {
			resultData.put("isSuccess", true);
		}else {
			resultData.put("isSuccess", false);
		}
		
		return resultData;
	}
}
