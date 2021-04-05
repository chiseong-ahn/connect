package com.scglab.connect.services.company.external;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.utils.DataUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(name = "외부 연동 관리", value = "/api/external")
public class ExtenalController {

	@Autowired
	private ExternalService extenalService;
	
	@Autowired
	private CommonService commonService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(name = "푸시발송", method = RequestMethod.POST, value = "/push", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> sendPush(@RequestBody Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return this.extenalService.sendPush(params, request, response);
	}

	@RequestMapping(name = "로그인", method = RequestMethod.POST, value = "/{companyId}/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object login(@PathVariable String companyId, @RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = DataUtils.getParameter(request, "id", "");
		String password = DataUtils.getParameter(request, "password", "");
		return this.commonService.getCompany(companyId).login(id, password);
	}

	@RequestMapping(name = "직원목록 조회", method = RequestMethod.GET, value = "/{companyId}/employees", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> employees(@PathVariable String companyId, @RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.commonService.getCompany(companyId).employees();
	}

	@RequestMapping(name = "직원상세 조회", method = RequestMethod.GET, value = "/{companyId}/employees/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> employee(@PathVariable String companyId, @PathVariable String id,
			@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return this.commonService.getCompany(companyId).employee(id);
	}

	@RequestMapping(name = "민원등록", method = RequestMethod.POST, value = "/{companyId}/minwons", produces = MediaType.APPLICATION_JSON_VALUE)
	public String minwons(@PathVariable String companyId, @RequestParam Map<String, String> params,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.commonService.getCompany(companyId).minwons(params);
	}

	@RequestMapping(name = "고객의 계약정보 목록", method = RequestMethod.GET, value = "/{companyId}/contracts/{member}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> contracts(@PathVariable String companyId, @PathVariable String member,
			@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return this.commonService.getCompany(companyId).contracts(member);
	}

	@RequestMapping(name = "고객의 계약상세정보", method = RequestMethod.GET, value = "/{companyId}/contracts/{member}/{useContractNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> contractInfo(@PathVariable String companyId, @PathVariable long member,
			@PathVariable String useContractNum, @RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return this.commonService.getCompany(companyId).contractInfo(useContractNum);
	}

	@RequestMapping(name = "고객의 결제 상세정보", method = RequestMethod.GET, value = "/{companyId}/contracts/{member}/{useContractNum}/bil", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> contractBil(@PathVariable String companyId, @PathVariable long member,
			@PathVariable String useContractNum, @RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String requestYm = DataUtils.getString(params, "requestYm", "");
		String deadlineFlag = DataUtils.getString(params, "deadlineFlag", "");
		return this.commonService.getCompany(companyId).contractBill(useContractNum, requestYm, deadlineFlag);
	}

	@RequestMapping(name = "고객의 결제 상세정보", method = RequestMethod.GET, value = "/{companyId}/isWorking", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> test(@PathVariable String companyId, @RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Calendar cal = Calendar.getInstance();
		System.out.println(cal);

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);

		this.logger.debug("time : " + hour + " " + min + " " + sec);

		return null;
	}
	
	@RequestMapping(name = "근무요일확인", method = RequestMethod.GET, value = "/{companyId}/holiday", produces = MediaType.APPLICATION_JSON_VALUE)
	public int holiday(@PathVariable String companyId, @RequestParam Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return this.commonService.getCompany(companyId).getWorkCalendar();
	}

}