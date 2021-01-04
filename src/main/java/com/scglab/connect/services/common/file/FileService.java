package com.scglab.connect.services.common.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scglab.connect.constant.Constant;
import com.scglab.connect.properties.DomainProperties;
import com.scglab.connect.properties.PathProperties;
import com.scglab.connect.services.common.CommonService;
import com.scglab.connect.services.common.service.ErrorService;
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.utils.DataUtils;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class FileService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private PathProperties pathProperty;
	@Autowired private DomainProperties domainProperty;
	@Autowired private MessageHandler messageHandler;
	@Autowired private CommonService commonService;
	@Autowired private ErrorService errorService;
	
	public FileDto uploadFile(MultipartFile file, Map<String, Object> params, HttpServletRequest request) throws Exception {
		String div = DataUtils.getString(params, "div", "common");
		FileDto fileDto = new FileDto();
		
		try {
			
			if(!file.isEmpty()) {
			
				// 원본 파일명.
				String originFileName = file.getOriginalFilename();
				String ext = originFileName.substring(originFileName.lastIndexOf(".") + 1).toLowerCase();
				
				if(Constant.UPLOAD_IMAGE_FORMAT.indexOf(ext) == -1) {
					// 허용하지 않는 파일형식일 경우.
					throw new RuntimeException(this.messageHandler.getMessage("error.upload.reason2")); 
				}
				
				long fileSize = file.getSize();
				
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH) + 1;
				
				// 저장할 파일명.
				// String saveFileName = md5Generator(originFileName + System.currentTimeMillis()).toString() + "." + ext;
				String saveFileName = UUID.randomUUID() + "." + ext;
				
				// 실제 저장할 파일경로.
				String depth1 = !div.equals("") ? this.pathProperty.getUpload() + "/" + div : this.pathProperty.getUpload();
				String depth2 = depth1 + "/" + year;
				String depth3 = depth2 + "/" + month;
				
				String savePath = depth3;
				this.logger.debug("savePath : " + savePath);
				
				// 저장할 디렉토리가 존재하지 않으면 생성.
				File saveDirectory = new File(savePath);
				
				if (!saveDirectory.exists()) {
					try {
						saveDirectory.mkdirs();
						
					} catch (Exception e) {
						//e.getStackTrace();
						throw new Exception(e);
					}
				}
				String filePath = savePath + "/" + saveFileName;
				
				// 최종파일 생성.
				File saveFile = new File(filePath);
				file.transferTo(saveFile);
				
				// 디렉토리 및 파일에 대한 권한 허용.(읽기, 쓰기, 실행)
				grantFile(depth1, true, true, true);
				grantFile(depth2, true, true, true);
				grantFile(depth3, true, true, true);
				grantFile(filePath, true, false, false);
				
				// 업로드 된 파일 정보.
				fileDto.setOriginFileName(originFileName);
				fileDto.setFileName(saveFileName);
				fileDto.setSavePath(savePath.replace(this.pathProperty.getUpload(), ""));
				fileDto.setFileSize(fileSize);
				
				this.logger.debug("file : " + fileDto.toString());
			}else {
				throw new RuntimeException("error.upload.empty");
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
			throw new RuntimeException("error.upload.reason1");
		}
		return fileDto;
	}
	
	public FileDto uploadFileWithThumbnail(MultipartFile file, Map<String, Object> params, HttpServletRequest request) throws Exception {
		String div = DataUtils.getString(params, "div", "common");
		FileDto fileDto = new FileDto();
		
		try {
			
			if(!file.isEmpty()) {
			
				// 원본 파일명.
				String originFileName = file.getOriginalFilename();
				String ext = originFileName.substring(originFileName.lastIndexOf(".") + 1).toLowerCase();
				
				if(Constant.UPLOAD_IMAGE_FORMAT.indexOf(ext) == -1) {
					// 허용하지 않는 파일형식일 경우.
					throw new RuntimeException(this.messageHandler.getMessage("error.upload.reason2")); 
				}
				
				long fileSize = file.getSize();
				
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH) + 1;
				
				// 저장할 파일명.
				// String saveFileName = md5Generator(originFileName + System.currentTimeMillis()).toString() + "." + ext;
				String saveFileName = UUID.randomUUID() + "." + ext;
				
				// 실제 저장할 파일경로.
				String depth1 = !div.equals("") ? this.pathProperty.getUpload() + "/" + div : this.pathProperty.getUpload();
				String depth2 = depth1 + "/" + year;
				String depth3 = depth2 + "/" + month;
				
				String savePath = depth3;
				this.logger.debug("savePath : " + savePath);
				
				// 저장할 디렉토리가 존재하지 않으면 생성.
				File saveDirectory = new File(savePath);
				
				if (!saveDirectory.exists()) {
					try {
						saveDirectory.mkdirs();
						
					} catch (Exception e) {
						//e.getStackTrace();
						throw new Exception(e);
					}
				}
				
				String filePath = savePath + "/" + saveFileName;
				
				// 최종파일 생성.
				File saveFile = new File(filePath);
				file.transferTo(saveFile);
				
				// 디렉토리 및 파일에 대한 권한 허용.(읽기, 쓰기, 실행)
				grantFile(depth1, true, true, true);
				grantFile(depth2, true, true, true);
				grantFile(depth3, true, true, true);
				grantFile(filePath, true, false, false);
				
				// 업로드 된 파일 정보.
				fileDto.setOriginFileName(originFileName);
				fileDto.setFileName(saveFileName);
				fileDto.setSavePath(savePath.replace(this.pathProperty.getUpload(), ""));
				fileDto.setFileSize(fileSize);
				fileDto.setFileUrl("//" + this.domainProperty.getCstalk() + fileDto.getSavePath() + saveFileName);
				
				BufferedImage bi = ImageIO.read(saveFile);
				int width = bi.getWidth();
				int height = bi.getHeight();
				
				fileDto.setWidth(width);
				fileDto.setHeight(height);
				
				this.logger.debug("width : " + width);
				this.logger.debug("height : " + height);
				
				// 썸네일 이미지 생성 준비.
				int thumbWidth = 0;
				int thumbHeight = 0;
				
				// 썸네일 너비사이즈 제한에 따른 비율조정. 
				if(width > Constant.THUMBNAIL_WIDTH_SIZE) {
					thumbWidth = Constant.THUMBNAIL_WIDTH_SIZE;
					thumbHeight = (int)((Constant.THUMBNAIL_WIDTH_SIZE / (double)width) * height);
				}else {
					thumbWidth = width;
					thumbHeight = height;
				}
				
				String thumbFileName = "thumb_" + saveFileName;
				String thumbSavePath = savePath;
				
				// 썸네일 파일.
				File thumbFile = new File(thumbSavePath + "/" + thumbFileName);
				
				// 썸네일 생성.
				Thumbnails
					.of(new File(filePath))
		        	.size(thumbWidth, thumbHeight)
		        	.toFile(thumbFile);
				
				// 생성된 썸네일 파일의 읽기권한 허용.
				thumbFile.setReadable(true, false);
				
				long thumbFileSize = thumbFile.length();
				
				fileDto.setThumbFileName(thumbFileName);
				fileDto.setThumbSavePath(thumbSavePath.replace(this.pathProperty.getUpload(), ""));
				fileDto.setThumbFileSize(thumbFileSize);
				fileDto.setThumbWidth(thumbWidth);
				fileDto.setThumbHeight(thumbHeight);
				
				fileDto.setThumbFileUrl("//" + this.domainProperty.getCstalk() + fileDto.getThumbSavePath() + saveFileName);
				
				this.logger.debug("file : " + fileDto.toString());
			}else {
				
				throw new RuntimeException("error.upload.empty");
			}
		} catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("error.upload.reason1");
		}
		return fileDto;
	}
	
	private void grantFile(String filePath, boolean readable, boolean writable, boolean executable) {
		File file = new File(filePath);
		this.logger.info("filePath : " + filePath + "[read : " + readable + ", write : " + writable + ", execute : " + executable);
		
		if(executable)
			file.setExecutable(true, false);
		
		if(readable)
			file.setReadable(true, false);
		
		if(writable)
			file.setWritable(true, false);
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
