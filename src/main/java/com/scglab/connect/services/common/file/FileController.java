package com.scglab.connect.services.common.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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

@RestController
@RequestMapping(name = "파일관리", value="/api/file")
public class FileController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private PathProperties pathProperty;
	@Autowired private FileService fileService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/download")
	public ResponseEntity<Resource> download(@RequestParam Map<String, Object> params) throws IOException {
		
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
	
	@RequestMapping(name = "파일 업로드", method = RequestMethod.POST, value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
	public FileDto upload(@RequestParam("file") MultipartFile file, @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.fileService.uploadFile(file, params, request);	
	}
	
	
	@RequestMapping(name = "파일 업로드 및 썸네일 생성.", method = RequestMethod.POST, value = "/uploadWithThumbnail", produces = MediaType.APPLICATION_JSON_VALUE)
	public FileDto uploadWithThumbnail(@RequestParam("file") MultipartFile file, @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		return this.fileService.uploadFileWithThumbnail(file, params, request);	
	}
	
	
	@RequestMapping(name = "파일 삭제처리", method = RequestMethod.DELETE, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> delete(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
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
