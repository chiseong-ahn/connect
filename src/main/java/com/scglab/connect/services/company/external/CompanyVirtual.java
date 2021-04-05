package com.scglab.connect.services.company.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.scglab.connect.constant.Constant;

@Service
public class CompanyVirtual extends CompanyAbstract implements ICompany {
	
	@Value("${domain.relay-vir}")
	private String relayVirtualDomain;
	
	public String getRelayDomain() {
		return this.relayVirtualDomain;
	}
	
	// 1. 상담사 로그인
	public boolean login(String id, String password) {
		
		// 운영서버는 csmaster1에 대해서만 마스터비밀번호 적용.
		if((id.equals("vmaster1") || id.equals("vmanager1")) && password.equals("1212")) {
			return true;
			
		}
		
		return false;
	}

	public int getCompanyId() {
		return Constant.COMPANY_CODE_VIRTUAL;
	}

	public String getCompanyName() {
		return Constant.COMPANY_NAME_VIRTUAL;
	}

	
}
