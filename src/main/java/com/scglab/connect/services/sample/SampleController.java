package com.scglab.connect.services.sample;

import java.util.Map;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/samples")
@Tag(name = "CRUD 예제", description = "CRUD 작성에 대한 예제입니다.")
public class SampleController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	SampleService sampleService;
	
	@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="목록 조회", description = "조건에 맞는 게시물 목록을 조회합니다.")
	public Map<String, Object> list(@Parameter(hidden = true) @RequestParam Map<String, Object> params) throws Exception {
		this.logger.debug("DEBUG LOGGER TEST");
		return this.sampleService.selectAll(params);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="상세정보 조회", description = "게시물의 상세정보를 조회합니다.")
	public Map<String, Object> object(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(name = "게시물 번호", description = "조회할 게시물의 관리번호", required = true, example = "10") @PathVariable String id) throws Exception {
		return this.sampleService.selectOne(params, id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="게시물 생성(등록)", description = "게시물을 등록(생성)합니다.")
	@Parameters({
    	@Parameter(name = "name", description = "이름", required = true, in = ParameterIn.QUERY, example = "홍길동(2자~4자)"),
    })
	public Map<String, Object> save(@Parameter(hidden = true) @RequestParam Map<String, Object> params) throws Exception {
		return this.sampleService.insert(params);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="게시물 정보 변경(수정)", description = "게시물 정보를 변경(수정)합니다.")
	@Parameters({
    	@Parameter(name = "id", description = "아이디", required = true, in = ParameterIn.QUERY, example = "1"),
    	@Parameter(name = "name", description = "이름", required = true, in = ParameterIn.QUERY, example = "홍길동(2~4자)")
    })
	public Map<String, Object> update(@Parameter(hidden = true) @RequestParam Map<String, Object> params) throws Exception {
		return this.sampleService.update(params);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="게시물 삭제", description = "게시물을 삭제합니다.")
	@Parameters({
    	@Parameter(name = "id", description = "아이디", required = true, in = ParameterIn.QUERY, example = "1"),
    })
	public Map<String, Object> delete(@Parameter(hidden = true) @RequestParam Map<String, Object> params) throws Exception {
		return this.sampleService.delete(params);
	}
	
}


