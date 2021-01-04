# 파일 업로드

## 업로드 정책
### 1. 저장경로
- 서비스구분 > 연도 > 월 디렉토리 자동생성 후 저장.<br>
  /data/project/gasapp-cstalk/apache/data/attach/[서비스구분]/[년도]/[월]
  - 디렉토리 : 읽기,쓰기,실행 권한 허용.
  - 파일 : 읽기 권한 허용.

### 2. 서비스주소
- https://cstalk-dev.gasapp.co.kr/attach/[서비스구분]/[년도]/[월]/[파일명]

### 3. 업로드 허용 포맷
- jpg, jpeg, gif, png (대소문자 구분하지 않음.)

### 4. 업로드 용량
- 5MB 제한

### 5. 서비스 구분
- 채팅상담 이미지 메시지 : talk
- 업무매뉴얼 이미지 : manual

### 6. 썸네일 생성
- 너비 최대값 (300px)
- 업로드한 이미지 너비가 너비 최대값을 초과할 경우 너비를 너비 최대값(300px)으로 줄이고 높이를 너비 축소비율과 동일한 비율로 축소.
- 너비가 최대값을 초과하지 않을경우 원본을 그대로 썸네일로 사용.

<hr><Br>

## 업로드 API
### 1. 일반 파일 업로드
1. API : https://[Domain]:[Port]/api/file/upload
2. Method : POST
3. Enctype : multipart/form-data
4. Parameters 
  - div : "talk" // 서비스 구분값
  - attach name : "file" // 업로드파일 엘리먼트 name.
5. response
```json
{
  "originFileName" : "20200805_133154.jpg",
  "fileUrl" : "https://cstalk-dev.gasapp.co.kr/attach/talk/2021/1/204724e0-7d35-4637-946b-9fd19649f59f.jpg",
  "fileSize" : 1301546,
  "width" : 4032,
  "height" : 1908,
}
```

### 2. 일반 파일 업로드 및 썸네일 생성
1. API : https://[Domain]:[Port]/api/file/uploadWithThumbnail
2. Method : POST
3. Enctype : multipart/form-data
4. Parameters 
  - div : "talk" // 서비스 구분값
  - attach name : "file" // 업로드파일 엘리먼트 name.
5. response
```json
{
  "originFileName" : "20200805_133154.jpg",
  "fileUrl" : "https://cstalk-dev.gasapp.co.kr/attach/talk/2021/1/204724e0-7d35-4637-946b-9fd19649f59f.jpg",
  "fileSize" : 1301546,
  "width" : 4032,
  "height" : 1908,
  "thumbFileUrl" : "https://cstalk-dev.gasapp.co.kr/attach/talk/2021/1/thumb_204724e0-7d35-4637-946b-9fd19649f59f.jpg",
  "thumbFileSize" : 5593,
  "thumbWidth" : 300,
  "thumbHeight" : 141
}
```

### 3. 매뉴얼 업로드
1. API : https://[Domain]:[Port]/api/file/uploadManual
2. Method : 
3. Enctype : multipart/form-data
4. Parameters
  - companyId : 1 // 가스사 관리번호.
  - manualIndex : 1 // 매뉴얼 인덱스.
  - attach name : "file" // 업로드파일 엘리먼트 name.
5. response
```json
{
  "originFileName" : "20200805_133154.jpg",
  "fileUrl" : "https://cstalk-dev.gasapp.co.kr/attach/manual/1/1/10.jpg",
  "fileSize" : 1301546,
  "width" : 4032,
  "height" : 1908
}
```

[< 목록으로 돌아가기](manual.md)