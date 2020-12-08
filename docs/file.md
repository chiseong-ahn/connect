# 파일 업로드

## 예제.
- post http://localhost:8080/api/file
- 파일 선택 및 업로드.
- 업로드 응답결과
```java
{
  "originFileName" : "20200805_133154.jpg",     // 원본 파일명
  "fileName" : "c9307b6e-d718-447e-b736-35581f10aed4.jpg",  // 저장된 파일명
  "savePath" : "/Volumes/Data/_upload/test/2020/12",    // 파일이 저장된 경로
  "fileSize" : 1301546,     // 파일용량(Byte)
  "width" : 4032,   // 가로 사이즈
  "height" : 1908,  // 세로 사이즈
  "thumbFileName" : "thumb_c9307b6e-d718-447e-b736-35581f10aed4.jpg",   // 썸네일 파일명
  "thumbSavePath" : "/Volumes/Data/_upload/test/2020/12",   // 썸네일파일이 저장된 경로
  "thumbFileSize" : 5593,       // 썸네일 파일용량(Byte)
  "thumbWidth" : 300,   // 썸네일 가로 사이즈
  "thumbHeight" : 141   // 썸네일 세로 사이즈
}
```


[< 목록으로 돌아가기](manual.md)