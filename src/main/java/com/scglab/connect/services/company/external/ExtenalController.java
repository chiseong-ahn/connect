package com.scglab.connect.services.company.external;

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
@RequestMapping("/api/extenal")
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/{companyId}/members", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="직원목록 조회", description = "직원목록 조회")
	public Object members(@Parameter(description = "회사id", example = "1") @PathVariable String companyId, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return getCompany(companyId).getMembers();
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
	
	
}