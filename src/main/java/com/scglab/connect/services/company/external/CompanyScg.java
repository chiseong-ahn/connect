package com.scglab.connect.services.company.external;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scglab.connect.constant.Constant;

@SuppressWarnings({"unchecked"})
@Service
public class CompanyScg extends CompanyAbstract implements ICompany {
	
	@Value("${domain.relay-scg}")
	private String relayScgDomain;
	
	@Value("${relay.use-example}")
	private boolean relayUseExample;
	
	public boolean getRelayUseExample() {
		return this.relayUseExample;
	}
	
	public String getRelayDomain() {
		return this.relayScgDomain;
	}
	
	// 1. 상담사 로그인
	public boolean login(String id, String password) {
		if(this.profile.matches("live(.*)")) {
			
			// 운영서버는 csmaster1에 대해서만 마스터비밀번호 적용.
			if(id.equals("csmaster1") && password.equals("1212")) {
				return true;
				
			}else {
				// 기간계에 아이디, 비밀번호 유효여부 검증하여 로그인 허용.
				String url = "https://" + getRelayDomain() + "/api/employee/authentication";
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", id);
				params.put("password", password);
	
				if(this.apiService.postStatusCode(url, params) == HttpURLConnection.HTTP_OK) {
					return true;
				}
			}
			
		}else {
			// 로컬 및 개발서버는 마스터비밀번호 적용.
			if(password.equals("1212")){
				return true;
				
			}else {
				// 기간계에 아이디, 비밀번호 유효여부 검증하여 로그인 허용.
				String url = "https://" + getRelayDomain() + "/api/employee/authentication";
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", id);
				params.put("password", password);
	
				if(this.apiService.postStatusCode(url, params) == HttpURLConnection.HTTP_OK) {
					return true;
				}
			}
		}
		
		return false;
	}

	public int getCompanyId() {
		return Constant.COMPANY_CODE_SEOUL;
	}

	public String getCompanyName() {
		return Constant.COMPANY_NAME_SEOUL;
	}

	
}
