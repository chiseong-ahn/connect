# WAS(Web Application Server) 설정 - 개발서버

## Tomcat 설정.
> 디렉토리
- /data/project/gasapp-cstalk/tomcat

> Port 설정.

| 항목 | Port | 설명 |
|:---:|:---:|---|
| Server | 8116 ||
| Http | 8115 ||
| Redirect | 8113 | |
| AJP | 8112 | Apache 와 연동 |

> 서버 설정.
- /data/project/gasapp-cstalk/tomcat/conf/server.xml
- 위 포트 설정 함.

> 구동환경(env)
- 개발 profile로 구동하기 위한 파일.
- /data/project/gasapp-cstalk/tomcat/bin/setenv.sh
```
export JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=dev"
```

## Tomcat 서비스 제어
> 서비스 구동
- /data/project/gasapp-cstalk/tomcat/bin
```
$ ./tomcatctl start
```

> 서비스 중지
- /data/project/gasapp-cstalk/tomcat/bin
```
$ ./tomcatctl stop
```


[< 서버환경 구성으로 돌아가기](./env.md)
