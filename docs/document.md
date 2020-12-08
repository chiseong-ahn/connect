# API 문서 작성
### OpenAPI 사용방법
- 참고자료 : https://swagger.io/

### 문서 확인
- http://cstalk-dev.gasapp.co.kr/api/document

> Model
- @Schema : 모델 설명
```java
@Schema(description = "모델 명")
public class model {
    @Schema(description = "아이디", defaultValue = "0")
    private int id;
}
```

> Controller
- @Tag : 클래스 설명
```java
@RestController
@RequestMapping("/samples")
@Tag(name = "CRUD 예제", description = "CRUD 작성에 대한 예제입니다.")
public class SampleController {}
```

> Method
- @Operation : 메소드 설명
```java
@RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
@Operation(summary="목록 조회", description = "조건에 맞는 게시물 목록을 조회합니다.")
public Map<String, Object> list(@Parameter(hidden = true) @RequestParam Map<String, Object> params) throws Exception {
    return null;
}
```