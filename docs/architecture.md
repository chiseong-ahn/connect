# 아키텍쳐 구성
| 항목 | 내용 | 설명 |
|:---:|---|---|
| Web Server | Apache | HTTPS, WebSocket, LB |
| WAS | Tomcat | HTTPS, WebSocket, 로컬 개발시 Embedded Tomcat 사용. |
| 개발언어 | Java | openjdk 11 |
| 프레임워크 | 스프링부트 | 2.3.3.RELEASE |
| RDB | Mysql | |
| Pub/Sub | Redis | 로컬 개발시 Embedded Redis 사용. |
| SQL mapper | Mybatis | |
| 로그 | Logback | |
| API | Rest | REST 기반의 API |
| 웹소켓 | Websocket | STOMP + SockJs + Redis pub/sub |
| 인증 | JWT | Token을 이용한 인증방식 |
| XSS Filter | lucy xss servlet filter | |
| 테스팅 | Junit | |
| 문서 | Postman, Markdown | 서비스하는 API 및 개발매뉴얼 |


[< 목록으로 돌아가기](manual.md)