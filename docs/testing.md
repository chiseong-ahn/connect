# 테스팅

## 테스티 항목 설정.
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyScgTest {
	
	@Autowired private CompanyScg companyScg;
	
	@Test
	public void 상담사_로그인() {
		String id="csmaster1";
		String password = "1212";
		
		boolean result = this.companyScg.login(id, password);
		Assertions.assertTrue(result);
	}
}
```

### 테스팅


[< 목록으로 돌아가기](manual.md)