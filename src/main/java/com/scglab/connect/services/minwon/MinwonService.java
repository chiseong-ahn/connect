package com.scglab.connect.services.minwon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scglab.connect.services.category.CategoryDao;
import com.scglab.connect.services.category.CategoryLarge;
import com.scglab.connect.services.category.CategoryMiddle;
import com.scglab.connect.services.category.CategorySmall;
import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.ErrorService;
import com.scglab.connect.services.company.external.ICompany;
import com.scglab.connect.services.login.LoginService;
import com.scglab.connect.services.member.Member;
import com.scglab.connect.services.room.RoomDao;
import com.scglab.connect.utils.DataUtils;

@Service
public class MinwonService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MinwonDao minwonDao;
	@Autowired
	private LoginService loginService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ErrorService errorService;
	@Autowired
	private RoomDao roomDao;
	@Autowired
	private CategoryDao categoryDao;

	public List<Map<String, Object>> codes(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		// 각 회사별 기간망 클래스 가져오기.
		ICompany company = this.commonService.getCompany(member.getCompanyId());

		List<Map<String, Object>> codes = company.getMinwonsCodes();

		return codes == null ? new ArrayList<>() : codes;
	}

	/**
	 *
	 * @Method Name : regist
	 * @작성일 : 2020. 11. 12.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 민원 등록
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Minwon regist(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Member member = this.loginService.getMember(request);
		params.put("companyId", member.getCompanyId());
		params.put("loginId", member.getId());

		String errorParams = "";
		if (!this.commonService.valid(params, "gasappMemberNumber"))
			errorParams = this.commonService.appendText(errorParams, "가스앱 회원번호-gasappMemberNumber");
		if (!this.commonService.valid(params, "useContractNum"))
			errorParams = this.commonService.appendText(errorParams, "사용계약번호-useContractNum");
		if (!this.commonService.valid(params, "categorySmallId"))
			errorParams = this.commonService.appendText(errorParams, "소분류 카테고리id-categorySmallId");
		if (!this.commonService.valid(params, "minwonCode"))
			errorParams = this.commonService.appendText(errorParams, "민원코드-minwonCode");
		if (!this.commonService.valid(params, "telNumber"))
			errorParams = this.commonService.appendText(errorParams, "휴대폰번호-telNumber");
		if (!this.commonService.valid(params, "chatId"))
			errorParams = this.commonService.appendText(errorParams, "룸에 대한 chatId-chatId");
		if (!this.commonService.valid(params, "roomId"))
			errorParams = this.commonService.appendText(errorParams, "방id-roomId");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		Minwon minwon = null;
		if (this.minwonDao.insertMinwon(params) > 0) {

			// room_history 의 소분류카테고리 업데이트.
			this.roomDao.updateCategoryOfRoomHistory(params);

			// 등록된 민원정보 조회.
			minwon = this.minwonDao.findMinwon(params);

			// 각 회사별 기간망 클래스 가져오기.
			ICompany company = this.commonService.getCompany(member.getCompanyId());

			Map<String, String> obj = new HashMap<String, String>();
			obj.put("customerMobileId", DataUtils.getString(params, "gasappMemberNumber", ""));
			obj.put("useContractNum", DataUtils.getString(params, "useContractNum", ""));
			obj.put("reqName", member.getName());
			obj.put("classCode", DataUtils.getString(params, "minwonCode", ""));
			obj.put("transfer", false + "");
			obj.put("handphone", DataUtils.getString(params, "telNumber", ""));
			obj.put("memo", DataUtils.getString(params, "memo", ""));
			obj.put("employeeId", member.getLoginName());
			obj.put("chatId", Integer.toString(DataUtils.getInt(params, "chatId", 0)));
			this.logger.info("Minwon > " + obj.toString());

			company.minwons(obj);
		}
		return minwon;
	}

	public List<Minwon> findSearchByRoomId(Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String errorParams = "";
		if (!this.commonService.valid(params, "roomId"))
			errorParams = this.commonService.appendText(errorParams, "방id-roomId");

		// 파라미터 유효성 검증.
		if (!errorParams.equals("")) {
			// 필수파라미터 누락에 따른 오류 유발처리.
			this.errorService.throwParameterErrorWithNames(errorParams);
		}

		List<Minwon> minwons = this.minwonDao.findSearchByRoomId(params);
		return minwons == null ? new ArrayList<Minwon>() : minwons;
	}

	// 민원코드 동기화.
	public void syncMinwonCodes() {
		String companyId = "1";
		ICompany company = this.commonService.getCompany(companyId);

		List<Map<String, Object>> list = company.getMinwonsCodes();

		if (list != null && list.size() > 0) {
			List<Map<String, Object>> largeCategories = list.stream()
					.filter(code -> DataUtils.getString(code, "classLevel", "0").equals("1")
							&& DataUtils.getString(code, "useYn", "N").equals("Y"))
					.collect(Collectors.toList());
			List<Map<String, Object>> middleCategories = list.stream()
					.filter(code -> DataUtils.getString(code, "classLevel", "0").equals("2")
							&& DataUtils.getString(code, "useYn", "N").equals("Y"))
					.collect(Collectors.toList());
			List<Map<String, Object>> smallCategories = list.stream()
					.filter(code -> DataUtils.getString(code, "classLevel", "0").equals("3")
							&& DataUtils.getString(code, "useYn", "N").equals("Y"))
					.collect(Collectors.toList());

			for (Map<String, Object> obj : largeCategories) {

				// this.logger.debug("obj : " + obj);
				String name = DataUtils.getString(obj, "name", "");
				String code = DataUtils.getString(obj, "code", "");

				obj.put("companyId", companyId);
				CategoryLarge category = this.categoryDao.findCategoryLargeByMinwonCode(obj);

				// 코드가 존재하지 않을 경우.
				obj.put("minwonCode", code);
				obj.put("minwonName", name);
				obj.put("loginId", null);

				if (category == null) {
					int lastSortIndex = this.categoryDao.getLastLargeSortIndex(obj);
					obj.put("sortIndex", lastSortIndex);
					
					this.logger.info("[대분류] 대분류 카테고리 생성 : " + obj);
					this.categoryDao.createCategoryLarge(obj);

				} else {
					if (!category.getMinwonName().equals(name)) {
						// 코드명이 다를경우.
						obj.put("id", category.getId());
						
						this.logger.info("[대분류] 대분류 카테고리 수정 : " + obj);
						this.categoryDao.updateCategoryLarge(obj);
					}
				}
			}

			for (Map<String, Object> obj : middleCategories) {
				this.logger.debug("obj : " + obj);
				String name = DataUtils.getString(obj, "name", "");
				String code = DataUtils.getString(obj, "code", "");
				String largeClassCode = DataUtils.getString(obj, "largeClassCode", "");

				obj.put("companyId", companyId);
				CategoryMiddle category = this.categoryDao.findCategoryMiddleByMinwonCode(obj);

				obj.put("minwonCode", code);
				obj.put("minwonName", name);
				obj.put("loginId", null);

				if (category == null) {
					// 코드가 존재하지 않을경우.
					obj.put("companyId", companyId);
					obj.put("code", largeClassCode);
					CategoryLarge categoryLarge = this.categoryDao.findCategoryLargeByMinwonCode(obj);
					if(categoryLarge != null) {
						obj.put("categoryLargeId", categoryLarge.getId());

						int lastSortIndex = this.categoryDao.getLastMiddleSortIndex(obj);
						obj.put("sortIndex", lastSortIndex);
						obj.put("code", code);
						
						this.logger.info("[중분류] 중분류 카테고리 생성 : " + obj);
						this.categoryDao.createCategoryMiddle(obj);
					}else {
						this.logger.info("[중분류] 중분류가 존재하지 않음 : " + obj);
					}
				} else {
					if (!category.getMinwonName().equals(name)) {
						// 코드명이 다를경우.
						obj.put("id", category.getId());
						
						this.logger.info("[중분류] 중분류 카테고리 수정 : " + obj);
						this.categoryDao.updateCategoryMiddle(obj);
					}
				}
			}

			for (Map<String, Object> obj : smallCategories) {
				// this.logger.debug("obj : " + obj);
				String name = DataUtils.getString(obj, "name", "");
				String code = DataUtils.getString(obj, "code", "");
				String middleClassCode = DataUtils.getString(obj, "middleClassCode", "");

				obj.put("companyId", companyId);
				CategorySmall category = this.categoryDao.findCategorySmallByMinwonCode(obj);

				obj.put("minwonCode", code);
				obj.put("minwonName", name);
				obj.put("loginId", null);

				
				if (category == null) {
					// 코드가 존재하지 않을경우.
					obj.put("companyId", companyId);
					obj.put("code", middleClassCode);
					CategoryMiddle categoryMiddle = this.categoryDao.findCategoryMiddleByMinwonCode(obj);
					if(categoryMiddle != null) {
						obj.put("categoryMiddleId", categoryMiddle.getId());

						int lastSortIndex = this.categoryDao.getLastSmallSortIndex(obj);
						obj.put("sortIndex", lastSortIndex);
						obj.put("code", code);
						
						this.logger.info("[소분류] 소분류 카테고리 생성 : " + obj);
						this.categoryDao.createCategorySmall(obj);
					}else {
						this.logger.info("[소분류] 중분류가 존재하지 않음 : " + obj);
					}
				} else {
					if (!category.getMinwonName().equals(name)) {
						// 코드명이 다를경우.
						obj.put("id", category.getId());
						
						this.logger.info("[소분류] 소분류 카테고리 수정 : " + obj);
						this.categoryDao.updateCategorySmall(obj);
					}
				}
			}
		}
	}
}