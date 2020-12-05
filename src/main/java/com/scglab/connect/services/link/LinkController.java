package com.scglab.connect.services.link;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

@RestController
@RequestMapping("/api/link")
@Tag(name = "링크", description = "링크 API")
public class LinkController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	LinkService linkService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "findMenuAll", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="링크 메뉴 전체 조회", description = "링크 메뉴 전체 조회", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "menuId", description = "속해있는 메뉴 id", required = true, in = ParameterIn.QUERY, example = "")
	})
	public List<LinkMenu> findMenuAll(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.linkService.findMenuAll(params, request);
	}
	
	
	@Auth //findDetilByMenuIdAndEnableStatus
	@RequestMapping(method = RequestMethod.GET, value = "findDetailByMenuIdAndEnableStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="활성화되어있는 메뉴에 속한 링크 상세조회", description = "활성화되어있는 메뉴에 속한 링크 상세조회", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "menuId", description = "속해있는 메뉴 id", required = true, in = ParameterIn.QUERY, example = "")
	})
	public List<LinkMenu> findDetailByMenuIdAndEnableStatus(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.linkService.findDetail(params, request);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "tree", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="링크 전체목록 : 트리", description = "링크 전체목록 : 트리", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	@Parameters({
		@Parameter(name = "menuId", description = "속해있는 메뉴 id", required = true, in = ParameterIn.QUERY, example = "")
	})
	public List<LinkMenu> tree(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.linkService.findTree(params, request);
	}
}


