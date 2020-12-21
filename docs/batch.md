# 배치 설정
## 스케쥴 등록
- com.scglab.connect.batch.ScheduleTask.java 참고.
```java
/*
* @Scheduled(cron = "* * * * * *") > 초(0~59) 분(0~59) 시간(0~23) 일(1-31) 월(1~12) 요일(0~7)
* @Scheduled(fixedRate = (1000 * 1)) > 시간(1초)마다 실행(이전 작업 종료와 상관없이 시작)
* "매일 0시 10분에 실행 되는 함수."
*/
@Scheduled(cron = "0 10 0 * * *")
public void dailyStatistics() {
    LocalTime startTime = LocalTime.now();
    this.logger.info("일일집계처리 시작. : " + startTime);
    
    // TODO :   

    LocalTime endTime = LocalTime.now();
    this.logger.info("일일집계처리 종료. : " + endTime);
    
    Duration duration = Duration.between(startTime, endTime);
    
    long diffSeconds = duration.getSeconds();
    this.logger.info("일일집계처리 소요시간(초) : " + diffSeconds);
}
```

[< 목록으로 돌아가기](manual.md)