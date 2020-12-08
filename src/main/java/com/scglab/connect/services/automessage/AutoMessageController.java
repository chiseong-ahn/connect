package com.scglab.connect.services.automessage;

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

import com.scglab.connect.base.annotations.Auth;
import com.scglab.connect.constant.Constant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auto-message")
@Tag(name = "자동메세지 관리", description = "자동메세지 API를 제공합니다.")
public class AutoMessageController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AutoMessageService autoMessageService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="자동메세지 목록", description = "자동메세지 목록", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "type", description = "메세지 유형", required = true, in = ParameterIn.QUERY, example = "1"),
		@Parameter(name = "message", description = "메세지", required = false, in = ParameterIn.QUERY, example = ""),
	})
	public List<AutoMessage> findAll(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.autoMessageService.findAll(params, request, response);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="자동메세지 상세", description = "자동메세지 상세", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public AutoMessage detail(@Parameter(description = "자동메세지 id") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.autoMessageService.getDetail(params, request, response);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="자동메세지 등록", description = "자동메세지 등록", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public AutoMessage regist(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.autoMessageService.regist(params, request, response);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="자동메세지 수정", description = "자동메세지 수정", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public AutoMessage update(@Parameter(description = "자동메세지 id") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.autoMessageService.update(params, request, response);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="자동메세지 삭제", description = "자동메세지 삭제", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> delete(@Parameter(description = "자동메세지 id") @PathVariable int id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		params.put("id", id);
		return this.autoMessageService.delete(params, request, response);
	}

}
	