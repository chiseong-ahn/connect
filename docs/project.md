# 프로젝트 구조
| 1Depth | 2Depth | 3Depth | 4Depth | 5Depth | Description |
|:---:|---|---|---|---|---|
| src   |       |   |   | | 개발 루트 디렉토리|
|       | main  |   |   | | 서비스용 개발 디렉토리 |
|       | test  |   |   | | 테스트용 개발 디렉토리 |
|       |       | java.com.scglab.connect  |  | | 자바 패키지 |
|       |       |   | base   |   | 서비스 기본 설정 관리 |
|       |       |   |   | annotaions  | 사용자정의 어노테이션 설정 |
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
|       |       |   |   | automessage  | 자동메시지 |
|       |       |   |   | category  | 카테고리 |
|       |       |   |   | common  | 공통 기능 |
|       |       |   |   | company  | 회사 |
|       |       |   |   | customer  | 고객 |
|       |       |   |   | keyword | 키워드 |
|       |       |   |   | link | 링크 |
|       |       |   |   | login | 로그인(인증) |
|       |       |   |   | main | 메인 |
|       |       |   |   | manual | 메뉴얼 |
|       |       |   |   | member | 멤버 |
|       |       |   |   | message | 메시지 |
|       |       |   |   | minwon | 민원 |
|       |       |   |   | room | 룸 |
|       |       |   |   | sample | 샘플소스 |
|       |       |   |   | socket | 소켓 |
|       |       |   |   | stats | 통계 |
|       |       |   |   | template | 템플릿 |
|       |       |   | utils  |    | 유틸리티 |
|       |       |  resources |   |  | 리소스 관리|
|       |       |   | config  |  |  |
|       |       |   | mapper  |   | sql xml|
|       |       |   | messages  |   | 다국어 메시징 |
|       |       |   | static  |   | 정적리소스 |
|       |       |   | templates  |   | template engine 파일  |
|       |       |   | application.yml  |   | 어플리케이션 설정 |
| pom.xml      |       |   |   |   | 메이븐 설정 파일 |