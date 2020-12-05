# 배치 설정
## 스케쥴 등록
```java
@Scheduled(cron = "0 10 0 * * *")
public void dailyStatistics() {
    this.logger.debug("매일 0시 10분에 실행 되는 함수.");
}
```