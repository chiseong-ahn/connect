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

@Profile({"local", "dev"})
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
	          new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").description("로그인을 통해 발급된 인증토큰을 삽입한다.<br>eyJhbGciOiJIUzI1NiJ9.eyJpZCI6NzAsImNvbXBhbnlJZCI6IjIiLCJjb21wYW55VXNlQ29uZmlnSnNvbiI6bnVsbCwiY29tcGFueU5hbWUiOiLsnbjsspzrj4Tsi5zqsIDsiqQiLCJpc0FkbWluIjoxLCJhdXRoTGV2ZWwiOjIsImxvZ2luTmFtZSI6bnVsbCwic3RhdGUiOjAsInByb2ZpbGVJbWFnZUlkIjowLCJzcGVha2VySWQiOjIxOSwibmFtZSI6IuyViOy5mOyEsSIsImlhdCI6MTYwNDk5MjM4NiwiZXhwIjoxNjA1MDc4Nzg2fQ.Da4hSzwLSbqb9wiTtZ5Hfce6m8bbLuEYXU2MzcdmiuE")));
	}
	
	@Bean
	public GroupedOpenApi sample() {
		String[] paths = { "/samples/**" };
		return GroupedOpenApi.builder().setGroup("샘플 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi auth() {
		String[] paths = { "/auth/**" };
		return GroupedOpenApi.builder().setGroup("인증 API").pathsToMatch(paths).build();
	}
	
	/*
	@Bean
	public GroupedOpenApi customer() {
		String[] paths = { "/customers/**" };
		return GroupedOpenApi.builder().setGroup("고객(회원) API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi template() {
		String[] paths = { "/templates/**" };
		return GroupedOpenApi.builder().setGroup("답변템플릿 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi adminMenu() {
		String[] paths = { "/admin/**" };
		return GroupedOpenApi.builder().setGroup("관리자 메뉴 API").pathsToMatch(paths).build();
	}
	
	@Bean
	public GroupedOpenApi talkMenu() {
		String[] paths = { "/talk/**" };
		return GroupedOpenApi.builder().setGroup("상담채팅 API").pathsToMatch(paths).build();
	}
	*/
	
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
	public GroupedOpenApi room() {
		String[] paths = { "/api/room/**" };
		return GroupedOpenApi.builder().setGroup("상담(채팅방)관리 API").pathsToMatch(paths).build();
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
}
