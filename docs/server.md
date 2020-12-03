# SCGLAB 채팅상담 서버 환경.

## 1. Web Server
### (1) Apache 설정
> 기본경로
- /usr/local/apache2
- /data/project/cstalk/apache

> 설정파일
- /usr/local/apache2/conf/httpd.conf    // 기본 공통 설정.
- /data/project/cstalk/apache/conf      // 프로젝트별 설정. 
```
// todo.
```
- /usr/local/apache2/conf/workers.properties
```
// todo.
```

> 설정파일 테스트
```
[gasapp@gasapp-dev bin]$ ./apachectl configtest
Syntax OK
```
### (2) Apache 제어
> 서비스 상태확인
- /usr/local/apache2/bin/apachectl
```
[gasapp@gasapp-dev bin]$ ./apachectl status
httpd (pid 12302) already running
```
> 서비스 중지
```
# ./apachectl stop
```

> 서비스 구동
```
# ./apachectl start
```


> 서비스 재구동
```
# ./apachectl restart   // 강제 재시작.
# ./apachectl graceful  // 자동으로 configtest 과 같이 설정파일을 검사하며, 이전 작업이 종료된 후 재시작 된다.
```

## 2. WAS (Web application Server)


## 3. DB


## 4. Redis


## 5. 인증서


## 6. Scouter