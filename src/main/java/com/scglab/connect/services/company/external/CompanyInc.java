package com.scglab.connect.services.company.external;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.auth.Contract;
import com.scglab.connect.services.member.Member;
@Service
public class CompanyInc implements ICompany {

	@Override
	public boolean login(String id, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Map<String, Object>> employees() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> employee(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String minwons(Map<String, String> params) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public Map<String, Object> contractInfo(String useContractNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> contractBilDetail(String useContractNum, String requestYm, String deadlineFlag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWorkCalendar() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Map<String, Object>> contracts(String member) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getProfile(String member) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCompanyId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCompanyName() {
		// TODO Auto-generated method stub
		return null;
	}

	


}
