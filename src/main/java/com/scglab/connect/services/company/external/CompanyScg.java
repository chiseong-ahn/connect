package com.scglab.connect.services.company.external;

import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fcibook.quick.http.ResponseBody;
import com.scglab.connect.constant.Constant;
import com.scglab.connect.properties.DomainProperties;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.HttpUtils;

@Service
public class CompanyScg implements ICompany {
	
	@Autowired private Environment env;
	@Autowired private DomainProperties domainProperty;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String protocol = "http://";
	
	public static void main(String[] args) {
		CompanyScg company = new CompanyScg();
		boolean result = company.login("csmaster1", "1212");
		System.out.println("result : " + result);
	}
	
	public String getActiveProfile() {
		String[] profiles = this.env.getActiveProfiles();
		if(profiles.length == 0) {
			profiles = this.env.getDefaultProfiles();
		}
		return profiles[0];
	}
	
	
	

	// 1. 상담사 로그인
	@Override
	public boolean login(String id, String password) {
		
		String url = getActiveProfile() == "local" 
				? this.protocol + this.domainProperty + "/api/employees/login" 
				: "http://localhost:8080/example/1/profile.json";
		
		ResponseBody body = HttpUtils.getForResponseBody(url);
		
		if(body.getStateCode() == Response.SC_OK) {
			return true;
		}
		return false;
	}

	// 2. 직원 목록 가져오기
	@Override
	public List<Map<String, Object>> employees() {
		String url = getActiveProfile() == "local" 
				? this.protocol + this.domainProperty + "/api/employees?comIds=18"
				: "http://localhost:8080/example/1/employees.json";
		
		List<Map<String, Object>> list = HttpUtils.getForList(url);
		
		return list;
	}

	// 3. 도시가스 직원정보 가져오기.
	@Override
	public Map<String, Object> employee(String id) {
		String url = getActiveProfile() == "local" 
				? this.protocol + this.domainProperty.getRelayScg() + "/api/employees/" + id
				: "http://localhost:8080/example/1/employee.json";
		
		Map<String, Object> obj = HttpUtils.getForMap(url);
		
		return obj;
	}

	// 4. 민원 등록
	@Override
	public int minwons(Map<String, String> params) {
		String url = getActiveProfile() == "local" 
				? this.protocol + this.domainProperty.getRelayScg() + "/api/chattalk/minwons"
				: "http://localhost:8080/example/1/minwons.json";
		
		Map<String, Object> obj = HttpUtils.postForMap(url);
		String id = DataUtils.getString(obj, "id", "");
		
		return Integer.parseInt(id);
	}
	
	
	// 5. 사용계약번호 상세 정보
	@Override
	public Map<String, Object> contractInfo(String useContractNum){
		String url = getActiveProfile() == "local" 
				? this.protocol + this.domainProperty.getRelayScg() + "/api/contractInfo/" + useContractNum
				: "http://localhost:8080/example/1/contractInfo.json";
		
		Map<String, Object> data = HttpUtils.getForMap(url);
		
		return data;
	}

	
	// 6. 사용계약번호 결제 상세 정보
	@Override
	public Map<String, Object> contractBilDetail(String useContractNum, String requestYm, String deadlineFlag) {
		String url = getActiveProfile() == "local" 
				? this.protocol + this.domainProperty.getRelayScg() + "/api/bill/" + useContractNum + "/detail?requestYm=" + requestYm + "&deadlineFlag=" + deadlineFlag
				: "http://localhost:8080/example/1/contractBil.json";
		
		Map<String, Object> data = HttpUtils.getForMap(url);
		
		return data;
	}
	
	// 7. 휴일 여부 체크
	@Override
	public int getWorkCalendar() {
		String day = "20201001";
		String url = getActiveProfile() == "local" 
				? this.protocol + this.domainProperty.getRelayScg() + "api/workcalendar?day=" + day
				: "http://localhost:8080/example/1/holiday.json";
		
		Map<String, Object> data = HttpUtils.getForMap(url);
		String isWorking = DataUtils.getString(data, "workingDay", "Y");

		return isWorking.equals("Y") ? 1 : 2;
	}
	
	
	// 8. 고객의 계약정보 목록
	@Override
	public List<Map<String, Object>> contracts(long member) {
		String url = getActiveProfile() == "local" 
				? this.protocol + this.domainProperty.getRelayScg() + "api/matt/contracts?member=" + member
				: "http://localhost:8080/example/1/contracts.json";
		
		List<Map<String, Object>> list = HttpUtils.getForList(url);
		
		return list;
	}
	
	
	// 9. 고객 profile 정보.
	@Override
	public Map<String, Object> getProfile(long member) {
		String url = getActiveProfile() == "local" 
				? this.protocol + this.domainProperty.getRelayScg() + "/api/matt/profile?member=" + member
				: "http://localhost:8080/example/1/profile.json?member=" + member;
		
		Map<String, Object> profile = HttpUtils.getForMap(url);	
			
		return profile;
	}

	@Override
	public String getCompanyId() {
		return Constant.COMPANY_CODE_SEOUL;
	}

	@Override
	public String getCompanyName() {
		return Constant.COMPANY_NAME_SEOUL;
	}
}
