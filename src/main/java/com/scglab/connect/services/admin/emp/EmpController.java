package com.scglab.connect.services.admin.emp;

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

import com.scglab.connect.base.annotatios.Auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/admin/emp")
@Api(tags = "관리자메뉴 > 계정관리 API")
public class EmpController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	EmpService empService;
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/{cid}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "계정 목록 조회", notes = "모든 계정을 조회한다.")
	public Map<String, Object> list(@ApiParam(hidden=true) @RequestParam Map<String, Object> params, @ApiParam(value="기관코드", required=true, defaultValue = "1") @PathVariable String cid) throws Exception {
		params.put("cid", cid);
		return this.empService.list(params);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.GET, value = "/{cid}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "계정 상세 조회", notes = "계정의 상세내용을 조회한다.")
	public Map<String, Object> object(@ApiParam(hidden=true) @RequestParam Map<String, Object> params, @ApiParam(value="기관코드", required=true, defaultValue = "1") @PathVariable String cid, @ApiParam(value="계정번호", required=true) @PathVariable String id) throws Exception {
		return this.empService.object(params, id);
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/{cid}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "계정 등록", notes = "계정을 등록한다.")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "speaker", value = "상담사번호", required = true, dataType = "int"),
		@ApiImplicitParam(name = "auth", value = "권한", required = true, dataType = "int"),
		@ApiImplicitParam(name = "profileimg", value = "프로필 이미지 업로드 번호", required = true, dataType = "int"),
		@ApiImplicitParam(name = "empno", value = "아이디", required = true, dataType = "string"),
		@ApiImplicitParam(name = "state", value = "상담상태", required = true, dataType = "int"),
	})
	public Map<String, Object> save(@ApiParam(hidden = true) @RequestParam Map<String, Object> params, @ApiParam(value="기관코드", required=true, defaultValue = "1") @PathVariable int cid) throws Exception {
//		this.logger.debug("emp : " + emp.toString());
//		return this.empService.save(emp);
		
		return null;
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.PUT, value = "/{cid}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "speaker", value = "상담사번호", required = true, dataType = "int", defaultValue = "66", paramType = "json"),
		@ApiImplicitParam(name = "auth", value = "권한", required = true, dataType = "int", defaultValue = "9", paramType = "json"),
		@ApiImplicitParam(name = "profileimg", value = "프로필 이미지 업로드 번호", required = false, dataType = "int", paramType = "json"),
		@ApiImplicitParam(name = "empno", value = "아이디", required = true, dataType = "string", defaultValue = "csahn", paramType = "json"),
		@ApiImplicitParam(name = "state", value = "상담상태", required = true, dataType = "int", defaultValue = "9", paramType = "json"),
		@ApiImplicitParam(name = "id", value = "계정번호", required = true, dataType = "int", defaultValue = "67", paramType = "json")
	})
	public  Map<String, Object> update(@ApiParam(hidden = true) @RequestParam Map<String, Object> params, @ApiParam(value="기관코드", required=true, defaultValue = "1") @PathVariable int cid, HttpServletRequest request) throws Exception {
		//String auth = DataUtils.getObjectValue(params, "auth", "");
		
//		this.logger.debug("auth : " + auth);
		//this.logger.debug("emp : " + emp.toString());
		//return this.empService.update(emp);
		return null;
	}
	
	@Auth
	@RequestMapping(method = RequestMethod.DELETE, value = "/{cid}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "계정 삭제", notes = "계정을 삭제한다.")
	@ApiImplicitParam(value="계정번호", defaultValue = "67", dataType = "int", paramType = "form")
	public Map<String, Object> delete(@ApiParam(hidden=true) @RequestParam Map<String, Object> params, @ApiParam(value="기관코드", required=true, defaultValue = "1") @PathVariable String cid) throws Exception {
		return this.empService.delete(params);
	}
}


