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
	@Autowired private MessageHandler messageHandler;
	@Autowired private CommonService commonService;
	@Autowired private ErrorService errorService;
	
	public FileDto uploadFile(MultipartFile file, Map<String, Object> params, HttpServletRequest request) throws Exception {
		String div = DataUtils.getString(params, "div", "");
		FileDto fileDto = new FileDto();
		
		try {
			
			if(!file.isEmpty()) {
			
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
				File saveFile = new File(filePath);
				file.transferTo(saveFile);
				
				// 업로드 된 파일 정보.
				fileDto.setOriginFileName(originFileName);
				fileDto.setFileName(saveFileName);
				fileDto.setSavePath(savePath);
				fileDto.setFileSize(fileSize);
				
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
				
				long thumbFileSize = thumbFile.length();
				
				fileDto.setThumbFileName(thumbFileName);
				fileDto.setThumbSavePath(thumbSavePath);
				fileDto.setThumbFileSize(thumbFileSize);
				fileDto.setThumbWidth(thumbWidth);
				fileDto.setThumbHeight(thumbHeight);
				
				this.logger.debug("file : " + fileDto.toString());
			}else {
				
				throw new RuntimeException("error.upload.empty");
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
			throw new RuntimeException("error.upload.type1");
		}
		return fileDto;
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
