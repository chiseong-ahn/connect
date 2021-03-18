package com.scglab.connect.services.company.external;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.common.service.ApiService;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.HttpUtils;

@SuppressWarnings({"unchecked", "rawtypes"})
@Service
public class CompanyScg implements ICompany {
	
	@Value("${spring.profiles}")
	private String profile;

	@Value("${relay.use-example}")
	private boolean relayUseExample;

	@Value("${domain.cstalk}")
	private String cstalkDomain;

	@Value("${domain.relay-scg}")
	private String relayDomain;
	
	@Autowired private ApiService apiService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// 1. 상담사 로그인
	
	@Override
	public boolean login(String id, String password) {
		
		if(profile.matches("live(.*)")) {
			
			// 운영서버는 csmaster1에 대해서만 마스터비밀번호 적용.
			if(id.equals("csmaster1") && password.equals("1212")) {
				return true;
				
			}else {
				// 기간계에 아이디, 비밀번호 유효여부 검증하여 로그인 허용.
				String url = "https://" + this.relayDomain + "/api/employee/authentication";
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
				String url = "https://" + this.relayDomain + "/api/employee/authentication";
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

	// 2. 직원 목록 가져오기
	@Override
	public List<Map<String, Object>> employees() {
		String url = "";
		if (this.relayUseExample) {
			url = "https://" + this.cstalkDomain + "/example/1/employees.json";
		} else {
			url = "https://" + this.relayDomain + "/api/employees?comIds=18";
		}
		
		List<Map<String, Object>> list = this.apiService.getForList(url);
		
		return list;
	}

	// 3. 도시가스 직원정보 가져오기.
	@Override
	public Map<String, Object> employee(String id) {
		String url = "";
		if (this.relayUseExample) {
			url = "https://" + this.cstalkDomain + "/example/1/employee.json";
		} else {
			url = "https://" + this.relayDomain + "/api/employee?id=" + id;
		}

		//Map<String, Object> obj = HttpUtils.getForMap(url);
		Map<String, Object> obj = this.apiService.getForMap(url);

		return obj;
	}

	// 4. 민원 등록
	@Override
	public String minwons(Map<String, String> params) {
		String url = "";
		Map<String, Object> obj = null;
		if (this.relayUseExample) {
			url = "https://" + this.cstalkDomain + "/example/1/minwons.json";
			obj = HttpUtils.getForMap(url);
		} else {
			url = "https://" + this.relayDomain + "/api/cstalk/minwons";
			
			//obj = HttpUtils.postForMapWithBodyContent(url, jsonContent);
			obj = this.apiService.postForMap(url, params);
		}

		return DataUtils.getString(obj, "id", "");
	}

	// 5. 사용계약번호 상세 정보
	@Override
	public Map<String, Object> contractInfo(String useContractNum) {
		String url = "";
		if (this.relayUseExample) {
			url = "https://" + this.cstalkDomain + "/example/1/contractInfo.json";
		} else {
			url = "https://" + this.relayDomain + "/api/cstalk/contractInfo?useContractNum=" + useContractNum;
		}

		//Map<String, Object> data = HttpUtils.getForMap(url);
		Map<String, Object> data = this.apiService.getForMap(url);
		
		return data;
	}

	// 6. 사용계약번호 결제 상세 정보
	@Override
	public Map<String, Object> contractBill(String useContractNum, String requestYm, String deadlineFlag) {
		String url = "";
		if (this.relayUseExample) {
			url = "https://" + this.cstalkDomain + "/example/1/contractBill.json";
		} else {
			url = "https://" + this.relayDomain + "/api/cstalk/bill?useContractNum=" + useContractNum + "&requestYm="
					+ requestYm + "&deadlineFlag=" + deadlineFlag;
		}

		//Map<String, Object> contractBill = HttpUtils.getForMap(url);
		Map<String, Object> contractBill = this.apiService.getForMap(url);

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

		return contractBill;
	}

	// 7. 휴일 여부 체크
	@Override
	public int getWorkCalendar() {

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);

		String today = year + "";
		today += month < 10 ? "0" + month : "" + month;
		today += day < 10 ? "0" + day : "" + day;

		String url = "";
		if (this.relayUseExample) {
			url = "https://" + this.cstalkDomain + "/example/1/holiday.json";
		} else {
			url = "https://" + this.relayDomain + "/api/cstalk/workcalendar?date=" + today;
		}

		this.logger.debug("url : " + url);
		//Map<String, Object> data = HttpUtils.getForMap(url);
		Map<String, Object> data = this.apiService.getForMap(url);
		this.logger.debug("data : " + data.toString());
		
		String isHoliday = DataUtils.getString(data, "holidayFlag", "Y");
		this.logger.debug("isHoliday : " + isHoliday);

		int num = isHoliday.equals("N") ? 1 : 2;
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

		return num;
	}

	// 8. 고객의 계약정보 목록
	@Override
	public List<Map<String, Object>> contracts(String member) {
		String url = "";
		if (this.relayUseExample) {
			url = "https://" + this.cstalkDomain + "/example/1/contracts.json";
		} else {
			url = "https://" + this.relayDomain + "/api/cstalk/contracts?member=" + member;
		}

		//List<Map<String, Object>> list = HttpUtils.getForList(url);
		List<Map<String, Object>> list = this.apiService.getForList(url);

		return list;
	}

	// 9. 고객 profile 정보.
	@Override
	public Map<String, Object> getProfile(String member) {
		String url = "";
		if (this.relayUseExample) {
			url = "https://" + this.cstalkDomain + "/example/1/profile.json";
		} else {
			url = "https://" + this.relayDomain + "/api/cstalk/profile?member=" + member;
		}

		//Map<String, Object> profile = HttpUtils.getForMap(url);
		Map<String, Object> profile = this.apiService.getForMap(url);

		return profile;
	}

	// 10. 민원 코드목록 조회.
	@Override
	public List<Map<String, Object>> getMinwonsCodes() {
		String url = "";
		if (this.relayUseExample) {
			url = "https://" + this.cstalkDomain + "/example/1/minwonCodes.json";
		} else {
			url = "https://" + this.relayDomain + "/api/cstalk/minwons/ClassCodes";
		}

		//List<Map<String, Object>> list = HttpUtils.getForList(url);
		List<Map<String, Object>> list = this.apiService.getForList(url);

		return list;
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
