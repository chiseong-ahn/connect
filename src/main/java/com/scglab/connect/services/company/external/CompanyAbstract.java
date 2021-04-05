package com.scglab.connect.services.company.external;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.common.service.ApiService;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.HttpUtils;

@SuppressWarnings({"unchecked", "rawtypes"})
@Service
public abstract class CompanyAbstract {
	
	@Value("${spring.profiles}")
	protected String profile;

	@Value("${domain.cstalk}")
	protected String cstalkDomain;

	@Autowired 
	protected ApiService apiService;

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public abstract boolean getRelayUseExample();
	public abstract String getRelayDomain();
	
	// 1. 로그인
	public abstract boolean login(String id, String password);

	// 2. 직원 목록 가져오기
	public List<Map<String, Object>> employees() {
		List<Map<String, Object>> list = null;
		
		if (this.getRelayUseExample()) {
			list = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("id", "vmaster1");
			obj.put("companyName", "가상도시가스");
			obj.put("name", "매니저");
			obj.put("deptCode", "200552");
			obj.put("deptName", "가상1직영");
			obj.put("posName", "사원");
			obj.put("userStatus", "Y");
			
			Map<String, Object> telephone = new HashMap<String, Object>();
			telephone.put("num1", null);
			telephone.put("num2", null);
			telephone.put("num3", null);
			obj.put("telephone", telephone);
			
			Map<String, Object> cellphone = new HashMap<String, Object>();
			cellphone.put("num1", "010");
			cellphone.put("num2", "2282");
			cellphone.put("num3", "0317");
			obj.put("cellphone", cellphone);
			
			list.add(obj);
			
		} else {
			String url = "https://" + this.getRelayDomain() + "/api/employees?comIds=18";
			list = this.apiService.getForList(url);
		}
		
		return list;
	}

	// 3. 직원정보 가져오기.
	public Map<String, Object> employee(String id) {
		Map<String, Object> obj = null;
		
		if (this.getRelayUseExample()) {
			obj = new HashMap<String, Object>();
			
			obj.put("id", "vmaster1");
			obj.put("companyName", "가상도시가스");
			obj.put("name", "매니저");
			obj.put("deptCode", "200552");
			obj.put("deptName", "가상1직영");
			obj.put("posName", "사원");
			obj.put("userStatus", "Y");
			
			Map<String, Object> telephone = new HashMap<String, Object>();
			telephone.put("num1", null);
			telephone.put("num2", null);
			telephone.put("num3", null);
			obj.put("telephone", telephone);
			
			Map<String, Object> cellphone = new HashMap<String, Object>();
			cellphone.put("num1", "010");
			cellphone.put("num2", "2282");
			cellphone.put("num3", "0317");
			obj.put("cellphone", cellphone);
			
			
		} else {
			String url = "https://" + this.getRelayDomain() + "/api/employee?id=" + id;
			obj = this.apiService.getForMap(url);
		}

		return obj;
	}

	// 4. 민원 등록
	public String minwons(Map<String, String> params) {
		Map<String, Object> obj = null;
		
		if (this.getRelayUseExample()) {
			obj = new HashMap<String, Object>();
			obj.put("id", "20200922000691");
			
		} else {
			String url = "https://" + this.getRelayDomain() + "/api/cstalk/minwons";
			obj = this.apiService.postForMap(url, params);
		}

		return DataUtils.getString(obj, "id", "");
	}

	// 5. 사용계약번호 상세 정보
	public Map<String, Object> contractInfo(String useContractNum) {
		Map<String, Object> obj = null;
		
		if (this.getRelayUseExample()) {
			obj = new HashMap<String, Object>();
			
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
			
			List<Map<String, Object>> history = new ArrayList<Map<String, Object>>();
			Map<String, Object> data1 = new HashMap<String, Object>();
			data1.put("requestYm", "201912");
			data1.put("deadlineFlag", "20");
			
			Map<String, Object> data2 = new HashMap<String, Object>();
			data2.put("requestYm", "201911");
			data2.put("deadlineFlag", "20");
			
			history.add(data1);
			history.add(data2);
			
			Map<String, Object> bill = new HashMap<String, Object>();
			
			obj.put("contractInfo", contractInfo);
			obj.put("history", history);
			obj.put("bill", bill);
			
		} else {
			String url = "https://" + this.getRelayDomain() + "/api/cstalk/contractInfo?useContractNum=" + useContractNum;
			obj = this.apiService.getForMap(url);
		}
		
		return obj;
	}

	// 6. 사용계약번호 결제 상세 정보
	public Map<String, Object> contractBill(String useContractNum, String requestYm, String deadlineFlag) {
		Map<String, Object> contractBill = null;
		
		if (this.getRelayUseExample()) {
			
		} else {
			String url = "https://" + this.getRelayDomain() 
						+ "/api/cstalk/bill"
						+ "?useContractNum=" + useContractNum 
						+ "&requestYm=" + requestYm 
						+ "&deadlineFlag=" + deadlineFlag;
			
			contractBill = this.apiService.getForMap(url);

			if (contractBill != null) {
				if (contractBill.containsKey("previousUnpayInfos")) {
					List<Map<String, Object>> previousUnpayInfos = (List<Map<String, Object>>) contractBill
							.get("previousUnpayInfos");

					int previousUnpayCount = 0;
					if (previousUnpayInfos != null) {
						previousUnpayCount = previousUnpayInfos.size();
						int count = 2; // 전달할 카운트.

						// 최근 [count]건에 대해서만 데이터 전달.
						if (previousUnpayInfos.size() > count) {
							List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>(
									previousUnpayInfos.subList(0, 2));
							contractBill.put("previousUnpayInfos", subList);
						}
					}
					// 2건 외의 실제 미납 건수룰 previousUnpayCount 속성으로 전달
					contractBill.put("previousUnpayCount", previousUnpayCount);
				}
			}
		}

		return contractBill;
	}

	// 7. 휴일 여부 체크
	public int getWorkCalendar() {

		int num;
		
		if (this.getRelayUseExample()) {
			num = 1;
			
		} else {
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DATE);

			String today = year + "";
			today += month < 10 ? "0" + month : "" + month;
			today += day < 10 ? "0" + day : "" + day;
			
			String url = "https://" + this.getRelayDomain() + "/api/cstalk/workcalendar?date=" + today;
			
			this.logger.debug("url : " + url);
			Map<String, Object> data = this.apiService.getForMap(url);
			this.logger.debug("data : " + data.toString());
			
			String isHoliday = DataUtils.getString(data, "holidayFlag", "Y");
			this.logger.debug("isHoliday : " + isHoliday);

			num = isHoliday.equals("N") ? 1 : 2;
			if (num == 1) {
				System.out.println(cal);

				int hour = cal.get(Calendar.HOUR_OF_DAY);
				int min = cal.get(Calendar.MINUTE);
				int sec = cal.get(Calendar.SECOND);

				this.logger.debug("time : " + hour + " " + min + " " + sec);

				if (hour < 9 || (hour == 17 && min >= 30) || hour >= 18) {
					// 오전 9시 이전이거나, 오후 17시 30분 이후시간일 경우(업무 외 시간)
					num = 2;

				} else if (hour >= 12 && hour < 13) {
					// 오전 12시에서 오후1시일 경우(점심시간)
					num = 3;
				}
			}
		}

		return num;
	}

	// 8. 고객의 계약정보 목록
	public List<Map<String, Object>> contracts(String member) {
		List<Map<String, Object>> list = null;
		
		if (this.getRelayUseExample()) {
			
			
		} else {
			String url = "https://" + this.getRelayDomain() + "/api/cstalk/contracts?member=" + member;
			list = this.apiService.getForList(url);
		}
		return list;
	}

	// 9. 고객 profile 정보.
	public Map<String, Object> getProfile(String member) {
		Map<String, Object> profile = null;
		
		if (this.getRelayUseExample()) {
			profile = new HashMap<String, Object>();
			profile.put("name", "이유진");
			profile.put("handphone", "01044588856");
			profile.put("cash", 5000);
			
		} else {
			String url = "https://" + this.getRelayDomain() + "/api/cstalk/profile?member=" + member;
			
			if(member != null) {
				profile = this.apiService.getForMap(url);
			}
		}

		return profile;
	}

	// 10. 민원 코드목록 조회.
	public List<Map<String, Object>> getMinwonsCodes() {
		List<Map<String, Object>> list = null;
		
		if (this.getRelayUseExample()) {
			
			
		} else {
			String url = "https://" + this.getRelayDomain() + "/api/cstalk/minwons/ClassCodes";
			list = this.apiService.getForList(url);
		}

		return list;
	}

	
	// 회사코드 조회
	public abstract int getCompanyId();

	// 회사명 조회
	public abstract String getCompanyName();

}
