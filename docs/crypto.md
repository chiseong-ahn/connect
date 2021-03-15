# 암/복호화

## 양방향 암호화 (AES256)
```java
try {
AES256Utils aes256Utils = new AES256Utils();

// 평문 문자열
String text = "안녕하세요";

// 암호화된 문자열
String encryptText = aes256Utils.encrypt(text);

// 복호화된 문자열
String decryptText = aes256Utils.decrypt(encryptText);

// 결과 출력
System.out.println("text : " + text);
System.out.println("encryptText : " + encryptText);
System.out.println("decryptText : " + decryptText);

}catch(Exception e) {
e.printStackTrace();
}
```



## 단방향 암호화 (SHA256)
```java
try {
    SHA256Utils sha256Utils = new SHA256Utils();

    // 평문 문자열
    String text = "안녕하세요";
    
    // 암호화된 문자열
    String encryptText = sha256Utils.encrypt(text);
    
    // 결과 출력
    System.out.println("text : " + text);
    System.out.println("encryptText : " + encryptText);

} catch (Exception e) {
    e.printStackTrace();
}
```

[< 목록으로 돌아가기](manual.md)