# 배포 프로세스
## 1차 - Shell 을 이용한 배포.

### Step1. 빌드
> 로컬환경에서 메이븐 프로젝트를 빌드하여 WAR파일을 생성한다.
```
# command
mvn clean install

or

# eclipse
프로젝트 선택 -> 마우스오른쪽 클릭 -> Run as 선택 -> Maven install 선택
``` 

### Step2.
> FTP를 통해 개발서버로 WAR 파일 전송.
- /home/gasapp/cstalk/backend/webapps/connect-1.0.0.war

### Step3.
> 배포용 쉘 파일 실행.
```
$ cd /home/gasapp/cstalk/backend
$ ./deploy.sh
```

### Step4. 로그 확인
> 로그확인 쉘 파일 실행.
```
$ cd /home/gasapp/cstalk/backend
$ ./log.sh
```

### Step5. 서비스 구동 확인.
- https://cstalk-dev.gasapp.co.kr/api/document

[< 목록으로 돌아가기](manual.md)