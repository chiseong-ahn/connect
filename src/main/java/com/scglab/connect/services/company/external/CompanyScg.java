package com.scglab.connect.services.company.external;

import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fcibook.quick.http.ResponseBody;
import com.scglab.connect.constant.Constant;
import com.scglab.connect.properties.DomainProperties;
import com.scglab.connect.properties.ServerProperties;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.HttpUtils;

@Service
public class CompanyScg implements ICompany {
	
	@Autowired private Environment env;
	@Autowired private DomainProperties domainProperty;
	@Autowired private ServerProperties serverProperty;
	
	@Value("${relay.use-example}")
	private boolean relayUseExample;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
		String url = "";
		if(this.relayUseExample) {
			url = "http://127.0.0.1:" + this.serverProperty.getPort() + "/example/1/profile.json";
		}else {
			url = "https://" + this.domainProperty.getRelayScg() + "/api/employees/login";
		}
		this.logger.info("url : " + url);
		
		ResponseBody body = HttpUtils.getForResponseBody(url);
		
		if(body.getStateCode() == Response.SC_OK) {
			return true;
		}
		return false;
	}

	// 2. 직원 목록 가져오기
	@Override
	public List<Map<String, Object>> employees() {
		String url = "";
		if(this.relayUseExample) {
			url = "http://127.0.0.1:" + this.serverProperty.getPort() + "/example/1/employees.json";
		}else {
			url = "https://" + this.domainProperty.getRelayScg() + "/api/employees?comIds=18";
		}
		
		List<Map<String, Object>> list = HttpUtils.getForList(url);
		
		return list;
	}

	// 3. 도시가스 직원정보 가져오기.
	@Override
	public Map<String, Object> employee(String id) {
		String url = "";
		if(this.relayUseExample) {
			url = "http://127.0.0.1:" + this.serverProperty.getPort() + "/example/1/employee.json";
		}else {
			url = "https://" + this.domainProperty.getRelayScg() + "/api/employees/" + id;
		}
		
		Map<String, Object> obj = HttpUtils.getForMap(url);
		
		return obj;
	}

	// 4. 민원 등록
	@Override
	public String minwons(Map<String, String> params) {
		String url = "";
		Map<String, Object> obj = null;
		if(this.relayUseExample) {
			url = "http://127.0.0.1:" + this.serverProperty.getPort() + "/example/1/minwons.json";
			obj = HttpUtils.getForMap(url);
		}else {
			url = "https://" + this.domainProperty.getRelayScg() + "/api/chattalk/minwons";
			obj = HttpUtils.postForMap(url, params);
		}
		
		return DataUtils.getString(obj, "id", "");
	}
	
	
	// 5. 사용계약번호 상세 정보
	@Override
	public Map<String, Object> contractInfo(String useContractNum){
		String url = "";
		if(this.relayUseExample) {
			url = "http://127.0.0.1:" + this.serverProperty.getPort() + "/example/1/contractInfo.json";
		}else {
			url = "https://" + this.domainProperty.getRelayScg() + "/api/contractInfo/" + useContractNum;
		}
		
		Map<String, Object> data = HttpUtils.getForMap(url);
		
		return data;
	}

	
	// 6. 사용계약번호 결제 상세 정보
	@Override
	public Map<String, Object> contractBilDetail(String useContractNum, String requestYm, String deadlineFlag) {
		String url = "";
		if(this.relayUseExample) {
			url = "http://127.0.0.1:" + this.serverProperty.getPort() + "/example/1/contractBil.json";
		}else {
			url = "https://" + this.domainProperty.getRelayScg() + "/api/bill/" + useContractNum + "/detail?requestYm=" + requestYm + "&deadlineFlag=" + deadlineFlag;
		}
		
		Map<String, Object> data = HttpUtils.getForMap(url);
		
		return data;
	}
	
	// 7. 휴일 여부 체크
	@Override
	public int getWorkCalendar() {
		String day = "20201001";
		String url = "";
		if(this.relayUseExample) {
			url = "http://127.0.0.1:" + this.serverProperty.getPort() + "/example/1/holiday.json";
		}else {
			url = "https://" + this.domainProperty.getRelayScg() + "api/workcalendar?day=" + day;
		}
		
		Map<String, Object> data = HttpUtils.getForMap(url);
		String isWorking = DataUtils.getString(data, "workingDay", "Y");

		return isWorking.equals("Y") ? 1 : 2;
	}
	
	
	// 8. 고객의 계약정보 목록
	@Override
	public List<Map<String, Object>> contracts(long member) {
		String url = "";
		if(this.relayUseExample) {
			url = "http://127.0.0.1:" + this.serverProperty.getPort() + "/example/1/contracts.json";
		}else {
			url = "https://" + this.domainProperty.getRelayScg() + "api/matt/contracts?member=" + member;
		}
		
		List<Map<String, Object>> list = HttpUtils.getForList(url);
		
		return list;
	}
	
	
	// 9. 고객 profile 정보.
	@Override
	public Map<String, Object> getProfile(long member) {
		String url = "";
		if(this.relayUseExample) {
			url = "http://127.0.0.1:" + this.serverProperty.getPort() + "/example/1/profile.json";
		}else {
			url = "https://" + this.domainProperty.getRelayScg() + "/api/matt/profile?member=" + member;
		}
		
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
