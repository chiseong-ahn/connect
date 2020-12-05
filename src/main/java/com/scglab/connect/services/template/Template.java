package com.scglab.connect.services.template;

import java.util.List;

import com.scglab.connect.services.keyword.Keyword;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(name = "답변템플릿")
@Setter
@Getter
@ToString
public class Template {
	private int id;
	private int categorySmallId;
	private String createDate;
	private String updateDate;
	private String udpateMemberId;
	private String companyId;
	private String ask;
	private String reply;
	private String link;
	private String linkProtocol;
	private String imagePath;
	private String imageName;
	private String memberId;
	private int isFavorite;
	private List<Keyword> keywordList;
}