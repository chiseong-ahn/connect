# 아키텍쳐 구성
| 항목 | 내용 | 설명 |
|:---:|---|---|
| 서버 | Tomcat | 로컬 개발시 Embedded Tomcat 사용. |
| 개발언어 | Java | openjdk 12 |
| 프레임워크 | 스프링부트 | 2.3.3.RELEASE |
| RDB | Mysql | |
| Memory DB | Redis | 로컬 개발시 Embedded Redis 사용. |
| SQL mapper | Mybatis | |
| 로그 | Logback | 일별 로그파일 자동 생성.|
| API | Rest | REST 기반의 API |
| 웹소켓 | Websocket | STOMP + SockJs + Redis pub/sub |
| 인증 | JWT | Token을 이용한 인증방식 |
| XSS Filter | lucy xss servlet filter | |
| template engine | freemarker | 프로토타입 작성을 위해사용(서비스용 아님) |
| 테스팅 | Junit | |
| 문서 | Postman + OpenApi 3.0(구 Swagger) | 서비스하는 API 정의|


[< 목록으로 돌아가기](manual.md)