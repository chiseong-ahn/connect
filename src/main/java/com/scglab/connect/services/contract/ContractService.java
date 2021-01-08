package com.scglab.connect.services.contract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.ErrorService;
import com.scglab.connect.services.company.external.CompanyInc;
import com.scglab.connect.services.company.external.CompanyScg;
import com.scglab.connect.services.company.external.ICompany;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;

@Service
public class ContractService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//@Autowired private MessageHandler messageHandler;
	@Autowired private LoginService loginService;
	@Autowired private CompanyScg companyScg;
	@Autowired private CompanyInc companyInc;
	@Autowired private CommonService commonService;
	@Autowired private ErrorService errorService;
	
	private ICompany getCompany(String companyId) {
		ICompany company = companyId.equals("1") ? this.companyScg : this.companyInc;
		return company;
	}
	
	/**
	 * 
	 * @Method Name : contracts
	 * @작성일 : 2020. 12. 9.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 고객의 사용계약번호 목록 조회.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> contracts(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		Member member = this.loginService.getMember(request);
		String companyId = member.getCompanyId();
		
		String errorParams = "";
	    if(!this.commonService.valid(params, "gasappMemberNumber"))
	        errorParams = this.commonService.appendText(errorParams, "사용계약번호-gasappMemberNumber");
	    
	    // 파라미터 유효성 검증.
	    if(!errorParams.equals("")) {
	        // 필수파라미터 누락에 따른 오류 유발처리.
	        this.errorService.throwParameterErrorWithNames(errorParams);
	    }
		
	    String gasappMemberNumber = DataUtils.getString(params, "gasappMemberNumber", "");
		Map<String, Object> profile = getCompany(companyId).getProfile(gasappMemberNumber);
		int cash = DataUtils.getInt(profile, "cash", 0);
		data.put("cash", cash);
		
		List<Map<String, Object>> list = getCompany(companyId).contracts(gasappMemberNumber);
		data.put("contracts", list);
		
		return data;
	}
	
	
	/**
	 * 
	 * @Method Name : contractInfo
	 * @작성일 : 2020. 12. 9.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 계약정보 상세 조회.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> contractInfo(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		String companyId = member.getCompanyId();
		
		String useContractNum = DataUtils.getString(params, "useContractNum", "");
		
		// 사용계약번호 파라미터 유효성 검증.
		if(useContractNum.equals("")) {
			// 필수 파라미터 누락에 따른 오류유발.
			this.errorService.throwParameterErrorWithNames("useContractNum");
		}
		
		// 사용계약 상세정보 조회.
		Map<String, Object> contractInfo = getCompany(companyId).contractInfo(useContractNum);
		
		// 결재 히스토리 존재여부.
		if(contractInfo.containsKey("history")) {
			List<Map<String, Object>> histories = (List<Map<String, Object>>) contractInfo.get("history");
			if(histories != null) {
				if(histories.size() > 0) {
					
					// 최신 1건의 히스토리에 대해 결제정보 조회.
					Map<String, Object> history = histories.get(0);
					String requestYm = DataUtils.getString(history, "requestYm", "");
					String deadlineFlag = DataUtils.getString(history, "deadlineFlag", "");
					
					if(!requestYm.equals("") && !deadlineFlag.equals("")) {
						Map<String, Object> bill = getCompany(companyId).contractBill(useContractNum, requestYm, deadlineFlag);
						contractInfo.put("bill", bill);
					}
				}
			}
		}
		
		return contractInfo;
	}
	
	
	/**
	 * 
	 * @Method Name : contractBil
	 * @작성일 : 2020. 12. 9.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 결제 상세 조회.
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> contractBil(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		String companyId = member.getCompanyId();
		
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
		
		String useContractNum = DataUtils.getString(params, "useContractNum", "");
		String requestYm = DataUtils.getString(params, "requestYm", "");
		String deadlineFlag = DataUtils.getString(params, "deadlineFlag", "");
		
		if(requestYm.equals("") || deadlineFlag.equals("")) {
			
			// 필수 파라미터 누락에 따른 오류유발.
			this.errorService.throwParameterError();
		}
		
		Map<String, Object> contractBill = getCompany(companyId).contractBill(useContractNum, requestYm, deadlineFlag);
		
		return contractBill;
	}
	
}