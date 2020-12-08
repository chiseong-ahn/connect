# 인증

## 1. 인증이 필요한 라우팅
> @Auth 어노테이션 적용.
```java
@Auth
@RequestMapping(method = RequestMethod.GET, value = "/needLogin", produces = MediaType.APPLICATION_JSON_VALUE)
public String needLogin(@Parameter(hidden = true) @RequestParam Map<String, Object> params) throws Exception {
    return "인증이 유효합니다."; 
}
```

> 인증되어 있는 경우.
- 결과
```java
인증이 유효합니다.
```

> 인증이 되지 않은 경우.
- 결과
```json
{
    "status": 401,
    "name": "UNAUTHORIZED",
    "message": "인증토큰이 전달되지 않아 리소스를 이용할 수 없습니다.",
    "trace": {
        "classLoaderName": null,
        "moduleName": null,
        "moduleVersion": null,
        "methodName": "isAccess",
        "fileName": "CommonInterceptor.java",
        "lineNumber": 63,
        "className": "com.scglab.connect.base.interceptor.CommonInterceptor",
        "nativeMethod": false
    }
}
```

## 2. 인증된 정보 가져오기
> Request 객체에 저장된 인증정보 추출.
```java
// import package
import com.scglab.connect.services.login.LoginService;

// declare member object
@Autowired private LoginService loginService;

// use in method
Member member = this.loginService.getMember(request);
String companyId = member.getCompanyId();
long loginId = member.getId();

```