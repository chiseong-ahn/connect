package com.scglab.connect.services.company.external;

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

	@Override
	public boolean login(String id, String password) {
		String uri = "/api/employees/login";
		
		String host = this.protocol + this.domainProperty.getRelayScg();
		//String apiUrl = host + uri;
		String apiUrl = "http://localhost:8080/samples/login";
		this.logger.debug("apiUrl : " + apiUrl);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("password", password);
		
		Map<String, Object> resultData = null;
		try {
			this.httpUtils.postApiForMap(apiUrl, params, null);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public List<Map<String, Object>> getMembers() {
		// TODO Auto-generated method stub
		/*
		 *  응답예시.
		 	[ {
			  "id" : "170941501",			// 
			  "comId" : "18",
			  "comName" : "SCG솔루션즈(주)",		
			  "nmKor" : "김은아",
			  "nmEng" : "Kim Euna",
			  "departDptCd" : "200553",
			  "deprtNmKor" : "경기5직영",
			  "posClsCd" : "J1 ",
			  "posClsName" : "J1",
			  "posSortOrder" : 65,
			  "posRnkCd" : "500",
			  "posRnkName" : "사원",
			  "rnkSortOrder" : 10,
			  "telephoneNum1" : null,
			  "telephoneNum2" : null,
			  "telephoneNum3" : null,
			  "cellphoneNum1" : "010",
			  "cellphoneNum2" : null,
			  "cellphoneNum3" : "6928",
			  "emailAddr1" : "kea6928@naver.com",
			  "userStatus" : "Y",
			  "branchYn" : "N",
			  "branchCd" : "101500",
			  "branchNm" : null,
			  "branchFlag" : "40",
			  "tabletUseYn" : null,
			  "telephone" : {
			    "num1" : null,
			    "num2" : null,
			    "num3" : null
			  },
			  "cellphone" : {
			    "num1" : "010",
			    "num2" : null,
			    "num3" : "6928"
			  },
			  "insideTelNum" : null
			}
		]
		 */
		
		String uri = "/api/safescg/employees?comIds=18";
		String apiUrl = this.domainProperty.getRelayScg() + uri;
		this.logger.debug("url : " + apiUrl);

		List<Map<String, Object>> resultData = this.httpUtils.getApiForList(apiUrl);
		if(resultData != null) {
			this.logger.debug("resultData : " + resultData.toString());
			
		}else {
			this.logger.debug("resultData is null");
			
		}
		
		return null;
	}

	@Override
	public Member getMemberInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		/*
		 * 	결과 예시.
			{
			  "id" : "csmaster1",
			  "name" : "서울도시가스",
			  "deptCode" : "200273",
			  "deptName" : "콜센터",
			  "positionClassCode" : " ",
			  "positionClassName" : null,
			  "positionRankCode" : "   ",
			  "positionRankName" : null,
			  "cellphone" : {
			    "num1" : " ",
			    "num2" : null,
			    "num3" : " "
			  },
			  "email" : "csmaster@seoulgas.co.kr",
			  "userStatus" : "Y",
			  "insideTelephone" : {
			    "num1" : "02",
			    "num2" : "323",
			    "num3" : "9681"
			  }
			}
		 */
		String id = DataUtils.getString(params, "id", "");
		String uri = "/api/safescg/employees/" + id;
		String apiUrl = this.domainProperty.getRelayScg() + uri;
		this.logger.debug("url : " + apiUrl);

		Map<String, Object> resultData = this.httpUtils.getApiForMap(apiUrl);
		if(resultData != null) {
			this.logger.debug("resultData : " + resultData.toString());
			
		}else {
			this.logger.debug("resultData is null");
			
		}
		
		
		return null;
	}

	@Override
	public int sendMinwon(Map<String, Object> params) {
		// TODO Auto-generated method stub
		/*
			{
			    "id":"20200922000691"
			}
		 */
		String uri = "/api/chattalk/minwons";
		String apiUrl = this.domainProperty.getRelayScg() + uri;
		
		HttpHeaders headers = null;
		
		this.logger.debug("url : " + apiUrl);
		this.logger.debug("params : " + params.toString());
		
		Map<String, Object> resultData = this.httpUtils.postApiForMap(apiUrl, params, headers);
		String id = DataUtils.getString(resultData, "id", "");
		
		return Integer.parseInt(id);
	}

	@Override
	public List<Map<String, Object>> getContractList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		/*
			[
			   {
			      "useContractNum":"6000000486",
			      "main":0,
			      "name":"우리집",
			      "jinbunAddress":null,
			      "newAddress":"서울특별시 마포구 ********,103호 (***)"
			   },
			   {
			      "useContractNum":"6000000502",
			      "main":0,
			      "name":"장모님",
			      "jinbunAddress":null,
			      "newAddress":"서울특별시 마포구 ******, (****)"
			   }
			]
		 */
		String id = DataUtils.getString(params, "id", "");
		String uri = "/api/matt/contracts?member=" + id;
		String apiUrl = this.domainProperty.getRelayScg() + uri;
		this.logger.debug("url : " + apiUrl);

		List<Map<String, Object>> resultData = this.httpUtils.getApiForList(apiUrl);
		if(resultData != null) {
			this.logger.debug("resultData : " + resultData.toString());
			
		}else {
			this.logger.debug("resultData is null");
			
		}
		
		return null;
	}
	
	@Override
	public Map<String, Object> getCustomerInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		/*
			{
			   "name":"이유진",
			   "handphone":"01044588856"
			}
		 */
		
		String id = DataUtils.getString(params, "id", "");
		String uri = "/api/matt/profile?member=" + id;
		String apiUrl = this.domainProperty.getRelayScg() + uri;
		this.logger.debug("url : " + apiUrl);

		Map<String, Object> resultData = this.httpUtils.getApiForMap(apiUrl);
		if(resultData != null) {
			this.logger.debug("resultData : " + resultData.toString());
			
		}else {
			this.logger.debug("resultData is null");
			
		}
		
		
		return null;
	}

	@Override
	public Contract getContractDetail(Map<String, Object> params) {
		
		/*
			{
			   "contractInfo":{
			      "useContractNum":"6000000486",
			      "customerNum":"1004538395",
			      "customerName":"김태수",
			      "firmName":"..",
			      "smsYn":"Y",
			      "oldZipcode1":"121",
			      "oldZipcode2":"809",
			      "branchCode":"100664",
			      "centerCode":"32",
			      "centerName":"강북2고객센터",
			      "centerPhone":"02-701-0337",
			      "meterNum":"300000486",
			      "meterIdNum":"118411000671",
			      "meterValidYm":"202203",
			      "meterLocationFlag":"10",
			      "deadlineFlag":"20",
			      "installPlaceNum":"200000486",
			      "gmtrBaseDay":"26",
			      "billSendMethodCode":"80",
			      "billSendMethod":"모바일",
			      "billSendMethodLastReqName":"김태수",
			      "billEmail":"doolee00@gmail.com",
			      "billCpNumber":{
			         "num1":"010",
			         "num2":"8324",
			         "num3":"8360"
			      },
			      "paymentMethodCode":"20",
			      "paymentType":"은행이체",
			      "unpayAmt":0.0,
			      "contractStatusCode":"10",
			      "contractStatus":"정상",
			      "useCode":"2",
			      "productCode":"33",
			      "productName":"업무난방용",
			      "productContractSeq":"001",
			      "takerEmployeeId":"200708016",
			      "takerEmployeeName":"신현숙",
			      "companyName":"서현ENG(주)",
			      "compensNum":null,
			      "cellPhoneNumber":{
			         "num1":"010",
			         "num2":"8324",
			         "num3":"8360"
			      },
			      "telPhoneNumber":{
			         "num1":"02",
			         "num2":"701",
			         "num3":"6654"
			      },
			      "selfReqFlag":"1",
			      "udocCount":1,
			      "jibunAddress":"서울 마포구 *** 18-30번지 103호",
			      "newAddress":"서울특별시 마포구 ********,103호 (***)",
			      "birthday":"920202",
			      "sex":"M",
			      "newAddressBase":"서울특별시 마포구 고산18길",
			      "jibunAddressBase":"서울 마포구 대흥동",
			      "newAddressDetail":"10,103호 (대흥동)",
			      "jibunAddressDetail":"18-30번지 103호"
			   },
			   "billDetailWithHis":{
			      "billDetailInfo":{
			         "useContractNum":"6000000486",
			         "requestYm":"202001",
			         "deadlineFlag":"20",
			         "paymentDeadline":"20200120",
			         "basicRate":0.0,
			         "useRate":232105.0,
			         "discountAmt":0.0,
			         "replacementCost":0.0,
			         "cancelCharge":0.0,
			         "vat":23210.0,
			         "overdueAmt":0.0,
			         "adjustmentAmt":0.0,
			         "adjustmentAltAmt":0.0,
			         "cutAmt":-5.0,
			         "chargeAmt":255310.0,
			         "paymentType":"자동이체(은행)",
			         "paymentTypeCode":"20",
			         "thisMonthIndicatorQty":2511,
			         "lastMonthIndicatorQty":2180,
			         "usageQty":331,
			         "factor":0.9996,
			         "factorQty":330.8676,
			         "averageEnergyQty":42.605,
			         "useEnergyQty":14096.6141,
			         "meterNum":"300000486",
			         "meterIdNum":"118411000671",
			         "gmtrYmFlag":"10",
			         "usePeriodStart":"20191126",
			         "usePeriodEnd":"20191225",
			         "lastMonthUsageQty":7.0,
			         "annualMonthUsageQty":125.0,
			         "payDate":"20200120",
			         "unpayAmt":0.0,
			         "bank":"우리은행",
			         "accountNum":"08624996502001",
			         "accountHolder":"이원준",
			         "prePayDate":"20191220",
			         "readDate":"26",
			         "readCode":"정기검침(PDA)",
			         "meterman":"신현숙",
			         "productYmd":"20190708",
			         "productCost1":0.0,
			         "productCost2":16.4653,
			         "productName1":" ",
			         "productName2":"업무용난방",
			         "virtualAccount":{
			            "accountName":"이원준",
			            "accounts":[
			               {
			                  "bankCode":"020",
			                  "name":"우리은행(은행)",
			                  "account":"29800105-218194"
			               },
			               {
			                  "bankCode":"003",
			                  "name":"기업은행(은행)",
			                  "account":"59503752-997969"
			               },
			               {
			                  "bankCode":"088",
			                  "name":"신한은행(은행)",
			                  "account":"27990102-779195"
			               },
			               {
			                  "bankCode":"027",
			                  "name":"한국씨티(은행)",
			                  "account":"17608049-92131"
			               },
			               {
			                  "bankCode":"081",
			                  "name":"KEB하나(은행)",
			                  "account":"25291057-176037"
			               },
			               {
			                  "bankCode":"023",
			                  "name":"SC은행",
			                  "account":"43416000-479196"
			               },
			               {
			                  "bankCode":"011",
			                  "name":"농협은행",
			                  "account":"79001576-804385"
			               },
			               {
			                  "bankCode":"004",
			                  "name":"국민은행(은행)",
			                  "account":"80179078-206193"
			               }
			            ]
			         },
			         "reportingDate":"20200111",
			         "regiNum":"8027100196",
			         "supplyRegiNum":"1098131605",
			         "supplyPrice":232105.0,
			         "supplyPriceVat":23210.0,
			         "useCode":"2",
			         "paymentCodeCard":"2",
			         "paymentCodeBank":"2",
			         "paymentFlag":"완납",
			         "paymentFlagCode":"1",
			         "customerNum":"1004538395",
			         "customerName":"김태수",
			         "customerCellPhoneNumber":{
			            "num1":"010",
			            "num2":"8324",
			            "num3":"8360"
			         },
			         "taxBillFlag":"10",
			         "tranReqDateCard":"20200116",
			         "tranReqDateBank":"20200116",
			         "payMethodCode":"20",
			         "payMethod":"자동이체(은행)",
			         "stopHoldPlanDate":" ",
			         "unpayInfos":[
			            
			         ],
			         "previousUnpayInfos":[
			            
			         ],
			         "jibunAddressType1":"서울 마포구 *** 18-30번지 103호 ",
			         "newAddressType1":"서울특별시 마포구 ********,103호 (***)",
			         "jibunAddressType2":"서울 마포구 대흥동 ****",
			         "newAddressType2":"서울특별시 마포구 고산18길 ****",
			         "allUnpayAmounts":0.0,
			         "allPayAmounts":255310.0,
			         "previousUnpayAmounts":0.0,
			         "paymentCode":"2",
			         "receiveAmt":255310.0,
			         "payable":false,
			         "stopHoldPlanYn":false,
			         "socialWelfareDiscount":false
			      },
			      "history":[
			         {
			            "useContractNum":"6000000486",
			            "requestYm":"201912",
			            "usePeriodStart":"20191026",
			            "usePeriodEnd":"20191125",
			            "indicatorQty":2180,
			            "usageQty":7,
			            "factorQty":6.9972,
			            "useEnergyQty":299.7531,
			            "chargeAmt":5420.0,
			            "receiveAmt":5420.0,
			            "unpayAmt":0.0,
			            "paymentType":"자동이체(은행)",
			            "paymentFlag":"완납",
			            "paymentFlagCode":"1",
			            "useCode":"2",
			            "paymentCodeCard":"2",
			            "paymentCodeBank":"2",
			            "payDate":"20191220",
			            "deadlineFlag":"20",
			            "jibunAddressType1":"서울 마포구 *** 18-30번지 103호 ",
			            "newAddressType1":"서울특별시 마포구 ********,103호 (***)",
			            "jibunAddressType2":"서울 마포구 대흥동 ****",
			            "newAddressType2":"서울특별시 마포구 고산18길 ****",
			            "paymentCode":"2",
			            "payable":false
			         },
			         {
			            "useContractNum":"6000000486",
			            "requestYm":"202001",
			            "usePeriodStart":"20191126",
			            "usePeriodEnd":"20191225",
			            "indicatorQty":2511,
			            "usageQty":331,
			            "factorQty":330.8676,
			            "useEnergyQty":14096.6141,
			            "chargeAmt":255310.0,
			            "receiveAmt":255310.0,
			            "unpayAmt":0.0,
			            "paymentType":"자동이체(은행)",
			            "paymentFlag":"완납",
			            "paymentFlagCode":"1",
			            "useCode":"2",
			            "paymentCodeCard":"2",
			            "paymentCodeBank":"2",
			            "payDate":"20200120",
			            "deadlineFlag":"20",
			            "jibunAddressType1":"서울 마포구 *** 18-30번지 103호 ",
			            "newAddressType1":"서울특별시 마포구 ********,103호 (***)",
			            "jibunAddressType2":"서울 마포구 대흥동 ****",
			            "newAddressType2":"서울특별시 마포구 고산18길 ****",
			            "paymentCode":"2",
			            "payable":false
			         }
			      ]
			   }
			}
		 */
		
		String contractNum = DataUtils.getString(params, "contractNum", "");
		String uri = "/api/chattalk/contractInfo/" + contractNum + "/detail+lastbill";
		String apiUrl = this.domainProperty.getRelayScg() + uri;
		this.logger.debug("url : " + apiUrl);

		Map<String, Object> resultData = this.httpUtils.getApiForMap(apiUrl);
		if(resultData != null) {
			this.logger.debug("resultData : " + resultData.toString());
			
		}else {
			this.logger.debug("resultData is null");
			
		}
		
		return null;
	}
	
	@Override
	public Map<String, Object> getContractMonthlyDetail(Map<String, Object> params){
		
		/*
			{
			   "useContractNum":"6000000486",
			   "requestYm":"201911",
			   "deadlineFlag":"20",
			   "paymentDeadline":"20191120",
			   "basicRate":0.0,
			   "useRate":0.0,
			   "discountAmt":0.0,
			   "replacementCost":0.0,
			   "cancelCharge":0.0,
			   "vat":0.0,
			   "overdueAmt":0.0,
			   "adjustmentAmt":0.0,
			   "adjustmentAltAmt":0.0,
			   "cutAmt":0.0,
			   "chargeAmt":0.0,
			   "paymentType":null,
			   "paymentTypeCode":null,
			   "thisMonthIndicatorQty":2173,
			   "lastMonthIndicatorQty":2173,
			   "usageQty":0,
			   "factor":0.9996,
			   "factorQty":0.0,
			   "averageEnergyQty":42.902,
			   "useEnergyQty":0.0,
			   "meterNum":"300000486",
			   "meterIdNum":"118411000671",
			   "gmtrYmFlag":"10",
			   "usePeriodStart":"20190926",
			   "usePeriodEnd":"20191025",
			   "lastMonthUsageQty":0.0,
			   "annualMonthUsageQty":0.0,
			   "payDate":" ",
			   "unpayAmt":0.0,
			   "bank":"우리은행",
			   "accountNum":"08624996502001",
			   "accountHolder":"이원준",
			   "prePayDate":null,
			   "readDate":"26",
			   "readCode":"정기검침(PDA)",
			   "meterman":"신현숙",
			   "productYmd":"20190708",
			   "productCost1":0.0,
			   "productCost2":16.4653,
			   "productName1":" ",
			   "productName2":"업무용난방",
			   "virtualAccount":{
			      "accountName":"이원준",
			      "accounts":[
			         {
			            "bankCode":"020",
			            "name":"우리은행(은행)",
			            "account":"29800105-218194"
			         },
			         {
			            "bankCode":"003",
			            "name":"기업은행(은행)",
			            "account":"59503752-997969"
			         },
			         {
			            "bankCode":"088",
			            "name":"신한은행(은행)",
			            "account":"27990102-779195"
			         },
			         {
			            "bankCode":"027",
			            "name":"한국씨티(은행)",
			            "account":"17608049-92131"
			         },
			         {
			            "bankCode":"081",
			            "name":"KEB하나(은행)",
			            "account":"25291057-176037"
			         },
			         {
			            "bankCode":"023",
			            "name":"SC은행",
			            "account":"43416000-479196"
			         },
			         {
			            "bankCode":"011",
			            "name":"농협은행",
			            "account":"79001576-804385"
			         },
			         {
			            "bankCode":"004",
			            "name":"국민은행(은행)",
			            "account":"80179078-206193"
			         }
			      ]
			   },
			   "reportingDate":"20191111",
			   "regiNum":"8027100196",
			   "supplyRegiNum":"1098131605",
			   "supplyPrice":0.0,
			   "supplyPriceVat":0.0,
			   "useCode":"2",
			   "paymentCodeCard":"2",
			   "paymentCodeBank":"2",
			   "paymentFlag":"완납",
			   "paymentFlagCode":"1",
			   "customerNum":"1004538395",
			   "customerName":"김태수",
			   "customerCellPhoneNumber":{
			      "num1":"010",
			      "num2":"8324",
			      "num3":"8360"
			   },
			   "taxBillFlag":"10",
			   "tranReqDateCard":"20191118",
			   "tranReqDateBank":"20191118",
			   "payMethodCode":"20",
			   "payMethod":"자동이체(은행)",
			   "stopHoldPlanDate":" ",
			   "unpayInfos":[
			      
			   ],
			   "previousUnpayInfos":[
			      
			   ],
			   "jibunAddressType1":"서울 마포구 *** 18-30번지 103호 ",
			   "newAddressType1":"서울특별시 마포구 ********,103호 (***)",
			   "jibunAddressType2":"서울 마포구 대흥동 ****",
			   "newAddressType2":"서울특별시 마포구 고산18길 ****",
			   "allUnpayAmounts":0.0,
			   "allPayAmounts":0.0,
			   "previousUnpayAmounts":0.0,
			   "paymentCode":"2",
			   "receiveAmt":0.0,
			   "payable":false,
			   "stopHoldPlanYn":false,
			   "socialWelfareDiscount":false
			}
		 */
		
		String contractNum = DataUtils.getString(params, "contractNum", "");
		String requestYm = DataUtils.getString(params, "requestYm", "");
		String deadlineFlag = DataUtils.getString(params, "deadlineFlag", "");
		
		String uri = "/api/bill/" + contractNum + "/detail?requestYm=" + requestYm + "&deadlineFlag=" + deadlineFlag;
		String apiUrl = this.domainProperty.getRelayScg() + uri;
		this.logger.debug("url : " + apiUrl);

		Map<String, Object> resultData = this.httpUtils.getApiForMap(apiUrl);
		if(resultData != null) {
			this.logger.debug("resultData : " + resultData.toString());
			
		}else {
			this.logger.debug("resultData is null");
			
		}
		
		return null;
	}

	@Override
	public int isHoliday(Map<String, Object> params) {
		/*
			{
			  "value" : false
			}
		 */
		
		String day = DataUtils.getString(params, "day", "");
		String cmd = DataUtils.getString(params, "cmd", "checkHoliday");
		
		String uri = "/api/safescg/calendar/" + day + "?cmd=" + cmd;
		String apiUrl = this.domainProperty.getRelayScg() + uri;
		this.logger.debug("url : " + apiUrl);

		Map<String, Object> resultData = this.httpUtils.getApiForMap(apiUrl);
		if(resultData != null) {
			this.logger.debug("resultData : " + resultData.toString());
			
		}else {
			this.logger.debug("resultData is null");
			
		}
		
		return 0;
	}

	@Override
	public String getCompanyId() {
		return Constant.COMPANY_CODE_SEOUL;
	}

	@Override
	public String getCompanyName() {
		return Constant.COMPANY_NAME_SEOUL;
	}

	@Override
	public int isWorking() {
		int iswork = 1;
		
		Calendar cal = Calendar.getInstance();
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		System.out.println("현재 시각은 " + year + "년도 " + month + "월 " + day + "일 " + hour + "시 " + min + "분 " + sec + "초입니다.");
		
		if(hour < 9 || hour > 17 || (hour == 17 && min > 30)) {
			// 근무 외 시간.(9시 이전, 17시 30분 이후)
			iswork = 2;
		
		}else{
			if(false) {	// 휴일 또는 근무 외 시 (MIS를 통해 확인)	
				iswork = 2;
				
			}else {		// 근무일자일 경우
				if(hour == 12) {
					// 점심시간.
					iswork = 3;
				}else {
					
				}
			}
		}
		
		return iswork;
	}
}
