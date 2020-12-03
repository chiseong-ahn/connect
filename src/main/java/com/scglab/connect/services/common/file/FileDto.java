package com.scglab.connect.services.common.file;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDto {
	private String originFileName;
	
	private String fileName;
	private String savePath;
	private long fileSize;
	private int width;
	private int height;
	
	private String thumbFileName;
	private String thumbSavePath;
	private long thumbFileSize;
	private int thumbWidth;
	private int thumbHeight;
}
