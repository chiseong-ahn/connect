# connect
SCGLAB Connect System

## 시스템 아키텍쳐
| 항목 | 내용 | 설명 |
|:---:|---|---|
| 서버 | Tomcat | 로컬 개발시 Embedded Tomcat 사용. |
| 개발언어 | Java | |
| 프레임워크 | 스프링부트 | |
| RDB | Mysql | |
| Memory DB | Redis | 로컬 개발시 Embedded Redis 사용. |
| SQL mapper | Mybatis | |
| 로그 | Logback | |
| API | Rest | |
| 웹소켓 | Websocket + STOMP + SockJs + Redis pub/sub | |
| 배치작업 | Spring batch | |
| 인증 | JWT | Token을를 이용한 인증방식 |
| XSS Filter | lucy xss servlet filter | |
| template engine | freemarker | |
| 테스팅 | Junit | |
| 문서 | Open API 3.0 (구 Swagger) | |

## 로컬 개발환경 구축
### 프로젝트 생성 (clone)
```
# git clone https://github.com/chiseong-ahn/connect.git
```
### Git 정책
- master : 운영서버 배포
- dev : 개발서버 배포용 브랜치
- feature-[기능] : 기능별 개발 브랜치.

### 빌드
```
# mvn clean install
```

### 서비스 구동
- local 구동일 경우 내장된 Redis를 자동으로 구동함.
- 개발 또는 운영일 경우 미리 Redis가 구동되어 있어야 함.

```
# mvn spring-boot:run
# mvn spring-boot:run -Dspring.profiles.active=dev  // 개발용 프로파일로 실행. (기본 local)
# mvn spring-boot:run -Dspring.profiles.active=live  // 운영용 프로파일로 실행. (기본 local)
```


