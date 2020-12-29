package com.scglab.connect.services.category;

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
@RequestMapping(name = "카테고리 관리", value="/api/category")
public class CategoryController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CategoryService categoryService;
	
	@Auth
	@RequestMapping(name="카테고리 전체 조회", method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> total(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.categoryService.total(params, request);
	}
	
	@Auth
	@RequestMapping(name="대분류, 중분류, 소분류 카테고리 조회(목록)", method = RequestMethod.GET, value = "tree", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CategoryLarge> tree(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.categoryService.tree(params, request);
	}
	
	@Auth
	@RequestMapping(name="대분류 카테고리 조회(목록)", method = RequestMethod.GET, value = "/large", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CategoryLarge> categoriesLarge(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.categoryService.categoryLargeList(params, request);
	}
	
	@Auth
	@RequestMapping(name="대분류 카테고리 상세조회", method = RequestMethod.GET, value = "/large/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CategoryLarge categoryLarge(@PathVariable int id, @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.categoryLarge(params, request);
	}
	
	@Auth
	@RequestMapping(name="대분류 카테고리 생성(등록)", method = RequestMethod.POST, value = "/large", produces = MediaType.APPLICATION_JSON_VALUE)
	public CategoryLarge saveLg(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.categoryService.saveCategoryLarge(params, request);
	}
	
	@Auth
	@RequestMapping(name="대분류 카테고리 정보 변경(수정)", method = RequestMethod.PUT, value = "/large/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public  CategoryLarge updateLg(@PathVariable int id, @RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.updateCategoryLarge(params, request);
	}
	
	@Auth
	@RequestMapping(name="대분류 카테고리 삭제", method = RequestMethod.DELETE, value = "/large/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public  Map<String, Object> deleteLg(@PathVariable int id, @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("type", "large");
		params.put("id", id);
		return this.categoryService.deleteCategoryLarge(params, request);
	}
	
	@Auth
	@RequestMapping(name="중분류 카테고리 조회(목록)", method = RequestMethod.GET, value = "/middle", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CategoryMiddle> categoriesMiddle(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.categoryService.categoryMiddleList(params, request);
	}
	
	@Auth
	@RequestMapping(name="중분류 카테고리 상세조회", method = RequestMethod.GET, value = "/middle/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CategoryMiddle categoryMiddle(@PathVariable int id, @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.categoryMiddle(params, request);
	}
	
	@Auth
	@RequestMapping(name="중분류 카테고리 생성(등록)", method = RequestMethod.POST, value = "/middle", produces = MediaType.APPLICATION_JSON_VALUE)
	public CategoryMiddle saveMd(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.categoryService.saveCategoryMiddle(params, request);
	}
	
	@Auth
	@RequestMapping(name="중분류 카테고리 정보 변경(수정)", method = RequestMethod.PUT, value = "/middle/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public  CategoryMiddle updateMd(@PathVariable int id, @RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.updateCategoryMiddle(params, request);
	}
	
	@Auth
	@RequestMapping(name="중분류 카테고리 삭제", method = RequestMethod.DELETE, value = "/middle/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public  Map<String, Object> deleteMd(@PathVariable int id, @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.deleteCategoryMiddle(params, request);
	}
	
	@Auth
	@RequestMapping(name="소분류 카테고리 조회(목록)", method = RequestMethod.GET, value = "/small", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CategorySmall> categoriesSmall(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.categoryService.categorySmallList(params, request);
	}
	
	@Auth
	@RequestMapping(name="소분류 카테고리 조회(목록)", method = RequestMethod.GET, value = "/small/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CategorySmall categorySmall(@PathVariable int id, @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.categorySmall(params, request);
	}
	
	@Auth
	@RequestMapping(name="소분류 카테고리 생성(등록)", method = RequestMethod.POST, value = "/small", produces = MediaType.APPLICATION_JSON_VALUE)
	public CategorySmall saveSm(@RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.categoryService.saveCategorySmall(params, request);
	}
	
	@Auth
	@RequestMapping(name="소분류 카테고리 정보 변경(수정)", method = RequestMethod.PUT, value = "/small/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public  CategorySmall updateSm(@PathVariable int id, @RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.updateCategorySmall(params, request);
	}
	
	@Auth
	@RequestMapping(name="소분류 카테고리 삭제", method = RequestMethod.DELETE, value = "/small/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public  Map<String, Object> deleteSm(@PathVariable int id, @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.deleteCategorySmall(params, request);
	}
	
	
	@Auth
	@RequestMapping(name="대분류 카테고리 정렬 정보 수정.", method = RequestMethod.PUT, value = "/large/{id}/sort-index", produces = MediaType.APPLICATION_JSON_VALUE)
	public CategoryLarge sortIndexLg(@PathVariable int id, @RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.updateSortIndexCategoryLarge(params, request);
	}
	
	@Auth
	@RequestMapping(name="중분류 카테고리 정렬 정보 수정.", method = RequestMethod.PUT, value = "/middle/{id}/sort-index", produces = MediaType.APPLICATION_JSON_VALUE)
	public CategoryMiddle sortIndexMd(@PathVariable int id, @RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.updateSortIndexCategoryMiddle(params, request);
	}
	
	@Auth
	@RequestMapping(name="소분류 카테고리 정렬 정보 수정.", method = RequestMethod.PUT, value = "/small/{id}/sort-index", produces = MediaType.APPLICATION_JSON_VALUE)
	public CategorySmall sortIndexSm(@PathVariable int id, @RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
		params.put("id", id);
		return this.categoryService.updateSortIndexCategorySmall(params, request);
	}
}


