# 로그 관리
## 로그출력 레벨 설정
- src/main/resources/config/logback-[profile].xml

## 사용방법
- 로거 선언
```
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
```

- 로그 출력
```
    this.logger.debug("디버그 메세지");
    this.logger.info("정보 메세지");
    this.logger.warn("경고 메세지");
    this.logger.error("에러 메세지");
```
