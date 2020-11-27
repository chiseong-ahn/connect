package com.scglab.connect.services.room;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "방 정보")
@Setter
@Getter
@ToString
public class Room {
	private Long id;					// 방id
	private String name;				// 방이름 
	
	private String memberName;			// 상담사 이름.
	private int memberId;				// 상담사id
	
	private String companyId;			// 회사id
	private String companyName;			// 회사명
	
	private int state;					// 방 상태 
	private int joinMessageId;			// 조인 시작id 
	private int chatId;					// 민원 연동id
	private Object joinHistoryJson;		// 조인 히스토리
	private String recentMessageId;		// 
	private int isOnline;				// 방의 온라인 상태
	
	private String endDate;				// 종료일
	private int lastMemberId;			// 이전 담당회원id
	private String joinStartDate;		// 조인시작메세지 생성일
	private String customerSpeakerId;	// 고객의 Speaker id
	private int noReadCount;			// 읽지않은 사용자 수
	private String waitStartDate;		// wait 시간

	private int lastMessageId;			// 마지막 메세지 id
	private String lastMessage;			// 마지막 메세지 
	private String lastMessageCreateDate;	// 마지막 메세지의 생성일.
	
	private String customerName;		// 고객이름
	private String gasappMemberNumber;	// 고객의 가스앱 모바일 id
	private String telNumber;			// 고객 전화번호
	private int isBlockCustomer;		// 고객의 block 상태
	
	private int speakMinute;			// 상담 시간
	private int joinHistoryCount;		// 이전 상담대화 건 수.
	
	private String createDate;
	private String updateDate;
	private String updateMemberId;
	
}