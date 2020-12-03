# 다국어 처리
## 파일 관리
- message.yml (기본)
- message_ko_KR.yml (한국어)
- message_en_US.yml (영어)

## 예제
```java
// 기본 메세지 가져오기.
String message = messageService.getMessage("[메세지 코드]"); // message_ko_KR.yml 에 정의된 메세지 코드 입력.
```
```java
// 파라미터를 매핑한 메세지 가져오기.
String[] messageArgs = new String[1];
messageArgs[0] = "홍길동";
String message = messageService.getMessage("main.greeting", messageArgs);
// 안녕하세요. {0}님 => 안녕하세요. 홍길동님
```
