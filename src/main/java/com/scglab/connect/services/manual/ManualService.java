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

import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;

@Service
@SuppressWarnings("unused")
public class ManualService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private MessageHandler messageService;
	@Autowired private LoginService loginService;
	@Autowired private ManualDao manualDao;
	
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
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		// 페이지 번호.
		int page = Integer.parseInt(DataUtils.getString(params, "page", "1"));
		page = page < 1 ? 1 : page;
		
		// 페이지당 노출할 게시물 수.
		int pageSize = Integer.parseInt(DataUtils.getString(params, "pageSize", "10"));
		pageSize = pageSize < 1 ? 10 : pageSize;
		
		// 조회 시작 번호.
		int startNum = (page - 1) * pageSize + 1;
		
		params.put("startNum", startNum);
		params.put("pageSize", pageSize);
		
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
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
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
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
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
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		String value = DataUtils.getString(params, "value", "false");
		
		int result = 0;
		if(value.equals("true")) {
			result = this.manualDao.createFavorite(params);
		}else {
			result = this.manualDao.deleteFavoriteToMember(params);
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("success", result > 0 ? true : false);
		
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
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Manual manual = null;
		
		if(this.manualDao.insertManual(params) > 0) {
				manual = this.manualDao.findManual(params);
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
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
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
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
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
		Map<String, Object> data = new HashMap<String, Object>(); 
		int nextPageNumber = this.manualDao.getNextPageNumber(params);
		data.put("nextPageNumber", nextPageNumber);
		return data;
	}
	
}