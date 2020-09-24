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
this.messageService.getMessage("main.greeting", null);
```

### Swagger
- [HOST]:[PORT]/swagger-ui.html

### Example (CRUD)



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

### Scouter 연동
