# 어플레키에션 설정 프로퍼티
## 사용방법
- application.yml
```yml
domain:
    cstalk:
    relay-scg: relay-scg-dev.gasapp.co.kr
    relay-inc: relay-inc-dev.gasapp.co.kr
```
- property 생성
```java
@Component
@ConfigurationProperties("domain")
@Getter
@Setter
@ToString
public class DomainProperties {
	private String cstalk;
	private String relayScg;
	private String relayInc;
}
```

- 서비스 사용
```java
// 멤버 객체 선언
@Autowired private DomainProperties domainProperty;

// method 사용.
String cstalk = this.domainProperty.getCstalk();
```

[< 목록으로 돌아가기](manual.md)