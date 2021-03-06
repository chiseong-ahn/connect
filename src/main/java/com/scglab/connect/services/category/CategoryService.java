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

import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.ErrorService;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;

@Service
public class CategoryService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CommonService commonService;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private LoginService loginService;
	@Autowired
	private ErrorService errorService;
	@Autowired
	private MessageHandler messageHandler;

	/**
	 * 
	 * @Method Name : total
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 카테고리 조회 (대중소 각각)
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * 
	 * @Method Name : tree
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 전체 카테고리 트리형태로 조회
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<CategoryLarge> tree(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		String isMinwon = DataUtils.getString(params, "isMinwon", Constant.NO);
		params.put("isMinwon", isMinwon);

		List<CategoryLarge> largeList = this.categoryDao.findCategoryLarge(params);
		List<CategoryMiddle> middleList = this.categoryDao.findCategoryMiddle(params);
		List<CategorySmall> smallList = this.categoryDao.findCategorySmall(params);

		// isMinwon=Y/N

		for (CategorySmall small : smallList) {
			small.setKey("c" + small.getId());
			small.setTitle(small.getName());
			small.setLevel(3);
		}

		for (CategoryMiddle middle : middleList) {
			middle.setKey("b" + middle.getId());
			middle.setTitle(middle.getName());
			middle.setLevel(2);

			middle.setChildren(smallList.stream().filter(small -> small.getCategoryMiddleId() == middle.getId())
					.collect(Collectors.toList()));
		}

		for (CategoryLarge large : largeList) {
			large.setKey("a" + large.getId());
			large.setTitle(large.getName());
			large.setLevel(1);
			large.setChildren(middleList.stream().filter(middle -> middle.getCategoryLargeId() == large.getId())
					.collect(Collectors.toList()));
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
	public List<CategoryLarge> categoryLargeList(Map<String, Object> params, HttpServletRequest request)
			throws Exception {
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
	public List<CategoryMiddle> categoryMiddleList(Map<String, Object> params, HttpServletRequest request)
			throws Exception {
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
	public List<CategorySmall> categorySmallList(Map<String, Object> params, HttpServletRequest request)
			throws Exception {
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

	/**
	 * 
	 * @Method Name : categoryMiddle
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 중분류 카테고리 상세 조회
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public CategoryMiddle categoryMiddle(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		CategoryMiddle categoryMiddle = this.categoryDao.getCategoryMiddle(params);
		return categoryMiddle == null ? new CategoryMiddle() : categoryMiddle;
	}

	/**
	 * 
	 * @Method Name : categorySmall
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 소분류 카테고리 상세 조회.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
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

		String errorParams = "";
		if (!this.commonService.valid(params, "name"))
			errorParams = this.commonService.appendText(errorParams, "대분류명-name");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		int lastSortIndex = this.categoryDao.getLastLargeSortIndex(params);
		params.put("sortIndex", lastSortIndex);

		CategoryLarge category = null;
		if (this.categoryDao.createCategoryLarge(params) > 0) {
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

		String errorParams = "";
		if (!this.commonService.valid(params, "categoryLargeId"))
			errorParams = this.commonService.appendText(errorParams, "대분류id-categoryLargeId");

		if (!this.commonService.valid(params, "name"))
			errorParams = this.commonService.appendText(errorParams, "중분류명-name");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		int lastSortIndex = this.categoryDao.getLastMiddleSortIndex(params);
		params.put("sortIndex", lastSortIndex);

		CategoryMiddle category = null;
		if (this.categoryDao.createCategoryMiddle(params) > 0) {
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

		String errorParams = "";
		if (!this.commonService.valid(params, "categoryMiddleId"))
			errorParams = this.commonService.appendText(errorParams, "중분류id-categoryMiddleId");

		if (!this.commonService.valid(params, "name"))
			errorParams = this.commonService.appendText(errorParams, "소분류명-name");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		int lastSortIndex = this.categoryDao.getLastSmallSortIndex(params);
		params.put("sortIndex", lastSortIndex);

		CategorySmall category = null;
		if (this.categoryDao.createCategorySmall(params) > 0) {
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

		String errorParams = "";
		if (!this.commonService.valid(params, "name"))
			errorParams = this.commonService.appendText(errorParams, "대분류명-name");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		CategoryLarge category = null;
		if (this.categoryDao.updateCategoryLarge(params) > 0) {
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
	public CategoryMiddle updateCategoryMiddle(Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		String errorParams = "";
		if (!this.commonService.valid(params, "categoryLargeId"))
			errorParams = this.commonService.appendText(errorParams, "대분류id-categoryLargeId");

		if (!this.commonService.valid(params, "name"))
			errorParams = this.commonService.appendText(errorParams, "중분류명-name");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		CategoryMiddle category = null;
		if (this.categoryDao.updateCategoryMiddle(params) > 0) {
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

		String errorParams = "";
		if (!this.commonService.valid(params, "categoryMiddleId"))
			errorParams = this.commonService.appendText(errorParams, "중분류id-categoryMiddleId");

		if (!this.commonService.valid(params, "name"))
			errorParams = this.commonService.appendText(errorParams, "소분류명-name");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		CategorySmall category = null;
		if (this.categoryDao.updateCategorySmall(params) > 0) {
			category = this.categoryDao.getCategorySmall(params);
		}

		return category == null ? new CategorySmall() : category;
	}

	/**
	 * 
	 * @Method Name : deleteCategoryLarge
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 대분류 카테고리 삭제.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> deleteCategoryLarge(Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		int result = this.categoryDao.deleteCategoryLarge(params);

		Map<String, Object> data = new HashMap<String, Object>();

		data.put("isSuccess", result > 0 ? true : false);
		return data;
	}

	/**
	 * 
	 * @Method Name : deleteCategoryMiddle
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 중분류 카테고리 삭제.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> deleteCategoryMiddle(Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		int result = this.categoryDao.deleteCategoryMiddle(params);

		Map<String, Object> data = new HashMap<String, Object>();

		data.put("isSuccess", result > 0 ? true : false);
		return data;
	}

	/**
	 * 
	 * @Method Name : deleteCategorySmall
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 소분류 카테고리 삭제.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> deleteCategorySmall(Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		int result = this.categoryDao.deleteCategorySmall(params);

		Map<String, Object> data = new HashMap<String, Object>();

		data.put("isSuccess", result > 0 ? true : false);
		return data;
	}

	/**
	 * 
	 * @Method Name : updateSortIndexCategoryLarge
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 대분류 카테고리 정렬순서 변경.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public CategoryLarge updateSortIndexCategoryLarge(Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("updateMemberId", member.getId());

		String errorParams = "";
		if (!this.commonService.validInteger(params, "sortIndex"))
			errorParams = this.commonService.appendText(errorParams, "정렬순번-sortIndex");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		CategoryLarge category = null;

		// 카테고리 조회.
		// 영향을 받는 다른카테고리의 순번 변경.
		this.categoryDao.updateLargeSortIndexToAfter(params);
		this.categoryDao.updateLargeSortIndex(params);

		// 해당하는 카테고리의 순번 변경.
		category = this.categoryDao.getCategoryLarge(params);

		return category == null ? new CategoryLarge() : category;
	}

	/**
	 * 
	 * @Method Name : updateSortIndexCategoryMiddle
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 중분류 카테고리 정렬순서 변경.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public CategoryMiddle updateSortIndexCategoryMiddle(Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("updateMemberId", member.getId());

		String errorParams = "";
		if (!this.commonService.validInteger(params, "sortIndex"))
			errorParams = this.commonService.appendText(errorParams, "정렬순번-sortIndex");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		// 카테고리 조회.
		CategoryMiddle category = this.categoryDao.getCategoryMiddle(params);
		if (category == null) {
			return null;
		}
		params.put("categoryLargeId", category.getCategoryLargeId());

		// 영향을 받는 다른카테고리의 순번 변경.
		this.categoryDao.updateMiddleSortIndexToAfter(params);
		this.categoryDao.updateMiddleSortIndex(params);

		// 해당하는 카테고리의 순번 변경.
		category = this.categoryDao.getCategoryMiddle(params);

		return category == null ? new CategoryMiddle() : category;
	}

	/**
	 * 
	 * @Method Name : updateSortIndexCategorySmall
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 소분류 카테고리 정렬순서 변경.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public CategorySmall updateSortIndexCategorySmall(Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("updateMemberId", member.getId());

		String errorParams = "";
		if (!this.commonService.validInteger(params, "sortIndex"))
			errorParams = this.commonService.appendText(errorParams, "정렬순번-sortIndex");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		// 카테고리 조회.
		CategorySmall category = this.categoryDao.getCategorySmall(params);
		if (category == null) {
			return null;
		}
		params.put("categoryMiddleId", category.getCategoryMiddleId());

		// 영향을 받는 다른카테고리의 순번 변경.
		this.categoryDao.updateSmallSortIndexToAfter(params);
		this.categoryDao.updateSmallSortIndex(params);

		// 해당하는 카테고리의 순번 변경.
		category = this.categoryDao.getCategorySmall(params);

		return category == null ? new CategorySmall() : category;
	}
}
