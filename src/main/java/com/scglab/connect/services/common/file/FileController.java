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
	
	@Autowired
	private PathProperties pathProperty;
	
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
	
	@RequestMapping(method = RequestMethod.POST, value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "파일 등록처리", description = "파일을 등록하고 등록정보를 리턴합니다.")
	@Parameters({
		@Parameter(name="div", description = "서비스명(저장되는 경로를 구분하는 값)", in = ParameterIn.QUERY, required = false, example = "service"),
		@Parameter(name="file", description = "파일업로드명", in = ParameterIn.QUERY ,required = true, example = ""),
	})
	public FileDto upload(@Parameter(hidden = true) @RequestParam("file") MultipartFile file, @Parameter(hidden = true) @RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
		String div = DataUtils.getString(params, "div", "");
		//Map<String, Object> obj = new HashMap<String, Object>();
		FileDto fileDto = new FileDto();
		
		try {
			
			// 원본 파일명.
			String originFileName = file.getOriginalFilename();
			String ext = originFileName.substring(originFileName.lastIndexOf(".") + 1);
			long fileSize = file.getSize();
			
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			
			// 저장할 파일명.
			// String saveFileName = md5Generator(originFileName + System.currentTimeMillis()).toString() + "." + ext;
			String saveFileName = UUID.randomUUID() + "." + ext;
			
			// 실제 저장할 파일경로.
			String savePath = !div.equals("") ? this.pathProperty.getUpload() + "/" + div : this.pathProperty.getUpload();
			savePath += "/" + year + "/" + month;
			this.logger.debug("savePath : " + savePath);
			
			// 저장할 디렉토리가 존재하지 않으면 생성.
			if (!new File(savePath).exists()) {
				try {
					new File(savePath).mkdirs();
				} catch (Exception e) {
					//e.getStackTrace();
					throw new Exception(e);
				}
			}
			String filePath = savePath + "/" + saveFileName;
			
			// 최종파일 생성.
			file.transferTo(new File(filePath));
			
			// 업로드 된 파일 정보.
//			fileDto.setOrignFilename(originFileName);
			fileDto.setSaveFilename(saveFileName);
			fileDto.setSavePath(savePath);
			fileDto.setFileSize(fileSize);
			
			this.logger.debug("file : " + fileDto.toString());
			
		} catch (Exception e) {
			//e.printStackTrace();
			throw new Exception(e);
		}
		return fileDto;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
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
