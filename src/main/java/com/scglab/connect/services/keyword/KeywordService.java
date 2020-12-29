package com.scglab.connect.services.keyword;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.ErrorService;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;

@Service
public class KeywordService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private KeywordDao keywordDao;
	@Autowired private MessageHandler messageService;
	@Autowired private LoginService loginService;
	@Autowired private CommonService commonService;
	@Autowired private ErrorService errorService;
	
	/**
	 * 
	 * @Method Name : findAll
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 키워드 검색
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<Keyword> findAll(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		this.logger.debug("params : " + params.toString());
		List<Keyword> list = this.keywordDao.findAll(params);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : findByName
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 이름으로 키워드 검색
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Keyword findByName(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		String errorParams = "";
	    if(!this.commonService.valid(params, "name"))
	        // 파라미터가 존재하지 않는 파라미터 등록.
	        errorParams = this.commonService.appendText(errorParams, "키워드명-name");
	    
	    // 파라미터 유효성 검증.
	    if(!errorParams.equals("")) {
	        // 필수파라미터 누락에 따른 오류 유발처리.
	        this.errorService.throwParameterErrorWithNames(errorParams);
	    }
		
		this.logger.debug("params : " + params.toString());
		Keyword keyword = this.keywordDao.getByName(params);
		
		return keyword;
	}
	
	/**
	 * 
	 * @Method Name : findByTemplateId
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 템플릿에 속한 키워드 조회
	 * @param params 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<Keyword> findByTemplateId(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		String errorParams = "";
	    if(!this.commonService.valid(params, "templateId"))
	        // 파라미터가 존재하지 않는 파라미터 등록.
	        errorParams = this.commonService.appendText(errorParams, "템플릿id-templateId");
	    
	    // 파라미터 유효성 검증.
	    if(!errorParams.equals("")) {
	        // 필수파라미터 누락에 따른 오류 유발처리.
	        this.errorService.throwParameterErrorWithNames(errorParams);
	    }
		
		this.logger.debug("params : " + params.toString());
		List<Keyword> list = this.keywordDao.getByTemplateId(params);
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : regist
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 키워드 등록
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Keyword regist(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		String errorParams = "";
	    if(!this.commonService.valid(params, "name"))
	        // 파라미터가 존재하지 않는 파라미터 등록.
	        errorParams = this.commonService.appendText(errorParams, "키워드명-name");
	    
	    // 파라미터 유효성 검증.
	    if(!errorParams.equals("")) {
	        // 필수파라미터 누락에 따른 오류 유발처리.
	        this.errorService.throwParameterErrorWithNames(errorParams);
	    }
		
		Keyword keyword = this.keywordDao.getByName(params);
		if(keyword == null) {
			int result = this.keywordDao.insertKeyword(params);
			if(result > 0) {
				keyword = this.keywordDao.getDetail(params);
			}
		}
		
		return keyword;
	}
	
}
