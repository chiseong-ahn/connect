# SCGLAB 채팅상담 어플리케이션

### 프로젝트 생성 (clone)
```bash
# git clone https://github.com/chiseong-ahn/connect.git
```

### 빌드
```bash
# mvn clean install
```


### 서비스 구동
- local 구동일 경우 애플리케이션 실행시 내장된 Redis를 자동으로 구동함.
- 개발 또는 운영서버 배포일 경우 미리 Redis가 서비스 구동되어 있어야 함.

```bash
# mvn spring-boot:run   // 옵션이 없을경우 local 환경으로 구동.
# mvn spring-boot:run -Dspring.profiles.active=dev      // 개발용으로 구동.
# mvn spring-boot:run -Dspring.profilㅇes.active=live   // 운영용으로 구동.
```

### 서비스 확인
- 서비스 : http://localhost:8080

[링크-개발 메뉴얼](/docs/manual.md "개발 메뉴얼 페이지로 이동합니다.")
