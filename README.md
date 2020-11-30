# SCGLAB 채팅상담 어플리케이션

## 1. 시스템 아키텍쳐
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
| 문서 | Open API 3.0 (구 Swagger) | |


---
## 2. 로컬 개발환경 구축
### Git 정책
- master : 운영서버 배포용 브랜치
- dev : 개발서버 배포용 브랜치
- feature-[기능] : 기능별 로컬개발 브랜치

#### 업무 진행 순서
- dev 브랜치 소스기준으로 feature-[신규업무]를 브랜치 생성.
- feature-[신규업무] 브랜치에서 개발 / 로컬 테스트
- 개발완료시 dev 브랜치 merge를 위한 Pull Request 발송.
- 승인권자가 Pull Request 처리. (승인/반려)
- 승인 시 feature-[신규업무]에서 dev 브랜치로 merge.
- dev 브랜치 개발서버 배포.
- 업무요청자(기획자 또는 QA담당자)가 QA 진행.
- QA완료 시 dev 브랜치에서 master 브랜치로 merge.
- 서비스 최종 점검.

### 프로젝트 생성 (clone)
```bash
// 임시 Repository
# git clone https://github.com/chiseong-ahn/connect.git
```

### 빌드
```bash
# mvn clean install
```

### 서비스 구동
- local 구동일 경우 애플리케이션 실행시 내장된 Redis를 자동으로 구동함.
- 개발 또는 운영서버 배포일 경우 미리 Redis가 서비스 구동되어 있어야 함.

```bash
# mvn spring-boot:run   // 옵션이 없을경우 local 환경으로 구동.
# mvn spring-boot:run -Dspring.profiles.active=dev      // 개발용으로 구동.
# mvn spring-boot:run -Dspring.profilㅇes.active=live   // 운영용으로 구동.
```

### 서비스 확인
- 서비스 : http://localhost:8080
- 문서 : http://localhost:8080/swagger-ui.html


## 3. 개발 가이드

### (1) 프로젝트 구조
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


### 주석
> Class
- Eclipse > Preference > Java > Code Style > Code Template > Comments > Types
```
/**
 * @FileName : ${file_name}
 * @Project : ${project_name}
 * @Date : ${date} 
 * @작성자 : ${user}
 * @변경이력 :
 * @프로그램 설명 :
 */
```

> Method
- Eclipse > Preference > Java > Code Style > Code Template > Comments > Methods
```
 /**
 * @Method Name : ${enclosing_method}
 * @작성일 : ${date}
 * @작성자 : ${user}
 * @변경이력 : 
 * @Method 설명 :
 * ${tags}
 */
```


### CRUD
- Sample CRUD 참고.
- com.scglab.connect.services.samples

### 메세지 관리(다국어)
- message.yml (기본)
- message_ko_KR.yml (한국어)
- message_en_US.yml (영어)
```java
// 기본 메세지 가져오기.
String message = messageService.getMessage("[메세지 코드]"); // message_ko_KR.yml 에 정의된 메세지 코드 입력.

// 파라미터를 매핑한 메세지 가져오기.
String[] messageArgs = new String[1];
messageArgs[0] = "홍길동";
String message = messageService.getMessage("main.greeting", messageArgs);
// 안녕하세요. {0}님 => 안녕하세요. 홍길동님
```

### 로그인 된 멤버정보 가져오기.
```java
// 멤버 정보 추출. - REST API 기반.(웹소켓 서비스에서는 사용불가)
Member member = this.loginService.getMember(request);

// 인증토큰에서 멤버 정보 추출. - 웹소켓 서비스에서 사용.
Member member = this.loginService.getMember(token);
```

### OpenAPI 사용방법
- https://swagger.io/

> Model
- @Schema : 모델 설명
```java
@Schema(description = "모델 명")
public class model {
    @Schema(description = "아이디", defaultValue = "0")
    private int id;
}
```

> Controller
- @Tag : 클래스 설명
```java
@RestController
@RequestMapping("/samples")
@Tag(name = "CRUD 예제", description = "CRUD 작성에 대한 예제입니다.")
public class SampleController {}
```

> Method
- @Operation : 메소드 설명
```java
@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
@Operation(summary="목록 조회", description = "조건에 맞는 게시물 목록을 조회합니다.")
public Map<String, Object> list(@Parameter(hidden = true) @RequestParam Map<String, Object> params) throws Exception {
    return null;
}
```

### Testing
```
```


