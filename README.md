# connect
SCGLAB Connect System

### 개발환경
- Repository : https://github.com/chiseong-ahn/connect.git
- Redis : 로컬실행시 redis가 구동되어 있어야 함.


### Redis 실행
- application.yml (Redis 정보) 

### 실행
```
# mvn install
# mvn spring-boot:run
```


### CORS
- com.scglab.connect.base.config.WebMvcConfig.java
```
@Override
public void addCorsMappings(CorsRegistry registry) {
	registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*");
}
```

### 다국어 메세지
- classpath:messages/message_ko_KR.yml
- classpath:messages/message_en_US.yml
```
// 메세지 서비스객체 주입
@Autowired
private MessageService messageService;      

// message.[국가코드].yml에 등록된 메세지 코드
String messageCode = "main.greeting";       

// 메세지 파라미터가 없을 경우
this.messageService.getMessage(messageCode);

// 메세지 파라미터가 있을 경우
Object[] parameters = new Object[1];        // 치환 메세지 파라마티
parameters[0] = "안치성";
this.messageService.getMessage(messageCode, parameters);
```

### Swagger
- [HOST]:[PORT]/swagger-ui.html

#### Class 설정
```
@Tag(name = "샘플관리", description = "CRUD에 대한 샘플 컨트롤러")
public class SampleController {
}
```

#### Method 설정
```
@RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary="고객 회원정보 수정", description = "", security = {@SecurityRequirement(name = "bearer-key")})
    public Map<String, Object> update(@Parameter(hidden = true) @RequestParam Map<String, Object> params, @Parameter(name = "id", description = "고객관리번호", required = true, in = ParameterIn.PATH, example = "") @PathVariable int id) throws Exception {
	params.put("id", id);
	return this.customerService.update(params);
}
```

### Example (CRUD)
- com/scglab/connect/services/sample 디렉토리 참고.


### Batch(Schedule) 사용
- com.scglab.connect.batch.ScheduleTask.java
```
@Scheduled(cron = "0 10 0 * * *")
public void dailyStatistics() {
	this.logger.debug("schedule - current data : " + LocalDateTime.now());
}
```

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

### Exception Handler
- com.scglab.connect.base.exception.GlobalExceptionHAndler.java 에서 관리.
- todo : Exception 발생시 Email 또는 Slack 을 연동하여 알림을 받을 수 있도록 할 예정. 



