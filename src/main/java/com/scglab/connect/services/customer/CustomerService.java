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
		int startNum = (page - 1) * pageSize + 1;
		
		params.put("startNum", startNum);
		params.put("pageSize", pageSize);
				
		int totalCount = this.customerDao.findAllCount(params);
		List<Map<String, Object>> list = null;
		if(totalCount > 0) {
			list = this.customerDao.findAll(params);
		}
		
		data.put("totalCount", totalCount);
		data.put("data", list);
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
	public Map<String, Object> block(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		int result = this.customerDao.enableBlackStatus(params);
		if(result > 0) {
			data = this.customerDao.findCustomer(params);
		}
		
		return data;
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
	public Map<String, Object> unBlock(Map<String, Object> params, HttpServletRequest request) throws Exception {
		this.logger.debug("request : " + request);
		
		Member member = this.loginService.getMember(request);
		this.logger.debug("member : " + member);
		params.put("companyId", member.getCompanyId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		int result = this.customerDao.disbleBlackStatus(params);
		if(result > 0) {
			data = this.customerDao.findCustomer(params);
		}
		
		return data;
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
		Customer customer = this.customerDao.findByGassappMemberNumber(params);
		this.logger.debug("customer : " + customer);
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> customerMap = objectMapper.convertValue(customer, Map.class);
		String token = this.jwtService.generateToken(customerMap);
		data.put("token", token);
		
		return data;
	}
	
	/**
	 * 
	 * @Method Name : update
	 * @작성일 : 2020. 11. 13.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 :
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> update(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		int id = DataUtils.getInt(params, "id", 0);
		if(id == 0) {
			Object[] args = new String[1];
			args[0] = "id";
			throw new RuntimeException(this.messageService.getMessage("error.parameter1", args));
		}
		int count = this.customerDao.update(params);
		data.put("isSuccess", count > 0 ? true : false);
		
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
		
		int userno = DataUtils.getInt(params, "userno", 0);
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
		
		if(!useContractNum.equals("")) {
			return company.contractBilDetail(useContractNum, requestYm, deadlineFlag);
		}
		return null;
	}
}
