package com.scglab.connect.services.company.external;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.scglab.connect.constant.Constant;
import com.scglab.connect.properties.DomainProperties;
import com.scglab.connect.services.common.auth.Contract;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.HttpUtils;

@Service
public class CompanyScg implements ICompany {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private DomainProperties domainProperty;
	
	private String protocol = "http://";
	private HttpUtils httpUtils;
	
	public CompanyScg() {
		this.httpUtils = new HttpUtils();
	}

	// 1. 상담사 로그인
	@Override
	public boolean login(String id, String password) {
		
		// /api/employees/login GET
		
		
		
		return true;
	}

	// 2. 직원 목록 가져오기
	@Override
	public List<Map<String, Object>> employees() {
		// TODO Auto-generated method stub
		
		// /api/employees?comIds=18 GET
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("id", "csmaster1");
		obj.put("companyName", "SCG솔루션즈(주)");
		obj.put("name", "이세임");
		obj.put("deptCode", "200552");
		obj.put("deptName", "경기1직영");
		obj.put("posName", "사원");
		obj.put("userStatus", "Y");
		
		Map<String, Object> telephone = new HashMap<String, Object>();
		telephone.put("num1", "02");
		telephone.put("num2", "552");
		telephone.put("num3", "7777");
		obj.put("telephone", telephone);
		
		Map<String, Object> cellphone = new HashMap<String, Object>();
		cellphone.put("num1", "010");
		cellphone.put("num2", "1111");
		cellphone.put("num3", "4062");	
		obj.put("cellphone", cellphone);
		
		return list;
	}

	// 3. 도시가스 직원정보 가져오기.
	@Override
	public Map<String, Object> employee(String id) {
		
		// api/employees/:id GET
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("id", "csmaster1");
		obj.put("companyName", "SCG솔루션즈(주)");
		obj.put("name", "이세임");
		obj.put("deptCode", "200552");
		obj.put("deptName", "경기1직영");
		obj.put("posName", "사원");
		obj.put("userStatus", "Y");
		
		Map<String, Object> telephone = new HashMap<String, Object>();
		telephone.put("num1", "02");
		telephone.put("num2", "552");
		telephone.put("num3", "7777");
		obj.put("telephone", telephone);
		
		Map<String, Object> cellphone = new HashMap<String, Object>();
		cellphone.put("num1", "010");
		cellphone.put("num2", "1111");
		cellphone.put("num3", "4062");	
		obj.put("cellphone", cellphone);
		
		return obj;
	}

	// 4. 민원 등록
	@Override
	public int minwons(Map<String, Object> params) {
		// TODO Auto-generated method stub
		/*
			{
			    "id":"20200922000691"
			}
		 */
		// /api/chattalk/minwons POST
		
		String id = "20200922000691";
		
		return Integer.parseInt(id);
	}
	
	
	// 5. 사용계약번호 상세 정보
	@Override
	public Map<String, Object> contractInfo(String useContractNum){
		
		// api/contractInfo/:useContractNum GET
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		Map<String, Object> contractInfo = new HashMap<String, Object>();
		contractInfo.put("useContractNum", "6000000486");
		contractInfo.put("customerName", "김태수");
		contractInfo.put("centerCode", "32");
		contractInfo.put("centerName", "강북2고객센터");
		contractInfo.put("centerPhone", "02-701-0337");
		contractInfo.put("meterNum", "300000486");
		contractInfo.put("gmtrBaseDay", "26");
		contractInfo.put("billSendMethod", "모바일");
		contractInfo.put("paymentType", "은행이체");
		contractInfo.put("contractStatus", "정상");
		contractInfo.put("productName", "업무난방용");
		
		Map<String, Object> telNumber = new HashMap<String, Object>();
		telNumber.put("num1", "02");
		telNumber.put("num2", "701");
		telNumber.put("num3", "6654");
		contractInfo.put("telNumber", telNumber);
		
		data.put("contractInfo", contractInfo);
		
		List<Map<String, Object>> history = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> history1 = new HashMap<String, Object>();
		history1.put("requestYm", "201912");
		history1.put("deadlineFlag", "20");
		history.add(history1);
		
		Map<String, Object> history2 = new HashMap<String, Object>();
		history2.put("requestYm", "201911");
		history2.put("deadlineFlag", "20");
		history.add(history2);
		
		data.put("history", history);
		
		return data;
	}

	
	// 6. 사용계약번호 결제 상세 정보
	@Override
	public Map<String, Object> contractBilDetail(String useContractNum, String requestYm, String deadlineFlag) {
		//api/bill/:useContractNum/detail?requestYm=:requestYm&deadlineFlag=:deadlineFlag
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("useContractNum", "6000000486");
		data.put("requestYm", "201911");
		data.put("deadlineFlag", "20");
		data.put("paymentDeadline", "20191120");
		data.put("basicRate", "0.0");
		data.put("useRate", "0.0");
		data.put("discountAmt", "0.0");
		data.put("replacementCost", "0.0");
		data.put("vat", "0.0");
		data.put("adjustmentAmt", "0.0");
		data.put("cutAmt", "0.0");
		data.put("chargeAmt", "0.0");
		data.put("usageQty", "0.0");
		data.put("unpayAmt", "0.0");
		data.put("allUnpayAmounts", "0.0");
		data.put("allPayAmounts", "0.0");
		data.put("previousUnpayAmounts", "0.0");
		data.put("payMethod", "자동이체(은행)");
		
		Map<String, Object> virtualAccount = new HashMap<String, Object>();
			virtualAccount.put("accountName", "이원준");
			List<Map<String, Object>> accounts = new ArrayList<Map<String, Object>>();
				Map<String, Object> account1 = new HashMap<String, Object>();
					account1.put("bankCode", "020");
					account1.put("name", "우리은행(은행)");
					account1.put("account", "29800105-218194");
			
				Map<String, Object> account2 = new HashMap<String, Object>();
					account2.put("bankCode", "003");
					account2.put("name", "기업은행(은행)");
					account2.put("account", "59503752-997969");
				
				accounts.add(account1);
				accounts.add(account2);
			virtualAccount.put("accounts", accounts);
		data.put("virtualAccount", virtualAccount);
		
		
		List<Map<String, Object>> previousUnpayInfos = new ArrayList<Map<String, Object>>();
			Map<String, Object> previousUnpayInfo1 = new HashMap<String, Object>();
				previousUnpayInfo1.put("requestYm", "201709");
				previousUnpayInfo1.put("unpayAmtAll", "2700");
		
				previousUnpayInfos.add(previousUnpayInfo1);
		data.put("previousUnpayInfos", previousUnpayInfos);
		
		return data;
	}
	
	// 7. 휴일 여부 체크
	@Override
	public int getWorkCalendar() {
		//day: 20201001
		// api/workcalendar?day=:day GET
	
		String day = "20201001";
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("holidayFlag", "N");
		data.put("workingDay", "Y");
		
		String isWorking = DataUtils.getString(data, "workingDay", "Y");

		
		return isWorking.equals("Y") ? 1 : 2;
	}
	
	
	// 8. 고객의 계약정보 목록
	@Override
	public List<Map<String, Object>> contracts(long useContractNum) {
		// TODO Auto-generated method stub
		// api/matt/contracts?member=:member GET
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> obj1 = new HashMap<String, Object>();
		Map<String, Object> obj2 = new HashMap<String, Object>();
		
		obj1.put("useContractNum", "6000000486");
		obj1.put("main", 1);
		obj1.put("alias", "우리집");
		obj1.put("jinbunAddress", null);
		obj1.put("newAddress", "서울특별시 마포구 ********,103호 (***)");
		
		obj2.put("useContractNum", "6000000502");
		obj2.put("main", 0);
		obj2.put("alias", "장모님");
		obj2.put("jinbunAddress", null);
		obj2.put("newAddress", "서울특별시 마포구 ******, (****)");
		
		list.add(obj1);
		list.add(obj2);
		
		return list;
	}
	
	
	// 9. 고객 profile 정보.
	@Override
	public Map<String, Object> getProfile(long member) {
		// TODO Auto-generated method stub
		// api/matt/profile?member=:member GET
		
		Map<String, Object> profile = new HashMap<String, Object>();
		profile.put("name", "이유진");
		profile.put("handphone", "01044588856");
		profile.put("cash", 5000);
		
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
