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

import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.services.company.external.CompanyInc;
import com.scglab.connect.services.company.external.CompanyScg;
import com.scglab.connect.services.company.external.ICompany;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;

@Service
public class ContractService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private MessageHandler messageHandler;
	@Autowired private LoginService loginService;
	@Autowired private CompanyScg companyScg;
	@Autowired private CompanyInc companyInc;
	
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
		
		String gasappMemberNumber = DataUtils.getString(params, "gasappMemberNumber", "");
		
		if(gasappMemberNumber.equals("")) {
			// 필수 파라미터 누락에 따른 오류유발.
			String[] errorParams = new String[1];
			errorParams[0] = "gasappMemberNumber";
			
			String reason = this.messageHandler.getMessage("error.params.type1", errorParams);
			throw new RuntimeException(reason);
		}
		
		Map<String, Object> profile = getCompany(companyId).getProfile(Long.parseLong(gasappMemberNumber));
		int cash = DataUtils.getInt(profile, "cash", 0);
		data.put("cash", cash);
		
		List<Map<String, Object>> list = getCompany(companyId).contracts(Long.parseLong(gasappMemberNumber));
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
		Map<String, Object> contractInfo = getCompany(companyId).contractInfo(useContractNum);
		
		/*
		if(contractInfo.containsKey("history")) {
			List<Map<String, Object>> histories = (List<Map<String, Object>>) contractInfo.get("history");
			if(histories != null) {
				if(histories.size() > 0) {
					for(Map<String, Object> history : histories) {
						String requestYm = DataUtils.getString(history, "requestYm", "");
						String deadlineFlag = DataUtils.getString(history, "deadlineFlag", "");
						
						if(!requestYm.equals("") && deadlineFlag.equals("")) {
							Map<String, Object> bil = getCompany(companyId).contractBilDetail(useContractNum, requestYm, deadlineFlag);
						}
					}
				}
			}
			
		}
		*/
		
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
		
		String useContractNum = DataUtils.getString(params, "useContractNum", "");
		String requestYm = DataUtils.getString(params, "requestYm", "");
		String deadlineFlag = DataUtils.getString(params, "deadlineFlag", "");
		
		if(requestYm.equals("") || deadlineFlag.equals("")) {
			// 필수 파라미터 누락에 따른 오류유발.
			String reason = this.messageHandler.getMessage("error.params.type0");
			throw new RuntimeException(reason);
		}
		
		Map<String, Object> contractBil = getCompany(companyId).contractBilDetail(useContractNum, requestYm, deadlineFlag);
		return contractBil;
	}
	
}