# connect
SCGLAB Connect System

### 개발환경
- Repository : https://github.com/chiseong-ahn/connect.git

### CORS

### 다국어 메세지
- classpath:messages/message_ko_KR.yml
- classpath:messages/message_en_US.yml
```
// 메세지 서비스객체 주입
@Autowired
private MessageService messageService;      

// message.[국가코드].yml에 등록된 메세지 코드
String messageCode = "main.greeting";       

// 메세지 파라미터가 있을 경우
Object[] parameters = new Object[1];        // 치환 메세지 파라마티
parameters[0] = "안치성";
this.messageService.getMessage("main.greeting", parameters);

// 메세지 파라미터가 없을 경우
this.messageService.getMessage("main.greeting");
```

### Swagger
- [HOST]:[PORT]/swagger-ui.html

#### Class 설정
```
@Api(tags = "클래스 이름")
public class SampleController {
}
```

#### Method 설정
```
@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiOperation(value = "샘플 상세 조회", notes = "게시물의 상세내용을 조회한다.")
public Map<String, Object> object(@ApiParam(hidden=true) @RequestParam Map<String, Object> params, @ApiParam(value="식별번호 아이디", required=true) @PathVariable String id) throws Exception {
	return this.sampleService.object(params, id);
}
```

### Example (CRUD)
- com/scglab/connect/services/sample 디렉토리 참고.


### Logger
- 사용방법
```
private final Logger logger = LoggerFactory.getLogger(this.getClass());

this.logger.trace("Print trace log");
this.logger.debug("Print debug log");
this.logger.info("Print info log");
this.logger.warn("Print warn log");
this.logger.error("Print error log");
```

### Test

### File Up/Download

### Crypto
- 양방향 암호화 (AES256)

- 단방향 암호화 (SHA256)

### Exception 



### Pretty json
```
// pom.xml 설정 
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.0.1</version>
</dependency>
```
```
// application.yml 설정 
spring:
    jackson:
        serialization:
          INDENT_OUTPUT: true
```

### Scouter 연동
