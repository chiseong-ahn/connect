package com.scglab.connect.services.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.category.CategoryDao;
import com.scglab.connect.services.category.CategoryMiddle;
import com.scglab.connect.services.category.CategorySmall;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;

@Service
public class TemplateService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private TemplateDao templateDao;
	@Autowired private CategoryDao categoryDao;
	
	@Autowired
	private MessageHandler messageService;
	
	@Autowired
	private LoginService loginService;
	
	/**
	 * 
	 * @Method Name : findAll
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 답변템플릿 목록 조회
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findAll(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		// 페이지 번호.
		int page = Integer.parseInt(DataUtils.getObjectValue(params, "page", "1"));
		page = page < 1 ? 1 : page;
		
		// 페이지당 노출할 게시물 수.
		int pageSize = Integer.parseInt(DataUtils.getObjectValue(params, "pageSize", "10"));
		pageSize = pageSize < 1 ? 10 : pageSize;
		
		// 조회 시작 번호.
		int startNum = (page - 1) * pageSize;
		
		params.put("startNum", startNum);
		params.put("pageSize", pageSize);

		this.logger.debug("params : " + params.toString());
		
		int totalCount = this.templateDao.findAllCount(params);
		List<Map<String, Object>> list = null;
		if(totalCount > 0) {
			list = this.templateDao.findAll(params);
		}
		
		
		data.put("totalCount", totalCount);
		data.put("data", list);
		
		return data;
	}
	
	/**
	 * 
	 * @Method Name : findTemplate
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 답변템플릿 상세 조회
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findTemplate(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		this.logger.debug("params : " + params.toString());
		return this.templateDao.getDetail(params);
	}
	
	public Map<String, Object> saveKeyword(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("company", member.getCompanyId());
		params.put("memberId", member.getId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		this.logger.debug("params : " + params.toString());
		int count = this.templateDao.selectCountKeyword(params);
		if(count == 0) {
			this.logger.debug("params : " + params.toString());
			this.templateDao.insertKeyword(params);
		}
		
		this.logger.debug("params : " + params.toString());
		Map<String, Object> keyword = this.templateDao.selectKeyword(params);
		
		data.put("keyword", keyword);
		return data;
	}
	
	/**
	 * 
	 * @Method Name : create
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 답변템플릿 등록
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> create(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Map<String, Object> data = null;
		int result = 0;
		
		// 템플릿 등록.
		result = this.templateDao.create(params);
		
		if(result > 0) {	// 등록 성공
			this.logger.debug("generated key : " + params.get("id"));
			
			if(params.containsKey("keywords")) {
				String[] keywords = DataUtils.getObjectValue(params, "keywords", "").split(",");
				this.logger.debug("keywords : " + keywords.toString());
				if(keywords.length > 0) {
					for(String keyword : keywords) {
						if(!DataUtils.getSafeValue(keyword).equals("")) {
							params.put("keyword", keyword.trim());
							
							this.logger.debug("params : " + params.toString());
							
							// 템플릿 키워드 연결.
							this.templateDao.insertTemplateKeyword(params);
						}
					}
				}
				
			}else{
				this.logger.debug("keywords is nothing!");
			}
			
			this.logger.debug("params : " + params.toString());
			
			// 등록된 템플릿 정보 조회.
			data = this.templateDao.getDetail(params);
			
		}else {
			throw new RuntimeException("error.template.regist");
		}
		
		return data;
	}
	
	
	/**
	 * 
	 * @Method Name : update
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 답변템플릿 수정.
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> update(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Map<String, Object> data = null;
		int result = 0;
		
		// 템플릿 정보 수정.
		result = this.templateDao.update(params);
		if(result > 0) {
			// 이전에 연결된 키워드정보 삭제.
			this.templateDao.deleteTemplateKeywords(params);
			if(params.containsKey("keywords")) {
				String[] keywords = DataUtils.getObjectValue(params, "keywords", "").split(",");
				this.logger.debug("keywords : " + keywords.toString());
			
				if(keywords.length > 0) {
					for(String keyword : keywords) {
						if(!DataUtils.getSafeValue(keyword).equals("")) {
							params.put("keyword", keyword.trim());
							
							// 신규 키워드 연결.
							this.templateDao.insertTemplateKeyword(params);
						}
					}
				}
				
			}else{
				this.logger.debug("keywords is nothing!");
			}
			
			data = this.templateDao.getDetail(params);
		}else {
			throw new RuntimeException("error.template.update");
		}
		
		return data;
	}
	
	/**
	 * 
	 * @Method Name : delete
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 답변템플릿 삭제
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> delete(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		int result = 0;
		
		Map<String, Object> template = this.templateDao.getDetail(params);
		if(template == null) {
			Object[] messageParams = new String[1];
			messageParams[0] = "id = " + DataUtils.getString(params, "id", "");
			data.put("reason", this.messageService.getMessage("error.template.notexist", messageParams));
		}else {
			
			// 이전에 연결된 키워드정보 삭제.
			this.templateDao.deleteTemplateKeywords(params);
			
			// 템플릿 삭제.
			result = this.templateDao.delete(params);
		}
		
		data.put("success", result > 0 ? true : false);
		return data;
	}
	
	/**
	 * 
	 * @Method Name : favorite
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 답변템플릿 즐겨찾기 등록/삭제
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> favorite(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		String value = DataUtils.getString(params, "value", "");
		int result = 0;
		if(value.equals("true")) {
			result = this.templateDao.insertFavorite(params);
		}else if(value.equals("false")) {
			result = this.templateDao.deleteFavorite(params);
		}
		
		data.put("success", result > 0 ? true : false);
		return data;
	}
	public List<CategoryMiddle> findByCategoryLargeId(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		return this.categoryDao.findCategoryMiddleByLargeId(params);
	}
	
	public List<CategorySmall> findByCategoryMiddleId(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		return this.categoryDao.findCategorySmallByMiddleId(params);
	}
	
	
}
