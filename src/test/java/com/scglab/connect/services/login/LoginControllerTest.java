package com.scglab.connect.services.login;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {
	
	@Autowired 
	private MockMvc mockMvc;
	
	@Ignore
	@Test
	public void 고객_로그인_Test() throws Exception {
		String api = "/api/auth/loginCustomer";
		String companyId = "1";
		String gasappMemberNumber = "3825";
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("companyId", companyId);
		params.add("gasappMemberNumber", gasappMemberNumber);
		
		this.mockMvc.perform(post(api)
					.params(params)
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.customer.gasappMemberNumber", Matchers.is(gasappMemberNumber)))
					.andDo(print());
	}
	@Ignore
	@Test
	public void 멤버_로그인_Test() throws Exception {
		String api = "/api/auth/loginMember";
		String companyId = "1";
		String loginName = "csmaster1";
		String password = "1212";
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("companyId", companyId);
		params.add("loginName", loginName);
		params.add("password", password);
		
		this.mockMvc.perform(post(api)
					.params(params)
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.profile.loginName", Matchers.is(loginName)))
					.andDo(print());
	}
	
	@Ignore
	@Test
	public void 멤버_인증정보조회_테스트() throws Exception {
		String api = "/api/auth/profile";
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiY29tcGFueUlkIjoiMSIsImNvbXBhbnlVc2VDb25maWdKc29uIjpudWxsLCJjb21wYW55TmFtZSI6IuyEnOyauOuPhOyLnOqwgOyKpCIsImlzQWRtaW4iOjEsImlzQ3VzdG9tZXIiOjAsImF1dGhMZXZlbCI6MiwibG9naW5OYW1lIjoiY3NtYXN0ZXIxIiwic3RhdGUiOjAsInByb2ZpbGVJbWFnZUlkIjowLCJzcGVha2VySWQiOjE3NywibmFtZSI6Iu2Zjeq4uOuPmSIsImRlcHROYW1lIjoi6rK96riwMeyngeyYgSIsInBvc2l0aW9uTmFtZSI6IuyCrOybkCIsImNyZWF0ZURhdGUiOiIyMDE5LTExLTA4IDE0OjEwOjA5IiwidXBkYXRlRGF0ZSI6IjIwMjAtMTItMjEgMTY6NDA6MTAiLCJ1cGRhdGVNZW1iZXJJZCI6IjEiLCJpYXQiOjE2MDg1MzY3NTAsImV4cCI6MTYwOTE0MTU1MH0.PmCCrLUWjw52bxWdfiCJmMzohomrWV8T0S-atTVbDUo";
		String loginName = "csmaster1";
		
		this.mockMvc.perform(get(api)
					.header("Authorization", token)
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.loginName", Matchers.is(loginName)))
					.andDo(print());
	}
}
