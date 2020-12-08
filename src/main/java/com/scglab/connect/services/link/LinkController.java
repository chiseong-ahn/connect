package com.scglab.connect.services.link;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
	public List<LinkMenu> tree(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.linkService.findTree(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/linkMenu/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="링크메뉴 상세조회", description = "링크 전체목록 : 트리", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public LinkMenu linkMenu(@Parameter(description = "링크메뉴 id")@PathVariable long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.linkService.linkMenu(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/linkMenu", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="링크메뉴 등록", description = "링크메뉴 등록", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public LinkMenu createLinkMenu(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.linkService.createLinkMenu(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/linkMenu/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="링크메뉴 수정", description = "링크메뉴 수정", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public LinkMenu updateLinkMenu(@Parameter(description = "링크메뉴 id")@PathVariable long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.linkService.updateLinkMenu(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/linkMenu/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="링크메뉴 삭제", description = "링크메뉴 삭제", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> deleteLinkMenu(@Parameter(description = "링크메뉴 id")@PathVariable long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		params.put("menuId", id);
		return this.linkService.deleteLinkMenu(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/linkDetail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="Link Detail", description = "Link Detail 상세조회", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public LinkDetail linkDetail(@Parameter(description = "Link Detail id")@PathVariable long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.linkService.linkDetail(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/linkDetail", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="Link Detail 등록", description = "link Detail 등록", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public LinkDetail createLinkDetail(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.linkService.createLinkDetail(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/linkDetail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="Link Detail 수정", description = "link Detail 수정", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public LinkDetail updateLinkDetail(@Parameter(description = "Link Detail id") @PathVariable long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.linkService.updateLinkDetail(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/linkDetail/{id}/enable", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="Link Detail의 활성화상태 변경", description = "link Detail의 활성화상태 변경", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public LinkDetail updateLinkDetailEnable(@Parameter(description = "Link Detail id") @PathVariable long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.linkService.updateLinkDetailEnable(params, request);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/linkDetail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="Link Detail 삭제", description = "link Detail 삭제", security = {@SecurityRequirement(name = Constant.AUTH_BEARERR_KEY)})
	public Map<String, Object> deleteLinkDetail(@Parameter(description = "Link Detail id") @PathVariable long id, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.linkService.deleteLinkDetail(params, request);
	}
}


