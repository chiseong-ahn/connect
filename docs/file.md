# 파일 업로드

## 업로드 정책
### 1. 저장경로
- 서비스구분 > 연도 > 월 디렉토리 자동생성 후 저장.<br>
  /data/project/gasapp-cstalk/apache/data/attach/[서비스구분]]/[년도]/[월]

### 2. 서비스주소
- https://cstalk-dev.gasapp.co.kr/attach/[서비스구분]]/[년도]/[월]/[파일명]

### 3. 업로드 허용 포맷
- jpg, jpeg, gif, png (대소문자 구분하지 않음.)

### 4. 업로드 용량
- 5MB 제한

### 5. 서비스 구분
- 채팅상담 이미지 메시지 : talk
- 업무매뉴얼 이미지 : manual

### 6. 썸네일 생성
- 너비 최대값 (300px)
- 너비가 최대값을 초과할 경우 너비를 300px로 줄이고 높이를 너비 축소비율과 동일하게 축소.
- 너비가 최대값을 초과하지 않을경우 원본을 그대로 썸네일로 사용.

<hr><Br>

## 업로드 API
### 1. 파일 업로드
- API : https://[Domain]:[Port]/api/file/upload
- Method : POST
- attach name : file
- response
```json
{
  "originFileName" : "200706_SCG-LAB_이메일-서명_안치성.png",   // 원본 이미지명.
  "fileName" : "737dcaa7-df45-4f97-86d9-cae5e2a56a14.png",  // 저장된 이미지명.
  "savePath" : "/data/project/gasapp-cstalk/apache/data/attach/talk/2020/12", // 저장된 경로
  "fileSize" : 18645, // 파일 용량(Byte)
  "width" : 800,  // 너비 사이즈(px)
  "height" : 197, // 높이 사이즈(px)
}
```

### 2. 파일 업로드 및 썸네일 생성
- API : https://[Domain]:[Port]/api/file/uploadWithThumbnail
- Method : POST
- attach name : file
- response
```json
{
  "originFileName" : "200706_SCG-LAB_이메일-서명_안치성.png",   // 원본 이미지명.
  "fileName" : "737dcaa7-df45-4f97-86d9-cae5e2a56a14.png",  // 저장된 이미지명.
  "savePath" : "/data/project/gasapp-cstalk/apache/data/attach/talk/2020/12", // 저장된 경로
  "fileSize" : 18645, // 파일 용량(Byte)
  "width" : 800,  // 너비 사이즈(px)
  "height" : 197, // 높이 사이즈(px)
  "thumbFileName" : "thumb_737dcaa7-df45-4f97-86d9-cae5e2a56a14.png",   // 저장된 썸네일 이미지명.
  "thumbSavePath" : "/data/project/gasapp-cstalk/apache/data/attach/talk/2020/12",  // 저장된 썸네일 경로.
  "thumbFileSize" : 10413,  // 썸네일 용량(Byte)
  "thumbWidth" : 300, // 썸네일 너비 사이즈(px)
  "thumbHeight" : 73  // 썸네이 ㄹ높이 사이즈(px)
}
```

[< 목록으로 돌아가기](manual.md)