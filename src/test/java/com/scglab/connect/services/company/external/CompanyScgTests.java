package com.scglab.connect.services.company.external;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.scglab.connect.utils.DataUtils;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class CompanyScgTests {
	
	@Autowired private CompanyScg companyScg;
	
	@Test
	public void 상담사_로그인() {
		String id="csmaster1";
		String password = "1212";
		
		boolean result = this.companyScg.login(id, password);
		Assertions.assertTrue(result);
	}
	
	@Test
	public void 직원_목록_조회() {
		List<Map<String, Object>> list = this.companyScg.employees();
		boolean result = list != null && list.size() > 0;
		Assertions.assertTrue(result);
	}
	
	@Test
	public void 직원_정보_조회() {
		String id = "csmaster1";
		Map<String, Object> employee = this.companyScg.employee(id);
		String employeeId = DataUtils.getString(employee, "id", "");
		Assertions.assertTrue(employeeId != null);
		Assertions.assertEquals(id, employeeId);
	}
	
	@Test
	public void 민원_등록() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("customerMobileId", "3716");
		data.put("useContractNum","6004910783");
		data.put("reqName","김윤석");
		data.put("classCode","010202");
		data.put("transfer","false");
		data.put("handphone","010-6601-5106");
		data.put("memo","민원테스트");
		data.put("employeeId","csmaster1");
		data.put("chatId","116");
		
		String id = this.companyScg.minwons(data);
		
		Assertions.assertTrue(id != null);
	}
	
	@Test
	public void 사용계약번호_상세_정보() {
		String useContractNum = "6000000486";
		Map<String, Object> result = this.companyScg.contractInfo(useContractNum);
		Assertions.assertTrue(result != null);
		Assertions.assertTrue(result.containsKey("contractInfo"));
		Assertions.assertTrue(result.containsKey("history"));
	}
	
	@Test
	public void 사용계약번호_결제상세_정보() {
		String useContractNum = "6000000486";
		String requestYm = "2020-01";
		String deadlineFlag = "20";
		
		Map<String, Object> result = this.companyScg.contractBill(useContractNum, requestYm, deadlineFlag);
		Assertions.assertTrue(result != null);
		Assertions.assertEquals(useContractNum, result.get("useContractNum"));	
	}
	
	@Test
	public void 휴일_여부_체크() {
		int result = this.companyScg.getWorkCalendar();
		Assertions.assertTrue(result == 1 || result == 2);
	}
	
	@Test
	public void 고객_계약정보_목록() {
		String member = "3369";
		List<Map<String, Object>> list = this.companyScg.contracts(member);
		boolean result = list != null && list.size() > 0;
		Assertions.assertTrue(result);
	}
	
	@Test
	public void 고객_프로필정보_조회() {
		String member = "3369";
		Map<String, Object> result = this.companyScg.getProfile(member);
		Assertions.assertTrue(result != null);
		Assertions.assertTrue(result.containsKey("name"));
		Assertions.assertNotEquals(result.get("name"), "");
	}
	
	@Test
	public void 민원_코드목록_조회() {
		List<Map<String, Object>> list = this.companyScg.getMinwonsCodes();
		boolean result = list != null && list.size() > 0;
		Assertions.assertTrue(result);
	}
	
	
	
}