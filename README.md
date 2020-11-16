# SCGLAB 채팅상담 시스템

## | 시스템 아키텍쳐
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
| 배치작업 | Spring batch | |
| 인증 | JWT | Token을를 이용한 인증방식 |
| XSS Filter | lucy xss servlet filter | |
| template engine | freemarker | |
| 테스팅 | Junit | |
| 문서 | Open API 3.0 (구 Swagger) | |


---
## | 로컬 개발환경 구축
### 프로젝트 생성 (clone)
```bash
# git clone https://github.com/chiseong-ahn/connect.git
```
### Git 정책
- master : 운영서버 배포
- dev : 개발서버 배포용 브랜치
- feature-[기능] : 기능별 개발 브랜치.

### 빌드
```bash
# mvn clean install
```

### 서비스 구동
- local 구동일 경우 애플리케이션 실행시 내장된 Redis를 자동으로 구동함.
- 개발 또는 운영일 경우 미리 Redis가 구동되어 있어야 함.

```bash
# mvn spring-boot:run
# mvn spring-boot:run -Dspring.profiles.active=dev  // 개발용 프로파일로 실행. (기본 local)
# mvn spring-boot:run -Dspring.profiles.active=live  // 운영용 프로파일로 실행. (기본 local)
```

### 서비스 확인
- 서비스 : http://localhost:8080
- 문서 : http://localhost:8080/swagger-ui.html


## | 개발 가이드

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
Object[] args = new String[1];
args[0] = "매핑할 문구";
String message = messageService.getMessage("error.parameter1", args);
```

### 로그인된 회원 정보
```java
// 일반 회원정보 추출.
Member member = this.loginService.getMember(request);

// 토큰을 통한 회원정보 추출.
String token = "토큰 문자열";
Profile profile = getProfile(token);
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