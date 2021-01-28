package com.scglab.connect.services.link;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.scglab.connect.base.annotations.Auth;

@RestController
@RequestMapping(name = "링크", value = "/api/link")
public class LinkController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	LinkService linkService;

	@Auth
	@RequestMapping(name = "링크 메뉴 전체 조회", method = RequestMethod.GET, value = "findMenuAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<LinkMenu> findMenuAll(@RequestParam Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		return this.linkService.findMenuAll(params, request);
	}

	@Auth
	@RequestMapping(name = "활성화되어있는 메뉴에 속한 링크 상세조회", method = RequestMethod.GET, value = "findDetailByMenuIdAndEnableStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<LinkMenu> findDetailByMenuIdAndEnableStatus(@RequestParam Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		return this.linkService.findDetail(params, request);
	}

	@Auth
	@RequestMapping(name = "링크 전체목록 : 트리", method = RequestMethod.GET, value = "tree", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<LinkMenu> tree(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.linkService.findTree(params, request);
	}

	@Auth
	@RequestMapping(name = "링크메뉴 상세조회", method = RequestMethod.GET, value = "/linkMenu/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public LinkMenu linkMenu(@PathVariable long id, @RequestParam Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.linkService.linkMenu(params, request);
	}

	@Auth
	@RequestMapping(name = "링크메뉴 등록", method = RequestMethod.POST, value = "/linkMenu", produces = MediaType.APPLICATION_JSON_VALUE)
	public LinkMenu createLinkMenu(@RequestBody Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		return this.linkService.createLinkMenu(params, request);
	}

	@Auth
	@RequestMapping(name = "링크메뉴 수정", method = RequestMethod.PUT, value = "/linkMenu/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public LinkMenu updateLinkMenu(@PathVariable long id, @RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.linkService.updateLinkMenu(params, request);
	}

	@Auth
	@RequestMapping(name = "링크메뉴 삭제", method = RequestMethod.DELETE, value = "/linkMenu/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> deleteLinkMenu(@PathVariable long id, @RequestParam Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		params.put("id", id);
		params.put("menuId", id);
		return this.linkService.deleteLinkMenu(params, request);
	}

	@Auth
	@RequestMapping(name = "Link Detail 상세조회", method = RequestMethod.GET, value = "/linkDetail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public LinkDetail linkDetail(@PathVariable long id, @RequestParam Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.linkService.linkDetail(params, request);
	}

	@Auth
	@RequestMapping(name = "Link Detail 등록", method = RequestMethod.POST, value = "/linkDetail", produces = MediaType.APPLICATION_JSON_VALUE)
	public LinkDetail createLinkDetail(@RequestBody Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		return this.linkService.createLinkDetail(params, request);
	}

	@Auth
	@RequestMapping(name = "Link Detail 수정", method = RequestMethod.PUT, value = "/linkDetail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public LinkDetail updateLinkDetail(@PathVariable long id, @RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.linkService.updateLinkDetail(params, request);
	}

	@Auth
	@RequestMapping(name = "Link Detail의 활성화상태 변경", method = RequestMethod.PUT, value = "/linkDetail/{id}/enable", produces = MediaType.APPLICATION_JSON_VALUE)
	public LinkDetail updateLinkDetailEnable(@PathVariable long id, @RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.linkService.updateLinkDetailEnable(params, request);
	}

	@Auth
	@RequestMapping(name = "Link Detail 삭제", method = RequestMethod.DELETE, value = "/linkDetail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> deleteLinkDetail(@PathVariable long id, @RequestParam Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.linkService.deleteLinkDetail(params, request);
	}
}
