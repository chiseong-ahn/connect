# 알림
## Slack 알림 발송
- 사용방법
```java
@Autowired private NotificationService notiService;

public void sendMessage(){
    this.notiService.webhookForSlack("[발송할 메시지]");
}
```