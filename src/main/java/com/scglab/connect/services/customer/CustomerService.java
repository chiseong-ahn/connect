package com.scglab.connect.services.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.ErrorService;
import com.scglab.connect.services.common.service.JwtService;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.company.external.ICompany;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;

@Service
public class CustomerService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private MessageHandler messageService;
	@Autowired private CustomerDao customerDao;
	@Autowired private JwtService jwtService;
	@Autowired private LoginService loginService;
	@Autowired private CommonService commonService;
	@Autowired private ErrorService errorService;
	
	/**
	 * 
	 * @Method Name : findAll
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객 검색.
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> findAll(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		//페이지 번호.
		int page = Integer.parseInt(DataUtils.getObjectValue(params, "page", "1"));
		page = page < 1 ? 1 : page;
		
		// 페이지당 노출할 게시물 수.
		int pageSize = Integer.parseInt(DataUtils.getObjectValue(params, "pageSize", "10"));
		pageSize = pageSize < 1 ? 10 : pageSize;
		
		// 조회 시작 번호.
		int startNum = (page - 1) * pageSize;
		
		params.put("startNum", startNum);
		params.put("pageSize", pageSize);
				
		int totalCount = this.customerDao.findAllCount(params);
		List<VCustomer> list = new ArrayList<VCustomer>();
		if(totalCount > 0) {
			list = this.customerDao.findAll(params);
		}
		
		data.put("totalCount", totalCount);
		data.put("list", list);
		return data;
	}
	
	
	/**
	 * 
	 * @Method Name : block
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 관심고객 지정/해제
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public VCustomer block(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		String blockType = DataUtils.getString(params, "blockType", "");
		int isBlock = blockType.equals("") ? 0 : 1;		// 관심고객 지정/해제여부(1-지정, 0-해제)
		
		int result =  0;
		if(isBlock == 1) {
			result = this.customerDao.enableBlackStatus(params);
		}else {
			result = this.customerDao.disbleBlackStatus(params);
		} 
		VCustomer vcustomer = null;
		if(result > 0) {
			vcustomer = this.customerDao.findCustomer(params);
		}
		
		return vcustomer;
	}
	
	/**
	 * 
	 * @Method Name : unBlock
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 관심고객 해제.
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public VCustomer unBlock(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		VCustomer vcustomer = null;
		int result = this.customerDao.disbleBlackStatus(params);
		if(result > 0) {
			vcustomer = this.customerDao.findCustomer(params);
		}
		
		return vcustomer;
	}
	
	/**
	 * 
	 * @Method Name : token
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 사용자 토큰 발급.
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> token(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		this.customerDao.regist(params);
		VCustomer customer = this.customerDao.findByGassappMemberNumber(params);
		this.logger.debug("customer : " + customer);
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> customerMap = objectMapper.convertValue(customer, Map.class);
		String token = this.jwtService.generateToken(customerMap);
		data.put("token", token);
		
		return data;
	}
	
	/**
	 * 
	 * @Method Name : plusSwearCount
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 :
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> plusSwearCount(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		VCustomer customer = this.customerDao.findCustomer(params);
		if(customer.getSwearCount() >= 2) {
			this.customerDao.resetSwearCount(params);
			this.customerDao.resetInsultCount(params);
		}else {
			this.customerDao.plusSwearCount(params);
		}
		
		data.put("isSuccess", true);
		
		return data;
	}
	
	public Map<String, Object> plusInsultCount(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		VCustomer customer = this.customerDao.findCustomer(params);
		if(customer.getSwearCount() >= 2) {
			this.customerDao.resetSwearCount(params);
			this.customerDao.resetInsultCount(params);
		}else {
			this.customerDao.plusInsultCount(params);
		}
		
		data.put("isSuccess", true);
		
		return data;
	}
	
	
	/**
	 * 
	 * @Method Name : contrancts
	 * @작성일 : 2020. 12. 2.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객 사용계약 목록 조회.
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> contracts(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		ICompany company = this.commonService.getCompany(member.getCompanyId()); 
		
		String userno = DataUtils.getString(params, "userno", "");
		List<Map<String, Object>> list = company.contracts(userno);
		
		for(Map<String, Object> obj : list) {
			String useContractNum = DataUtils.getString(obj, "useContractNum", "");
			if(!useContractNum.equals("")) {
				Map<String, Object> contractInfo = company.contractInfo(useContractNum);
				obj.put("contract", contractInfo);
			}
		}
		
		return list;
	}
	
	/**
	 * 
	 * @Method Name : contractInfo
	 * @작성일 : 2020. 12. 2.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 사용계약번호 상세 정보
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> contractInfo(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		ICompany company = this.commonService.getCompany(member.getCompanyId());
		String useContractNum = DataUtils.getString(params, "useContractNum", "");
		if(!useContractNum.equals("")) {
			return company.contractInfo(useContractNum);
		}
		return null;
	}
	
	/**
	 * 
	 * @Method Name : contractBilDetail
	 * @작성일 : 2020. 12. 3.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 사용계약번호 결제 상세 정보
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> contractBilDetail(Map<String, Object> params, HttpServletRequest request) throws Exception {
		Member member = this.loginService.getMember(request);
		ICompany company = this.commonService.getCompany(member.getCompanyId());
		String useContractNum = DataUtils.getString(params, "useContractNum", "");
		String requestYm = DataUtils.getString(params, "requestYm", "");
		String deadlineFlag = DataUtils.getString(params, "deadlineFlag", "");
		
		String errorParams = "";
	    if(!this.commonService.valid(params, "requestYm"))
	        errorParams = this.commonService.appendText(errorParams, "청구연월-requestYm");
	    
	    if(!this.commonService.valid(params, "deadlineFlag"))
	        errorParams = this.commonService.appendText(errorParams, "납기구분-deadlineFlag");
	    
	    // 파라미터 유효성 검증.
	    if(!errorParams.equals("")) {
	        // 필수파라미터 누락에 따른 오류 유발처리.
	        this.errorService.throwParameterErrorWithNames(errorParams);
	    }
		
		return company.contractBill(useContractNum, requestYm, deadlineFlag);
	}
}
