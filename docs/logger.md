# 로그 관리
## 로그출력 레벨 설정
- src/main/resources/config/logback-[profile].xml

## 사용방법
- 로거 선언
```java
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
```

- 로그 출력
```java
    this.logger.debug("디버그 메세지");
    this.logger.info("정보 메세지");
    this.logger.warn("경고 메세지");
    this.logger.error("에러 메세지");
```

- 로그 설정(local 기준)
```xml
<logger name="jdbc" level="OFF"/>
	<logger name="jdbc.sqlonly" level="INFO"/>
	<logger name="jdbc.sqltiming" level="OFF"/>
	<logger name="jdbc.audit" level="OFF"/>
	<logger name="jdbc.resultset" level="OFF"/>
	<logger name="jdbc.resultsettable" level="INFO"/>
	<logger name="jdbc.connection" level="OFF"/>
    <logger name="io.swagger.models.parameters.AbstractSerializableParameter" level="ERROR" />
    <logger name="com.scglab.connect" level="DEBUG">
        <appender-ref ref="DAILY_ROLLING_FILE_APPENDER" />
    </logger>
    
    <root level="INFO">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="DAILY_ROLLING_FILE_APPENDER" />
    </root>
```