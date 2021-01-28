# 프로젝트 구조
| 1Depth | 2Depth | 3Depth | 4Depth | 5Depth | Description |
|:---:|---|---|---|---|---|
| src   |       |   |   | | 개발 루트 디렉토리|
|       | main  |   |   | | 서비스용 개발 디렉토리 |
|       | test  |   |   | | 테스트용 개발 디렉토리 |
|       |       | java.com.scglab.connect  |  | | 자바 패키지 |
|       |       |   | base   |   | 서비스 기본 설정 관리 |
|       |       |   |   | annotations  | 사용자정의 어노테이션 설정 |
|       |       |   |   | aop  | 스프링 AOP 설정 |
|       |       |   |   | config  | 스프링 MVC 설정 |
|       |       |   |   | database  | 데이터베이스 설정 |
|       |       |   |   | exception  | 예외처리 설정 |
|       |       |   |   | filter  | 필터 설정 |
|       |       |   |   | interceptor  | 인터셉터 설정 |
|       |       |   |   | pubsub  | Redis 구독/발행 서비스 설정  |
|       |       |   |   | websocket  | 웹소켓 설정  |
|       |       |   | batch  |   | 배치 설정 관리 |
|       |       |   | constant  | | 정적 변수 관리  |
|       |       |   | properties | | 프로퍼티 관리  |
|       |       |   | services  | | 기능별 서비스 관리  |
|       |       |   |   | automessage  | 자동메시지 서비스 |
|       |       |   |   | category  | 카테고리 서비스 |
|       |       |   |   | common  | 공통 서비스 |
|       |       |   |   | company  | 회사 서비스 |
|       |       |   |   | customer  | 고객 서비스 |
|       |       |   |   | keyword | 키워드 서비스 |
|       |       |   |   | link | 링크 서비스 |
|       |       |   |   | login | 로그인(인증) 서비스 |
|       |       |   |   | main | 메인 서비스 |
|       |       |   |   | manual | 메뉴얼 서비스 |
|       |       |   |   | member | 멤버(상담사) 서비스 |
|       |       |   |   | message | 메시지 서비스 |
|       |       |   |   | minwon | 민원 서비스 |
|       |       |   |   | room | 룸 서비스 |
|       |       |   |   | sample | 샘플 서비스 |
|       |       |   |   | socket | 소켓 서비스 |
|       |       |   |   | stats | 통계 서비스 |
|       |       |   |   | template | 템플릿 서비스 |
|       |       |   | utils  |    | 유틸리티 |
|       |       |  resources |   |  | 리소스 관리|
|       |       |   | config  |  |  |
|       |       |   | mapper  |   | sql xml|
|       |       |   | messages  |   | 다국어 메시징 |
|       |       |   | static  |   | 정적리소스 |
|       |       |   | templates  |   | template engine 파일  |
|       |       |   | application.yml  |   | 어플리케이션 설정 |
| pom.xml      |       |   |   |   | 메이븐 설정 파일 |


[< 목록으로 돌아가기](manual.md)