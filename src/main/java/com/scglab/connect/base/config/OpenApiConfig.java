package com.scglab.connect.base.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;

@OpenAPIDefinition(
		info = @Info(
				title = "상담톡 API Document", 
				version = "v1"
		)
)
@Configuration
public class OpenApiConfig {
	
	@Bean
	 public OpenAPI customOpenAPI() {
	   return new OpenAPI()
	          .components(new Components()
	          .addSecuritySchemes("bearer-key", 
	          new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").description("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiY29tcGFueUlkIjoiMSIsImNvbXBhbnlVc2VDb25maWdKc29uIjpudWxsLCJjb21wYW55TmFtZSI6IuyEnOyauOuPhOyLnOqwgOyKpCIsImlzQWRtaW4iOjEsImlzQ3VzdG9tZXIiOjAsImF1dGhMZXZlbCI6MiwibG9naW5OYW1lIjoiY3NtYXN0ZXIxIiwic3RhdGUiOjAsInByb2ZpbGVJbWFnZUlkIjowLCJzcGVha2VySWQiOjE3NywibmFtZSI6IuyEnOyauOuPhOyLnOqwgOyKpCIsImNyZWF0ZURhdGUiOiIyMDE5LTExLTA4IDE0OjEwOjA5IiwidXBkYXRlRGF0ZSI6IjIwMjAtMTEtMjQgMTQ6MDc6NTUiLCJ1cGRhdGVNZW1iZXJJZCI6bnVsbCwiaWF0IjoxNjA2MTk1NDMwLCJleHAiOjE2MDYyODE4MzB9.MGE-_-P5gYAV2K3zp_oSyfYaNIaUf9yebFUx_sH9jPE")));
	}
	
	@Bean
	public GroupedOpenApi sample() {
		String[] paths = { "/api/example/**" };
		return GroupedOpenApi.builder().setGroup("샘플 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi auth() {
		String[] paths = { "/api/auth/**" };
		return GroupedOpenApi.builder().setGroup("로그인 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi company() {
		String[] paths = { "/api/company/**" };
		return GroupedOpenApi.builder().setGroup("회사관리 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi member() {
		String[] paths = { "/api/member/**" };
		return GroupedOpenApi.builder().setGroup("회원관리 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi minwon() {
		String[] paths = { "/api/minwon/**" };
		return GroupedOpenApi.builder().setGroup("민원관리 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi category() {
		String[] paths = { "/api/category/**" };
		return GroupedOpenApi.builder().setGroup("카테고리관리 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi template() {
		String[] paths = { "/api/template/**" };
		return GroupedOpenApi.builder().setGroup("템플릿관리 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi keyword() {
		String[] paths = { "/api/keyword/**" };
		return GroupedOpenApi.builder().setGroup("키워드관리 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi manual() {
		String[] paths = { "/api/manual/**" };
		return GroupedOpenApi.builder().setGroup("메뉴얼 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi customer() {
		String[] paths = { "/api/customer/**" };
		return GroupedOpenApi.builder().setGroup("고객관리 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi stats() {
		String[] paths = { "/api/stats/**" };
		return GroupedOpenApi.builder().setGroup("통계관리 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi room() {
		String[] paths = { "/api/room/**" };
		return GroupedOpenApi.builder().setGroup("채팅방 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi message() {
		String[] paths = { "/api/message/**" };
		return GroupedOpenApi.builder().setGroup("메세지 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi autMessage() {
		String[] paths = { "/api/auto-message/**" };
		return GroupedOpenApi.builder().setGroup("자동메세지 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi link() {
		String[] paths = { "/api/link/**" };
		return GroupedOpenApi.builder().setGroup("링크 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi extenal() {
		String[] paths = { "/api/external/**" };
		return GroupedOpenApi.builder().setGroup("외부 연동 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi file() {
		String[] paths = { "/api/file/**" };
		return GroupedOpenApi.builder().setGroup("파일 API").pathsToMatch(paths).build();
	}
	
}
