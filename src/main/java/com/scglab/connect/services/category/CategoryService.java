package com.scglab.connect.services.category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.auth.User;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;

@Service
public class CategoryService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private CategoryDao categoryDao;
	@Autowired private LoginService loginService;
	
	public Map<String, Object> total(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<Map<String, Object>> large = this.categoryDao.findCategoryLarge(params);
		List<Map<String, Object>> middle = this.categoryDao.findCategoryMiddle(params);
		List<Map<String, Object>> small = this.categoryDao.findCategorySmall(params);
		
		data.put("large", large);
		data.put("middle", middle);
		data.put("small", small);
		
		return data;
	}
	/**
	 * 
	 * @Method Name : categories
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 카테고리 검색.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> categories(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		
		String type = DataUtils.getString(params, "type", "");
		if(type.equals("large")) {			// 대분류
			list = this.categoryDao.findCategoryLarge(params);
			
		}else if(type.equals("middle")) {	// 중분류
			list = this.categoryDao.findCategoryMiddle(params);
			
		}else if(type.equals("small")) {	// 소분류
			list = this.categoryDao.findCategorySmall(params);
			
		}
		return list;
	}
	
	/**
	 * 
	 * @Method Name : category
	 * @작성일 : 2020. 11. 26.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 카테고리 상세조회.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> category(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Map<String, Object> data = null;
		
		String type = DataUtils.getString(params, "type", "");
		if(type.equals("large")) {			// 대분류
			data = this.categoryDao.getCategoryLarge(params);
			
		}else if(type.equals("middle")) {	// 중분류
			data = this.categoryDao.getCategoryMiddle(params);
			
		}else if(type.equals("small")) {	// 소분류
			data = this.categoryDao.getCategorySmall(params);
			
		}
		
		return data;
	}
	
	/**
	 * 
	 * @Method Name : save
	 * @작성일 : 2020. 11. 26.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 카테고리 등록
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> save(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		String type = DataUtils.getString(params, "type", "");
		if(type.equals("large")) {			// 대분류
			if(this.categoryDao.createCategoryLarge(params) > 0) {
				data = this.categoryDao.getCategoryLarge(params);
			}
			data = this.categoryDao.getCategoryLarge(params);
			
		}else if(type.equals("middle")) {	// 중분류
			data = this.categoryDao.getCategoryMiddle(params);
			
		}else if(type.equals("small")) {	// 소분류
			data = this.categoryDao.getCategorySmall(params);
			
		}
		
		int result = this.categoryDao.insert(params);
		if(result > 0) {
			Map<String, Object> category = this.categoryDao.selectOne(params);
			data.put("category", category);
		}
		data.put("isSuccess", result > 0 ? true : false);
		return data;
	}
	
	public Map<String, Object> update(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.categoryDao.update(params);
		if(result > 0) {
			Map<String, Object> category = this.categoryDao.selectOne(params);
			data.put("category", category);
		}
		data.put("isSuccess", result > 0 ? true : false);
		return data;
	}
	
	public Map<String, Object> delete(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.categoryDao.delete(params);
		data.put("isSuccess", result > 0 ? true : false);
		return data;
	}
}
