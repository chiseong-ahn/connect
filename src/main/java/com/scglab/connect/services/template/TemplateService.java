package com.scglab.connect.services.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.utils.DataUtils;

@Service
public class TemplateService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TemplateDao templateDao;
	
	public Map<String, Object> list(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		// 페이지 번호.
		int page = Integer.parseInt(DataUtils.getObjectValue(params, "page", "1"));
		page = page < 1 ? 1 : page;
		
		// 페이지당 노출할 게시물 수.
		int pageSize = Integer.parseInt(DataUtils.getObjectValue(params, "pageSize", "15"));
		pageSize = pageSize < 1 ? 15 : pageSize;
		
		// 대분류 카테고리
		String catelg = DataUtils.getObjectValue(params, "catelg", "");
		
		// 중분류 카테고리
		String catemd = DataUtils.getObjectValue(params, "catemd", "");
		
		// 소분류 카테고리
		String catesm = DataUtils.getObjectValue(params, "catesm", "");
		
		// 검색유형
		String keyfield = DataUtils.getObjectValue(params, "keyfield", "");
		
		// 검색어
		String keyword = DataUtils.getObjectValue(params, "keyword", "");
		
		// 조회 시작 번호.
		int startNum = (page - 1) * pageSize + 1;
		// 조회 마지막 번호.
		int endNum = pageSize;
		
		
		params.put("catelg", catelg);
		params.put("catemd", catemd);
		params.put("catesm", catesm);
		params.put("keyfield", keyfield);
		params.put("keyword", keyword);
		params.put("startNum", startNum);
		params.put("endNum", endNum);
		params.put("emp", "1");
		
		List<Map<String, Object>> list = null;
		int count = this.templateDao.selectCount(params);
		if(count > 0) {
			list = this.templateDao.selectAll(params);
		}
		
		data.put("total", count);
		data.put("list", list);
		data.put("page", page);
		data.put("pageSize", pageSize);
		
		return data;
	}
	
	public Map<String, Object> object(Map<String, Object> params, String id) throws Exception {
		Map<String, Object> object = this.templateDao.selectOne(params);
		return object;
	}
	
	public Map<String, Object> saveKeyword(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		
		int count = this.templateDao.selectCountKeyword(params);
		if(count == 0) {
			this.templateDao.insertKeyword(params);
		}
		
		Map<String, Object> keyword = this.templateDao.selectKeyword(params);
		
		data.put("keyword", keyword);
		return data;
	}
	
	public Map<String, Object> save(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		int result = 0;
		
		result = this.templateDao.insert(params);
		if(result > 0) {
			if(params.containsKey("id")) {
				this.logger.debug("generated key : " + params.get("id"));
				
				if(params.containsKey("keywords")) {
					String[] keywords = DataUtils.getObjectValue(params, "keywords", "").split(",");
					this.logger.debug("keywords : " + keywords.toString());
				
					for(String keyword : keywords) {
						params.put("keyword", keyword.trim());
						this.templateDao.insertTemplateKeyword(params);
					}
					
				}else{
					this.logger.debug("keywords is nothing!");
				}
			}else {
				this.logger.debug("generated key is nothing!");
			}
		}
		
		data.put("result", result > 0 ? true : false);
		return data;
	}
	
	public Map<String, Object> update(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		int result = 0;
		
		result = this.templateDao.update(params);
		if(result > 0) {
			if(params.containsKey("id")) {
				this.templateDao.deleteTemplateKeywords(params);
				if(params.containsKey("keywords")) {
					String[] keywords = DataUtils.getObjectValue(params, "keywords", "").split(",");
					this.logger.debug("keywords : " + keywords.toString());
				
					for(String keyword : keywords) {
						params.put("keyword", keyword.trim());
						this.templateDao.insertTemplateKeyword(params);
					}
					
				}else{
					this.logger.debug("keywords is nothing!");
				}
			}else {
				this.logger.debug("generated key is nothing!");
			}
		}
		
		data.put("result", result > 0 ? true : false);
		return data;
	}
	
	public Map<String, Object> delete(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		int result = this.templateDao.delete(params);
		data.put("result", result > 0 ? true : false);
		return data;
	}
	
	public Map<String, Object> favorite(Map<String, Object> params) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		boolean isFavorite = (boolean)params.get("isFavorite");
		int result = 0;
		if(isFavorite == true) {
			result = this.templateDao.insertFavorite(params);
		}else {
			result = this.templateDao.deleteFavorite(params);
		}
		
		data.put("result", result > 0 ? true : false);
		return data;
	}
}
