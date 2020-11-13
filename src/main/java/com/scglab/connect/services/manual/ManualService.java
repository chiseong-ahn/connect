package com.scglab.connect.services.manual;

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
import com.scglab.connect.utils.DataUtils;

@Service
@SuppressWarnings("unused")
public class ManualService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ManualDao manualDao;
	
	/**
	 * 
	 * @Method Name : manuals
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 매뉴얼 검색.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> manuals(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		// 전체 카운트 조회
		int totalCount = this.manualDao.findManualCount(params);
		data.put("totalCount", totalCount);
		
		if(totalCount > 0) {
			
			// 매뉴얼이 존재할 경우 목록 조회
			List<Manual> list = this.manualDao.findManuals(params);
			if(list != null) {
				data.put("data", list);
			}
		}
		
		return data;
	}
	
	/**
	 * 
	 * @Method Name : manual
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 매뉴얼 상세조회.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Manual manual(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 매뉴얼 조회
		return this.manualDao.findManual(params);
	}
	
	/**
	 * 
	 * @Method Name : tags
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 매뉴얼의 태그목록 조회.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public List<String> tags(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 태그목록 조회
		return this.manualDao.findTags(params);
	}
	
	/**
	 * 
	 * @Method Name : favorite
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 즐겨찾기 등록/삭제.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> favorite(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		return data;
	}
	
	/**
	 * 
	 * @Method Name : regist
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 매뉴얼 등록
	 * @param params
	 * @param request
	 * @param response : 등록된 매뉴얼.
	 * @return
	 * @throws Exception
	 */
	public Manual regist(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Manual manual = null;
		
		int result = this.manualDao.insertManual(params);
		if(result > 0) {
			Long id = DataUtils.getLong(params, "id", 0);
			if(id > 0) {
				manual = this.manualDao.findManual(params);
			}
		}
		
		return manual;
	}
	
	/**
	 * 
	 * @Method Name : update
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 매뉴얼 수정.
	 * @param params
	 * @param request
	 * @param response : 수정된 매뉴얼.
	 * @return
	 * @throws Exception
	 */
	public Manual update(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Manual manual = null;
		
		int result = this.manualDao.updateManual(params);
		if(result > 0) {
			manual = this.manualDao.findManual(params);
		}
		
		return manual;
	}
	
	/**
	 * 
	 * @Method Name : delete
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 매뉴얼 삭제.
	 * @param params
	 * @param request
	 * @param response 성공여부(true, false)
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> delete(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		int result = this.manualDao.deleteManual(params);
		
		data.put("success", result > 0 ? true : false);
		return data;
	}
	
	/**
	 * 
	 * @Method Name : nextPageNumber
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 다음페이지번호 조회.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> nextPageNumber(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.manualDao.findNextPageNumber(params);
	}
	
}