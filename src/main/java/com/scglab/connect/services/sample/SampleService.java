package com.scglab.connect.services.sample;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.utils.DataUtils;

import io.swagger.v3.oas.annotations.Operation;

@Service
public class SampleService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SampleDao sampleDao;
	
	@Autowired
	private MessageHandler messageService;
	
	public Map<String, Object> selectAll(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.sampleDao.selectAll(params);
		data.put("total", 100);
		data.put("list", list);
		
		return data;
	}
	
	public Map<String, Object> selectOne(Map<String, Object> params, String id) throws Exception {
		params.put("id", id);
		Map<String, Object> object = this.sampleDao.selectOne(params);
		return object;
	}
	
	@Operation(summary = "게시물 등록처리.", description = "파라미터(name)을 입력받아 게시물을 등록한다.")
	public Map<String, Object> insert(Map<String, Object> params) throws Exception {
		Map<String, Object> resultData = new HashMap<String, Object>();
		String name = DataUtils.getString(params, "name", "");
		
		// 파라미터(name)이 없을 경우.
		if(name.equals("")) {
			Object[] errorParams = new String[1];
			errorParams[0] = "name";		// 오류를 유발한 파라미터 명.
			
			// 오류 사유
			String reason = messageService.getMessage("error.parameter1", errorParams);
			
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
		
		// 게시물 등록.
		int result = this.sampleDao.insert(params);
		
		// 등록이 성공한 경우에 게시물정보 조회.
		if(result > 0) {
			
			// 등록된 게시물 id(PK) 조회.
			BigInteger id = DataUtils.getBigInteger(params, "id", BigInteger.ZERO);
			params.put("id", id);
			
			// 등록된 게시물 정보 조회.
			Map<String, Object> sample = this.sampleDao.selectOne(params);
			resultData.put("data", sample);
		}
		
		// 성공여부(true-성공, false-실패)
		resultData.put("isSuccess", result > 0 ? true : false);
		
		return resultData;
	}
	
	public Map<String, Object> update(Map<String, Object> params) throws Exception {
		Map<String, Object> resultData = new HashMap<String, Object>();
		String id = DataUtils.getString(params, "id", "");
		String name = DataUtils.getString(params, "name", "");
		
		// 파라미터(name)이 없을 경우.
		if(id.equals("") || name.equals("")) {
			Object[] errorParams = new String[1];
			errorParams[0] = id.equals("") ? "id" : "name";		// 오류를 유발한 파라미터 명.
			
			// 오류 사유
			String reason = messageService.getMessage("error.parameter1", errorParams);
			
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
		
		int result = this.sampleDao.update(params);
		
		// 수정이 되지 않은 경우 오류 유발.
		if(result == 0) {
			Object[] errorParams = new String[1];
			errorParams[0] = id;
			
			// 오류 사유.
			String reason = messageService.getMessage("error.update1");
			
			// 오류 유발.
			throw new RuntimeException(reason);
		}
			
		// 수정된 게시물 정보 조회.
		Map<String, Object> sample = this.sampleDao.selectOne(params);
		resultData.put("data", sample);
		
		// 성공여부(true-성공, false-실패)
		resultData.put("isSuccess", result > 0 ? true : false);
		
		return resultData;
	}
	
	public Map<String, Object> delete(Map<String, Object> params) throws Exception {
		Map<String, Object> resultData = new HashMap<String, Object>();
		String id = DataUtils.getString(params, "id", "");
		
		// 파라미터(name)이 없을 경우.
		if(id.equals("")) {
			Object[] errorParams = new String[1];
			errorParams[0] = "id";		// 오류를 유발한 파라미터 명.
			
			// 오류 사유
			String reason = messageService.getMessage("error.parameter1", errorParams);
			
			// 오류 유발.
			throw new RuntimeException(reason);
		}
		
		int result = this.sampleDao.delete(params);
		
		// 삭제가 되지 않은 경우 오류 유발.
		if(result == 0) {
			Object[] errorParams = new String[1];
			errorParams[0] = id;
			
			// 오류 사유.
			String reason = messageService.getMessage("error.update1");
			
			// 오류 유발.
			throw new RuntimeException(reason);
		}
		
		// 성공여부(true-성공, false-실패)
		resultData.put("isSuccess", result > 0 ? true : false);
		
		return resultData;
	}
}
