package com.scglab.connect.services.review;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scglab.connect.base.exception.UnauthorizedException;
import com.scglab.connect.constant.Constant;
import com.scglab.connect.services.common.service.JwtService;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.utils.DataUtils;
import com.scglab.connect.utils.ExcelUtils;

@Service
public class ReviewService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private LoginService loginService;
	@Autowired
	private JwtService jwtService;
	

	public ResponseEntity<Map<String, Object>> findAll(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		// 정렬 기준
		String sort = DataUtils.getString(params, "sort", "ASC");
		params.put("sort", sort);

		// 페이지 번호.
		int page = Integer.parseInt(DataUtils.getObjectValue(params, "page", "1"));
		page = page < 1 ? 1 : page;

		// 페이지당 노출할 게시물 수.
		int pageSize = Integer.parseInt(DataUtils.getObjectValue(params, "pageSize", "10"));
		pageSize = pageSize < 1 ? 10 : pageSize;

		// 조회 시작 번호.
		int startNum = (page - 1) * pageSize;

		params.put("startNum", startNum);
		params.put("pageSize", pageSize);
		
		List<Review> list = null;
		int count = this.reviewDao.findCount(params);
		this.logger.debug("review count : " + count);
		
		if(count > 0) {
			list = this.reviewDao.findAll(params);
			this.logger.debug("review list : " + list.toString());
		}
		
		data.put("count", count);
		data.put("list", list);
		
		return new ResponseEntity<>(data, null, HttpStatus.OK);
	}
	
	
	public void findAllWithDown(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String accessToken = DataUtils.getString(params, "accessToken", "");
		
		if(accessToken.equals("")) {
			// 인증되지 않은경우.
			throw new UnauthorizedException("auth.valid.fail.reason1");
			
		}else {
			if(!this.jwtService.validateToken(accessToken)) {
				throw new UnauthorizedException("auth.valid.fail.reason2");
				
			}else {
				// 토큰이 유효한 경우.
				
				Map<String, Object> claims = this.jwtService.getJwtData(accessToken);
				if(claims == null) {
					throw new RuntimeException("claims is null");
				}
				this.logger.debug("claims : " + claims.toString());
				request.setAttribute("accessToken", accessToken);
				
				Member profile = new Member();
				profile = (Member) DataUtils.convertMapToObject(claims, profile);
				
				// token에서 추출한 Profile 정보를 request객체에 저장.
				request.setAttribute(Constant.AUTH_MEMBER, profile);
			}
		}
		
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());
		
		// 정렬 기준
		String sort = DataUtils.getString(params, "sort", "ASC");
		params.put("sort", sort);

		List<Review> list = null;
		int count = this.reviewDao.findCount(params);
		this.logger.debug("review count : " + count);
		
		if(count > 0) {
			list = this.reviewDao.findAll(params);
			this.logger.debug("review list : " + list.toString());
			
			String fileName = "고객만족도현황";
			String[] titles = {"고객명", "채팅상담일시", "상담원", "평점"};
			String[] keys = {"customerName", "createDate", "memberName", "reviewScore"};
			
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for(Review review : list) {
				ObjectMapper objectMapper = new ObjectMapper();
				Map<String, Object> data = objectMapper.convertValue(review, Map.class);
				datas.add(data);
			}
			
			ExcelUtils excelUtils = new ExcelUtils();
			excelUtils.create(fileName, titles, keys, datas, response);
		}
		
		
	}

}