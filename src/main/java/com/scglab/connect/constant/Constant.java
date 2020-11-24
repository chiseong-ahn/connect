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
	
	public final static String	SOCKET_ENDPOINT = "/ws-stomp";
	public final static String	SOCKET_ROOM_PREFIX = "/sub/chat/room/";		// 소켓 채널의 prefix
	public final static String	SOCKET_RECEIVE_URI = "/socket/message";		// 소켓 채널의 prefix
	
	public final static long	JSONTOKEN_EXPIRE = 60 * 60 * 24 * 1000;		// 토큰의 유효기간 
	public final static String	JSONTOKEN_SECRETKEY = "lsisjwtsecretkey";	// 토큰의 비밀키
	
	public final static int		DEFAULT_MESSAGE_INTERVAL_DAY = 1500;		// 메세지 조회 기간(일)
	public final static int		DEFAULT_MESSAGE_MORE_PAGE_SIZE = 30;		// 더보기 메세지 갯수
	
	
	
}
