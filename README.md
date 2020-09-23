# connect
SCGLAB Connect System

### 개발환경
- Repository : https://github.com/chiseong-ahn/connect.git

### Example (CRUD)

### Logger

### Test

### File Up/Download

### Crypto

### Swagger
- [HOST]:[PORT]/swagger-ui.html

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