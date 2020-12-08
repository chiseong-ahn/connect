package com.scglab.connect.services.common.file;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.scglab.connect.properties.PathProperties;
import com.scglab.connect.utils.DataUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/file")
@Tag(name = "파일", description = "파일 업/다운로드 API")
public class FileController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private PathProperties pathProperty;
	@Autowired private FileService fileService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/download")
	@Operation(summary = "파일 다운로드", description = "파일을 다운로드 한다.")
	public ResponseEntity<Resource> download(@Parameter(hidden = true) @RequestParam Map<String, Object> params) throws IOException {
		
		String filename = DataUtils.getString(params, "fileName", "test.jpg");
		
		// 파일 경로
		Path path = Paths.get(this.pathProperty.getUpload() + "/" + filename);
		String contentType = Files.probeContentType(path);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName().toString());
		headers.add(HttpHeaders.CONTENT_TYPE, contentType);

		Resource resource = new InputStreamResource(Files.newInputStream(path));
		
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "파일 업로드", description = "파일을 업로드하고 업로드된 정보를 반환합니다.")
	@Parameters({
		@Parameter(name="div", description = "서비스명(저장되는 경로를 구분하는 값)", in = ParameterIn.QUERY, required = false, example = "service"),
		@Parameter(name="file", description = "파일업로드명", in = ParameterIn.QUERY ,required = true, example = ""),
	})
	public FileDto upload(@Parameter(hidden = true) @RequestParam("file") MultipartFile file, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.fileService.uploadFile(file, params, request);	
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "파일 삭제처리", description = "파일 삭제")
	@Parameters({
		@Parameter(name="fileFullname", description = "파일명(경로 포함)", in = ParameterIn.QUERY ,required = true, example = ""),
	})
	public Map<String, Object> delete(@Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		
		String fileFullname = DataUtils.getString(params, "fileFullname", "");
		if(!fileFullname.equals("")) {
			File file = new File(fileFullname);
			if(file.exists()) {
				if(file.delete()) {
					result.put("success", true);
				}
			}
		}
		return result;
	}
	
	

}
