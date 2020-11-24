package com.scglab.connect.services.common.file;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDto {
	private String orignFilename;
	private String saveFilename;
	private String savePath;
	private long fileSize;
}
