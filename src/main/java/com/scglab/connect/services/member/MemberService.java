package com.scglab.connect.services.member;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.service.MessageService;

@Service
public class MemberService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MemberDao memberDao;

	
	public Map<String, Object> state(Map<String, Object> params, int id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		if(id > 0) {
			params.put("id", id);
			int result = this.memberDao.update("api.member.state", params);
			data.put("success", result > 0 ? true : false);
		}else {
			Object[] errorParams = new String[1];
			errorParams[0] = "id";
			String message = this.messageService.getMessage("error.params.type4", errorParams);
			throw new RuntimeException(message);
		}
		
		return data;
	}
}