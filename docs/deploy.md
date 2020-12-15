# 배포
## 1. 전체 프로세스
> 빌드 -> WAR 전송  -> 백업 -> 배포 -> 서버 재구동 -> Log 확인 -> 서비스 확인


## 2. 세부 설명.
### (1) 빌드
> 로컬환경에서 메이븐 프로젝트를 빌드하여 WAR파일을 생성한다.
```
# command
mvn clean install

or

# eclipse
프로젝트 선택 -> 마우스오른쪽 클릭 -> Run as 선택 -> Maven install 선택
``` 

### (2) 파일전송
> FTP를 통해 개발서버로 WAR 파일 전송.
- /home/gasapp/cstalk/backend/webapps/connect-1.0.0.war

### (3) 배포 및 WAS 재구동
> 배포용 쉘 파일 실행.
```
$ cd /home/gasapp/cstalk/backend
$ ./deploy.sh
```

### (4) 로그 확인
> 로그확인 쉘 파일 실행.
```
$ cd /home/gasapp/cstalk/backend
$ ./log.sh
```

### (5) 서비스 구동 확인.
- https://cstalk-dev.gasapp.co.kr/api/document

[< 목록으로 돌아가기](manual.md)