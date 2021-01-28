package com.scglab.connect.services.common.file;

import java.awt.image.BufferedImage;
import java.io.File;
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
import com.scglab.connect.services.common.service.MessageHandler;
import com.scglab.connect.utils.DataUtils;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class FileService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PathProperties pathProperty;
	@Autowired
	private DomainProperties domainProperty;
	@Autowired
	private MessageHandler messageHandler;

	/**
	 * 
	 * @Method Name : uploadFile
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 파일업로드
	 * @param file
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public FileDto uploadFile(MultipartFile file, Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		return uploadFile(file, params, request, true);
	}

	/**
	 * 
	 * @Method Name : uploadFile
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 파일업로드
	 * @param file
	 * @param params
	 * @param request
	 * @param autoDivision
	 * @return
	 * @throws Exception
	 */
	public FileDto uploadFile(MultipartFile file, Map<String, Object> params, HttpServletRequest request,
			boolean autoDivision) throws Exception {
		String div = DataUtils.getString(params, "div", "common");
		FileDto fileDto = new FileDto();

		try {

			if (!file.isEmpty()) {

				// 원본 파일명.
				String originFileName = file.getOriginalFilename();
				String ext = originFileName.substring(originFileName.lastIndexOf(".") + 1).toLowerCase();

				if (Constant.UPLOAD_IMAGE_FORMAT.indexOf(ext) == -1) {
					// 허용하지 않는 파일형식일 경우.
					throw new RuntimeException(this.messageHandler.getMessage("error.upload.reason2"));
				}

				long fileSize = file.getSize();

				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH) + 1;

				// 저장할 파일명.
				String saveFileName = UUID.randomUUID() + "." + ext;

				// 실제 저장할 파일경로.
				String depth1 = "", depth2 = "", depth3 = "", savePath = "";

				// 연월 디렉토리 자동생성 여부.
				if (autoDivision) {
					depth1 = this.pathProperty.getUpload() + "/" + div;
					depth2 = depth1 + "/" + year;
					depth3 = depth2 + "/" + month;
					savePath = depth3;
				} else {
					savePath = this.pathProperty.getUpload() + "/" + div;
				}

				this.logger.debug("savePath : " + savePath);

				// 저장할 디렉토리가 존재하지 않으면 생성.
				File saveDirectory = new File(savePath);

				if (!saveDirectory.exists()) {
					try {
						saveDirectory.mkdirs();

					} catch (Exception e) {
						// e.getStackTrace();
						throw new Exception(e);
					}
				}
				String filePath = savePath + "/" + saveFileName;

				// 최종파일 생성.
				File saveFile = new File(filePath);
				file.transferTo(saveFile);

				if (autoDivision) {
					// 디렉토리 및 파일에 대한 권한 허용.(읽기, 쓰기, 실행)
					grantFile(depth1, true, true, true);
					grantFile(depth2, true, true, true);
					grantFile(depth3, true, true, true);
					grantFile(filePath, true, false, false);
				}

				// 업로드 된 파일 정보.
				fileDto.setOriginFileName(originFileName);
				fileDto.setFileSize(fileSize);
				fileDto.setFileUrl("https://" + this.domainProperty.getCstalk() + "/attach"
						+ savePath.replace(this.pathProperty.getUpload(), "") + "/" + saveFileName);

				BufferedImage bi = ImageIO.read(saveFile);
				int width = bi.getWidth();
				int height = bi.getHeight();

				fileDto.setWidth(width);
				fileDto.setHeight(height);
			} else {
				throw new RuntimeException("error.upload.empty");
			}

		} catch (Exception e) {
			// e.printStackTrace();
			throw new RuntimeException("error.upload.reason1");
		}
		return fileDto;
	}

	/**
	 * 
	 * @Method Name : uploadFileWithThumbnail
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 파일업로드 및 썸네일 생성.
	 * @param file
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public FileDto uploadFileWithThumbnail(MultipartFile file, Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		return uploadFileWithThumbnail(file, params, request, true);
	}

	/**
	 * 
	 * @Method Name : uploadFileWithThumbnail
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 파일업로드 및 썸네일 생성.
	 * @param file
	 * @param params
	 * @param request
	 * @param autoDivision
	 * @return
	 * @throws Exception
	 */
	public FileDto uploadFileWithThumbnail(MultipartFile file, Map<String, Object> params, HttpServletRequest request,
			boolean autoDivision) throws Exception {
		String div = DataUtils.getString(params, "div", "common");
		FileDto fileDto = new FileDto();

		try {

			if (!file.isEmpty()) {

				// 원본 파일명.
				String originFileName = file.getOriginalFilename();
				String ext = originFileName.substring(originFileName.lastIndexOf(".") + 1).toLowerCase();

				if (Constant.UPLOAD_IMAGE_FORMAT.indexOf(ext) == -1) {
					// 허용하지 않는 파일형식일 경우.
					throw new RuntimeException(this.messageHandler.getMessage("error.upload.reason2"));
				}

				long fileSize = file.getSize();

				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH) + 1;

				// 저장할 파일명.
				String saveFileName = UUID.randomUUID() + "." + ext;

				// 실제 저장할 파일경로.
				String depth1 = "", depth2 = "", depth3 = "", savePath = "";

				// 연월 디렉토리 자동생성 여부.
				if (autoDivision) {
					depth1 = this.pathProperty.getUpload() + "/" + div;
					depth2 = depth1 + "/" + year;
					depth3 = depth2 + "/" + month;
					savePath = depth3;
				} else {
					savePath = this.pathProperty.getUpload() + "/" + div;
				}

				this.logger.debug("savePath : " + savePath);

				// 저장할 디렉토리가 존재하지 않으면 생성.
				File saveDirectory = new File(savePath);

				if (!saveDirectory.exists()) {
					try {
						saveDirectory.mkdirs();

					} catch (Exception e) {
						throw new Exception(e);
					}
				}

				String filePath = savePath + "/" + saveFileName;

				// 최종파일 생성.
				File saveFile = new File(filePath);
				file.transferTo(saveFile);

				this.logger.debug("div : " + div);
				if (div.indexOf("/") > -1) {
					String[] divArray = div.split("/");
					String dir = this.pathProperty.getUpload();
					for (int i = 0; i < divArray.length; i++) {
						dir += ("/" + divArray[i]);
						this.logger.debug("dir : " + dir);
						grantFile(dir, true, true, true);
					}
				}

				// 연월 디렉토리 자동생성 여부에 따라 디렉토리 권한처리.
				if (autoDivision) {
					// 디렉토리 및 파일에 대한 권한 허용.(읽기, 쓰기, 실행)
					grantFile(depth1, true, true, true);
					grantFile(depth2, true, true, true);
					grantFile(depth3, true, true, true);
				}
				grantFile(filePath, true, false, false);

				// 업로드 된 파일 정보.
				fileDto.setOriginFileName(originFileName);
				fileDto.setFileSize(fileSize);
				fileDto.setFileUrl("https://" + this.domainProperty.getCstalk() + "/attach"
						+ savePath.replace(this.pathProperty.getUpload(), "") + "/" + saveFileName);

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
				if (width > Constant.THUMBNAIL_WIDTH_SIZE) {
					thumbWidth = Constant.THUMBNAIL_WIDTH_SIZE;
					thumbHeight = (int) ((Constant.THUMBNAIL_WIDTH_SIZE / (double) width) * height);
				} else {
					thumbWidth = width;
					thumbHeight = height;
				}

				String thumbFileName = "thumb_" + saveFileName;
				String thumbSavePath = savePath;

				// 썸네일 파일.
				File thumbFile = new File(thumbSavePath + "/" + thumbFileName);

				// 썸네일 생성.
				Thumbnails.of(new File(filePath)).size(thumbWidth, thumbHeight).toFile(thumbFile);

				// 생성된 썸네일 파일의 읽기권한 허용.
				thumbFile.setReadable(true, false);

				long thumbFileSize = thumbFile.length();

				fileDto.setThumbFileSize(thumbFileSize);
				fileDto.setThumbWidth(thumbWidth);
				fileDto.setThumbHeight(thumbHeight);
				fileDto.setThumbFileUrl("https://" + this.domainProperty.getCstalk() + "/attach"
						+ thumbSavePath.replace(this.pathProperty.getUpload(), "") + "/" + thumbFileName);

				this.logger.debug("file : " + fileDto.toString());
			} else {

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

	/**
	 * 
	 * @Method Name : uploadManual
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 매뉴얼 업로드
	 * @param file
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public FileDto uploadManual(MultipartFile file, Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		String companyId = DataUtils.getString(params, "companyId", "");
		String manualIndex = DataUtils.getString(params, "manualIndex", "");

		String div = "manual/" + companyId + "/" + manualIndex;
		params.put("div", div);
		return uploadFileWithThumbnail(file, params, request, false);
	}

	/**
	 * 
	 * @Method Name : grantFile
	 * @작성일 : 2021. 1. 28.
	 * @작성자 : anchiseong
	 * @변경이력 :
	 * @Method 설명 : 디렉토리 권한처리.
	 * @param filePath
	 * @param readable
	 * @param writable
	 * @param executable
	 */
	private void grantFile(String filePath, boolean readable, boolean writable, boolean executable) {
		File file = new File(filePath);
		this.logger.info("filePath : " + filePath + "[read : " + readable + ", write : " + writable + ", execute : "
				+ executable);

		if (executable)
			file.setExecutable(true, false);

		if (readable)
			file.setReadable(true, false);

		if (writable)
			file.setWritable(true, false);
	}
}
