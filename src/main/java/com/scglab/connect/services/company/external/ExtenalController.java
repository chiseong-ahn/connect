package com.scglab.connect.services.company.external;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scglab.connect.utils.DataUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/external")
@Tag(name = "외부 연동 관리", description = "")
public class ExtenalController {
	
	@Autowired private ExternalService extenalService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired private CompanyScg companyScg;
	@Autowired private CompanyInc companyInc;
	
	private ICompany getCompany(String companyId) {
		ICompany company = companyId.equals("1") ? this.companyScg : this.companyInc;
		return company;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/push", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="푸시발송", description = "푸시발송")
	@Parameters({
		@Parameter(name = "userno", in = ParameterIn.QUERY, description = "회원번호", required = true, example = "3825"),
		@Parameter(name = "message", in = ParameterIn.QUERY, description = "메세지", required = true, example = "테스트 발송.")
	})
	public Map<String, Object> sendPush(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.extenalService.sendPush(params, request, response);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/{companyId}/login", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="로그인", description = "로그인")
	@Parameters({
		@Parameter(name = "id", in = ParameterIn.QUERY, description = "아이디", required = true, example = "csmaster1"),
		@Parameter(name = "password", in = ParameterIn.QUERY, description = "비밀번호", required = true, example = "1212")
	})
	public Object login(@Parameter(description = "회사id", example = "1") @PathVariable String companyId, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String id = DataUtils.getParameter(request, "id", "");
		String password = DataUtils.getParameter(request, "password", "");
		
		return getCompany(companyId).login(id, password);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{companyId}/employees", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="직원목록 조회", description = "직원목록 조회")
	public List<Map<String, Object>> employees(@Parameter(description = "회사id", example = "1") @PathVariable String companyId, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return getCompany(companyId).employees();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{companyId}/employees/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="직원상세 조회", description = "직원상세 조회")
	public Map<String, Object> employee(@Parameter(description = "회사id", example = "1") @PathVariable String companyId, @Parameter(description = "아이디", example = "1") @PathVariable String id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return getCompany(companyId).employee(id);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{companyId}/minwons", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="민원등록", description = "민원등록")
	@Parameters({
		@Parameter(name = "customerMobileId", description = "가스앱 회원 id", required = true, in = ParameterIn.QUERY, example = "3716"),
		@Parameter(name = "useContractNum", description = "계약번호", required = true, in = ParameterIn.QUERY, example = "6004910783"),
		@Parameter(name = "reqName", description = "요청자명", required = true, in = ParameterIn.QUERY, example = "김윤석"),
		@Parameter(name = "classCode", description = "민원코드", required = true, in = ParameterIn.QUERY, example = "010202"),
		@Parameter(name = "transfer", description = "민원이관여부", required = true, in = ParameterIn.QUERY, example = "false"),
		@Parameter(name = "cellPhone.num1", description = "핸드폰 번호(앞 3자리)", required = true, in = ParameterIn.QUERY, example = "010"),
		@Parameter(name = "cellPhone.num2", description = "핸드폰 번호(중간 3~4자리)", required = true, in = ParameterIn.QUERY, example = "6601"),
		@Parameter(name = "cellPhone.num3", description = "핸드폰 번호(끝 4자리)", required = true, in = ParameterIn.QUERY, example = "5106"),
		@Parameter(name = "memo", description = "메모", required = true, in = ParameterIn.QUERY, example = "민원테스트"),
		@Parameter(name = "employeeId", description = "민원등록 직원 사번", required = true, in = ParameterIn.QUERY, example = "csmaster1"),
		@Parameter(name = "chatId", description = "채팅 id", required = true, in = ParameterIn.QUERY, example = "116"),
	})
	public String minwons(@Parameter(description = "회사id", example = "1") @PathVariable String companyId, @Parameter(hidden = true) @RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getCompany(companyId).minwons(params);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/{companyId}/contracts/{member}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="고객의 계약정보 목록", description = "고객의 계약정보 목록")
	public List<Map<String, Object>> contracts(@Parameter(description = "회사id", example = "1") @PathVariable String companyId, @Parameter(description = "가스앱 회원번호", example = "3369") @PathVariable String member, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getCompany(companyId).contracts(member);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/{companyId}/contracts/{member}/{useContractNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="고객의 계약상세정보", description = "고객의 계약상세정보")
	public Map<String, Object> contractInfo(@Parameter(description = "회사id", example = "1") @PathVariable String companyId, @Parameter(description = "가스앱 회원번호", example = "3369") @PathVariable long member, @Parameter(description = "사용계약번호", example = "6000000486") @PathVariable String useContractNum, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getCompany(companyId).contractInfo(useContractNum);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/{companyId}/contracts/{member}/{useContractNum}/bil", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="고객의 결제 상세정보", description = "고객의 결제 상세정보")
	@Parameters({
		@Parameter(name = "requestYm", description = "요청월", required = true, in = ParameterIn.QUERY, example = "202001"),
		@Parameter(name = "deadlineFlag", description = "납기구분", required = true, in = ParameterIn.QUERY, example = "20"),
	})
	public Map<String, Object> contractBil(@Parameter(description = "회사id", example = "1") @PathVariable String companyId, @Parameter(description = "가스앱 회원번호", example = "3369") @PathVariable long member, @Parameter(description = "사용계약번호", example = "6000000486") @PathVariable String useContractNum, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String requestYm = DataUtils.getString(params, "requestYm", "");
		String deadlineFlag = DataUtils.getString(params, "deadlineFlag", "");
		return getCompany(companyId).contractBill(useContractNum, requestYm, deadlineFlag);
	}
	
	
}