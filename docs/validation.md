# 유효성 검증 Validation

## 파라미터 유효성 검증.
> 예제
```java
public void example(Map<String, Object> params, HttpServletRequest request) throws Exception {
    String errorParams = "";

    // name 이라는 문자열 파라미터의 데이터가 존재하는지 확인.
    if(!this.commonService.validString(params, "name"))
        // 파라미터가 존재하지 않는 파라미터 등록.
        errorParams = this.commonService.appendText(errorParams, "이름-name");
    
    // 파라미터 유효성 검증.
    if(!errorParams.equals("")) {
        // 필수파라미터 누락에 따른 오류 유발처리.
        this.errorService.throwParameterErrorWithNames(errorParams);
    }
}
```
> 결과
```json
{
    "status": 500,
    "name": "INTERNAL_SERVER_ERROR",
    "message": "필수 파라미터(이름-name)가 누락되어 정상적으로 처리되지 않았습니다.",
    "trace": {
        "classLoaderName": null,
        "moduleName": null,
        "moduleVersion": null,
        "methodName": "throwParameterErrorWithNames",
        "fileName": "ErrorService.java",
        "lineNumber": 24,
        "className": "com.scglab.connect.services.common.service.ErrorService",
        "nativeMethod": false
    }
}
```

[< 목록으로 돌아가기](manual.md)