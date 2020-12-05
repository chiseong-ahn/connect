package com.scglab.connect.services.category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
		
		List<CategoryLarge> large = this.categoryDao.findCategoryLarge(params);
		List<CategoryMiddle> middle = this.categoryDao.findCategoryMiddle(params);
		List<CategorySmall> small = this.categoryDao.findCategorySmall(params);
		
		data.put("large", large);
		data.put("middle", middle);
		data.put("small", small);
		
		return data;
	}
	
	public List<CategoryLarge> tree(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		List<CategoryLarge> largeList = this.categoryDao.findCategoryLarge(params);
		List<CategoryMiddle> middleList = this.categoryDao.findCategoryMiddle(params);
		List<CategorySmall> smallList = this.categoryDao.findCategorySmall(params);
		
		for(CategorySmall small : smallList) {
			small.setKey("c" + small.getId());
			small.setTitle(small.getName());
			small.setLevel(3);
		}
		
		for(CategoryMiddle middle : middleList) {
			middle.setKey("b" + middle.getId());
			middle.setTitle(middle.getName());
			middle.setLevel(2);
			
			middle.setChildren(smallList.stream().filter(small -> small.getCategoryMiddleId() == middle.getId()).collect(Collectors.toList()));
		}
		
		for(CategoryLarge large : largeList) {
			large.setKey("a" + large.getId());
			large.setTitle(large.getName());
			large.setLevel(1);
			large.setChildren(middleList.stream().filter(middle -> middle.getCategoryLargeId() == large.getId()).collect(Collectors.toList()));
		}
		
		return largeList;
	}
	
	/**
	 * 
	 * @Method Name : categories
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 카테고리 대분류 조회.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<CategoryLarge> categoryLargeList(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		return this.categoryDao.findCategoryLarge(params);
	}
	
	/**
	 * 
	 * @Method Name : categoryMiddleList
	 * @작성일 : 2020. 12. 5.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 카테고리 중분류 조회.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<CategoryMiddle> categoryMiddleList(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		return this.categoryDao.findCategoryMiddle(params);
	}
	
	/**
	 * 
	 * @Method Name : categorySmallList
	 * @작성일 : 2020. 12. 5.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 카테고리 소분류 조회.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<CategorySmall> categorySmallList(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		return this.categoryDao.findCategorySmall(params);
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
	public CategoryLarge categoryLarge(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		CategoryLarge categoryLarge = this.categoryDao.getCategoryLarge(params);
		return categoryLarge == null ? new CategoryLarge() : categoryLarge;
	}
	
	public CategoryMiddle categoryMiddle(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		CategoryMiddle categoryMiddle = this.categoryDao.getCategoryMiddle(params);
		return categoryMiddle == null ? new CategoryMiddle() : categoryMiddle;
	}
	
	public CategorySmall categorySmall(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		CategorySmall categorySmall = this.categoryDao.getCategorySmall(params);
		return categorySmall == null ? new CategorySmall() : categorySmall;
	}
	
	/**
	 * 
	 * @Method Name : saveCategoryLarge
	 * @작성일 : 2020. 11. 26.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 대분류 카테고리 등록
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public CategoryLarge saveCategoryLarge(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		CategoryLarge category = null;
		if(this.categoryDao.createCategoryLarge(params) > 0) {
			category = this.categoryDao.getCategoryLarge(params);
		}
		return category == null ? new CategoryLarge() : category;
	}
	
	/**
	 * 
	 * @Method Name : saveCategoryMiddle
	 * @작성일 : 2020. 12. 5.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 중분류 카테고리 등록.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public CategoryMiddle saveCategoryMiddle(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		CategoryMiddle category = null;
		if(this.categoryDao.createCategoryLarge(params) > 0) {
			category = this.categoryDao.getCategoryMiddle(params);
		}
		return category == null ? new CategoryMiddle() : category;
	}
	
	
	/**
	 * 
	 * @Method Name : saveCategorySmall
	 * @작성일 : 2020. 12. 5.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 소분류 카테고리 등록.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public CategorySmall saveCategorySmall(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		CategorySmall category = null;
		if(this.categoryDao.createCategoryLarge(params) > 0) {
			category = this.categoryDao.getCategorySmall(params);
		}
		return category == null ? new CategorySmall() : category;
	}
	
	
	/**
	 * 
	 * @Method Name : updateCategoryLarge
	 * @작성일 : 2020. 12. 5.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 대분류 카테고리 수정.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public CategoryLarge updateCategoryLarge(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		CategoryLarge category = null;
		if(this.categoryDao.updateCategoryLarge(params) > 0) {
			category = this.categoryDao.getCategoryLarge(params);
		}
		
		return category == null ? new CategoryLarge() : category;
	}
	
	
	/**
	 * 
	 * @Method Name : updateCategoryMiddle
	 * @작성일 : 2020. 12. 5.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 중분류 카테고리 수정.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public CategoryMiddle updateCategoryMiddle(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		CategoryMiddle category = null;
		if(this.categoryDao.updateCategoryMiddle(params) > 0) {
			category = this.categoryDao.getCategoryMiddle(params);
		}
		
		return category == null ? new CategoryMiddle() : category;
	}
	
	
	/**
	 * 
	 * @Method Name : updateCategorySmall
	 * @작성일 : 2020. 12. 5.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 소분류 카테고리 수정.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public CategorySmall updateCategorySmall(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		CategorySmall category = null;
		if(this.categoryDao.updateCategorySmall(params) > 0) {
			category = this.categoryDao.getCategorySmall(params);
		}
		
		return category == null ? new CategorySmall() : category;
	}
	
	public Map<String, Object> delete(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		String type = DataUtils.getString(params, "type", "");
		
		int result = 0;
		switch(type) {
		case "large":
			result = this.categoryDao.deleteCategoryLarge(params);
			break;
			
		case "middle" :
			result = this.categoryDao.deleteCategoryMiddle(params);
			break;
			
		case "small" :
			result = this.categoryDao.deleteCategorySmall(params);
			break;
			
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("isSuccess", result > 0 ? true : false);
		return data;
	}
	
	public CategoryLarge updateSortIndexCategoryLarge(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("updateMemberId", member.getId());
		
		CategoryLarge category = null;
		if(this.categoryDao.updateLargeSortIndexToAfter(params) > 0) {
			category = this.categoryDao.getCategoryLarge(params);
		}
		
		return category == null ? new CategoryLarge() : category;
	}
	
	public CategoryMiddle updateSortIndexCategoryMiddle(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("updateMemberId", member.getId());
		
		CategoryMiddle category = null;
		if(this.categoryDao.updateMiddleSortIndexToAfter(params) > 0) {
			category = this.categoryDao.getCategoryMiddle(params);
		}
		
		return category == null ? new CategoryMiddle() : category;
	}
	
	public CategorySmall updateSortIndexCategorySmall(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("updateMemberId", member.getId());
		params.put("categoryMiddleId", DataUtils.getLong(params, "category_middle_id", 0));
		
		CategorySmall category = null;
		if(this.categoryDao.updateSmallSortIndexToAfter(params) > 0) {
			category = this.categoryDao.getCategorySmall(params);
		}
		
		return category == null ? new CategorySmall() : category;
	}
}