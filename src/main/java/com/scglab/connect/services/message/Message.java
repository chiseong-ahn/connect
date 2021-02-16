package com.scglab.connect.services.message;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Message {
	private long id;					// 메시지 id
	private long joinMessageId;			// 속한 방의 조인 메세지 id
	private String createDate;			// 등록일시
	private String updateDate;			// 수정일시 
    private long speakerId;				// 메시지를 작성한 사용자
    private String speakerName;			// 메시지를 작성한 사용자 명
    private long roomId;				// 방 id
    private String roomName;			// 방 이름
    private String companyId;				// 회사 id
    private int messageType;			// 메시지 유형(0-일반, 1-이미지, 2-동영상, 3-첨부, 4-링크, 5-이모티콘)
    private int noReadCount;			// 읽지않은 사용자 수.
    private int isSystemMessage;		// 시스템 메세지 여부(0-시스템메시지 아님, 1-시스템 메시지)
    private String message;				// 메시지
    private int messageAdminType;		// 시스템 메시지의 다른 유형(0-일반메시지, 1-시스템메시지)
    private int isEmployee;				// 상담사 작성여부.
    private int isCustomer;				// 고객 작성여부.
    private int isOnline;				// 방의 온라인 상태(1-온라인, 0-오프라인)
    private String messageDetail;		// 메시지 상세
    private String thumbnail;			// 이미지 썸네일 URL
    
    @JsonIgnore
    private String createDate2;			// 등록일시2
}