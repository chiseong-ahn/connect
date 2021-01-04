package com.scglab.connect.constant;

public class Constant {
	
	public final static String	YES = "Y";
	public final static String	NO = "N";

	public final static String	COMPANY_CODE_SEOUL = "1";					// 서울도시가스 회사코드
	public final static String	COMPANY_NAME_SEOUL = "서울도시가스";
	public final static String	COMPANY_CODE_INCHON = "2";					// 인천도시가스 회사코드
	public final static String	COMPANY_NAME_INCHON = "인천도시가스";
	
	public final static String	AUTH_USER = "AUTH_USER";					// 로그인된 회원을 Request 객체에 저장하는 키값.
	public final static String	AUTH_MEMBER = "AUTH_MEMBER";				// 로그인된 회원을 Request 객체에 저장하는 키값.
	public final static String	AUTH_BEARERR_KEY = "bearer-key";			// 인증토큰 헤더명
	
	public final static String	SOCKET_APPLICATION_DESTINATION_PREFIX = "/pub";
	public final static String	SOCKET_SIMPLE_BROKER = "/sub";
	public final static String	SOCKET_USER_DESTINATION_PREFIX = "/user";
	public final static String	SOCKET_USER_SUBSCRIBE_URI = "/session/message";		// 소켓 수신채널의 prefix
	public final static String	SOCKET_RECEIVE_URI = "/socket/message";		// 소켓 수신채널의 prefix
	public final static String	SOCKET_ENDPOINT = "/ws";
	public final static String	SOCKET_ROOM_PREFIX = "/socket/room";		// 소켓 채널의 prefix
	public final static String	SOCKET_LOBBY_ROOM = "LOBBY";
	public final static String	SOCKET_PRIVATE_ROOM = SOCKET_USER_DESTINATION_PREFIX + SOCKET_USER_SUBSCRIBE_URI;		// 유저 수신채
	
	public final static long	JSONTOKEN_EXPIRE = 60 * 60 * 24 * 1000 * 7;		// 토큰의 유효기간 
	//public final static String	JSONTOKEN_SECRETKEY = "SCGLABCSTALKAYSBHYACS20210205000";	// 토큰의 비밀키
	public final static String	JSONTOKEN_SECRETKEY = "SCGLABCSTALKAYSBHYACSSCGLABCSTALKAYSBHYACSSCGLABCSTALKAYSBHYACS";	// 토큰의 비밀키
	
	public final static String	ENCRYPT_SECRETKEY = "SCGLABCSTALKAYSBHYACS20210205000";		// 암호화 비밀키
	public final static String	CUSTOMER_KEY = "SCGLABCSTALKAYSBHYACS";
	
	public final static int		DEFAULT_MESSAGE_INTERVAL_DAY = 1500;		// 메세지 조회 기간(일)
	public final static int		DEFAULT_MESSAGE_MORE_PAGE_SIZE = 30;		// 더보기 메세지 갯수
	
	public final static int		ROOM_STATE_READY = 0;						// 룸 상태 - 대기
	public final static int		ROOM_STATE_ING = 1;							// 룸 상태 - 진행
	public final static int		ROOM_STATE_CLOSE = 2;						// 룸 상태 - 종료
	
	public final static int 	MESSAGE_TYPE_NORMAL = 0;					// 메시지유형 - 일반(텍스트)
	public final static int		MESSAGE_TYPE_IMAGE = 1;						// 메시지유형 - 이미지
	public final static int		MESSAGE_TYPE_MOVIE = 2;						// 메시지유형 - 동영상
	public final static int		MESSAGE_TYPE_ATTACH = 3;					// 메시지유형 - 파일
	public final static int		MESSAGE_TYPE_LINK = 4;						// 메시지유형 - 링크
	public final static int		MESSAGE_TYPE_EMOTICON = 5;					// 메시지유형 - 이모티콘
	public final static int		MESSAGE_TYPE_TEL = 6;						// 메시지유형 - 전화번호
	
	public final static String	UPLOAD_IMAGE_FORMAT = "jpg,jpeg,git,png";	// 업로드 허용 확장자.
	public final static int		THUMBNAIL_WIDTH_SIZE = 300;
}
