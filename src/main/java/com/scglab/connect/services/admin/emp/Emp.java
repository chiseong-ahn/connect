package com.scglab.connect.services.admin.emp;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Api(tags = "계정")
public class Emp implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "계정번호", example = "", position = 1)
	private int id;
	
	@ApiModelProperty(hidden = true)
	private int cid;
	
	@ApiModelProperty(hidden = true)
	private int speaker;
	
	@ApiModelProperty(value = "권한 (2-관리자, 4-상담사, 7-조회자, 9-Guest)", example = "9", position = 2)
	private int auth;
	
	@ApiModelProperty(value = "프로필이미지 업로드 번호", allowEmptyValue = true, position = 3)
	private int profileimg;
	
	@ApiModelProperty(value = "사번", example = "csahn", position = 4)
	private String empno;
	
	@ApiModelProperty(value = "상담상태 (0-상담중, 1-휴식중, 2-회의중, 3-콜집중, 5-퇴근, 6-점심, 9-기타)", example = "9", position = 5)
	private int state;
	
	@ApiModelProperty(hidden = true)
	private LocalDateTime createDate;
	
	
	@ApiModelProperty(hidden = true)
	private LocalDateTime workDate;
}
