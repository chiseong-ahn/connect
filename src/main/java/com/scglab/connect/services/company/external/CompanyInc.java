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
		return true;
	}

	@Override
	public List<Map<String, Object>> getMembers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Member getMemberInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int sendMinwon(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Map<String, Object>> getContractList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Map<String, Object> getCustomerInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contract getContractDetail(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Map<String, Object> getContractMonthlyDetail(Map<String, Object> params){
		
		return null;
	}

	@Override
	public int isHoliday(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return 0;
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

	@Override
	public int isWorking() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	


}
