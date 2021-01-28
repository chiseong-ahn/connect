# 배포
## 1. 프로세스
### (1) 개발(dev)
> 빌드 -> 개발서버로 WAR 전송 -> 현재소스 백업(최근 10개 유지) -> 배포(WAR) -> 서버 재구동 -> Log 확인 -> 서비스 확인.

### (2) 운영(dev)
> 빌드 -> 개발서버로 WAR 전송 -> 운영서버로 WAR 전송 -> 현재소스 백업(최근 10개 유지) -> 배포(WAR) -> 서버 재구동 -> Log 확인 -> 서비스 확인.

## 2. 세부 설명.
### (1) 빌드
> 로컬환경에서 메이븐 프로젝트를 빌드하여 WAR파일을 생성한다.
```
# command
mvn clean install

or

# eclipse
프로젝트 선택 -> 마우스오른쪽 클릭 -> Run as 선택 -> Maven install 실행.
``` 

### (2) 파일전송
> FTP를 통해 개발서버로 WAR 파일 전송.
- /home/gasapp/cstalk/backend/build/dev.war		-- 개발서버
- /home/gasapp/cstalk/backend/build/live1.war		-- 운영서버1
- /home/gasapp/cstalk/backend/build/live2.war		-- 운영서버2

### (3) 배포 및 WAS 재구동
> 배포용 쉘 파일 실행.
```sh
$ cd /home/gasapp/cstalk/backend/deploy
$ ./dev.sh		-- 개발서버 배포.
$ ./restart.sh	-- 개발서버 재구동.
$ ./live1.sh		-- 운영서버1 배포.
$ ./live2.sh		-- 운영서버2 배포.

```

### (4) 로그 확인
> 로그확인 쉘 파일 실행.
```sh
$ cd /home/gasapp/cstalk/backend
$ ./log.sh 1		-- 1번 서버 로그 출력.
$ ./log.sh 2		-- 2번 서버 로그 출력.
```

### (5) 서비스 구동 확인.
- https://cstalk-dev.gasapp.co.kr

[< 목록으로 돌아가기](manual.md)
