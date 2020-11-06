package com.scglab.connect.services.common.file;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

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

import com.scglab.connect.base.annotations.Auth;
import com.scglab.connect.properties.PathProperties;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/file")
@Tag(name = "파일관리", description = "파일 업로드 및 다운로드")
public class FileController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PathProperties pathProperty;
	
	@RequestMapping(method = RequestMethod.GET, value = "/download")
	@Operation(summary = "파일 다운로드", description = "파일을 다운로드 한다.")
	public ResponseEntity<Resource> download() throws IOException {
		
		// 파일 경로
		Path path = Paths.get("/Users/anchiseong/Downloads/test1.jpg");
		String contentType = Files.probeContentType(path);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName().toString());
		headers.add(HttpHeaders.CONTENT_TYPE, contentType);

		Resource resource = new InputStreamResource(Files.newInputStream(path));
		
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}
	
	
	@Auth
	@RequestMapping(method = RequestMethod.POST, value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "파일 등록처리", description = "파일을 등록하고 등록정보를 리턴합니다.")
	public Map<String, Object> upload(@Parameter(hidden = true) @RequestParam("file") MultipartFile file) throws Exception {
		
		Map<String, Object> obj = new HashMap<String, Object>();
		
		try {
			
			// 원본 파일명.
			String originFileName = file.getOriginalFilename();
			
			// 저장할 파일명.
			String saveFileName = md5Generator(originFileName).toString();
			
			// 실제 저장할 파일경로.
			String savePath = pathProperty.getUpload();
			
			// 저장할 디렉토리가 존재하지 않으면 생성.
			if (!new File(savePath).exists()) {
				try {
					new File(savePath).mkdir();
				} catch (Exception e) {
					//e.getStackTrace();
					throw new Exception(e);
				}
			}
			
			String filePath = savePath + "\\" + saveFileName;
			
			// 최종파일 생성.
			file.transferTo(new File(filePath));
			
			// 업로드 된 파일 정보.
			obj.put("originFileName", originFileName);
			obj.put("saveFileName", saveFileName);
			obj.put("saveFilePath", filePath);
			
			this.logger.debug("file : " + obj.toString());
			
		} catch (Exception e) {
			//e.printStackTrace();
			throw new Exception(e);
		}
		return obj;
	}
	
	private String md5Generator(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest mdMD5 = MessageDigest.getInstance("MD5");
        mdMD5.update(input.getBytes("UTF-8"));
        byte[] md5Hash = mdMD5.digest();
        StringBuilder hexMD5hash = new StringBuilder();
        for(byte b : md5Hash) {
            String hexString = String.format("%02x", b);
            hexMD5hash.append(hexString);
        }
        return hexMD5hash.toString();
    }

}
