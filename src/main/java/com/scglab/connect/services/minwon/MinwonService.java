package com.scglab.connect.services.minwon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.service.MessageService;

@Service
public class MinwonService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MinwonDao minwonDao;
	
	/**
	 * 
	 * @Method Name : regist
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 민원 등록 
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Minwon regist(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Minwon minwon = null;
		
		int result = this.minwonDao.insertMinwon(params);
		if(result > 0) {
			minwon = this.minwonDao.findMinwon(params);
		}
		return minwon;
	}
	
}