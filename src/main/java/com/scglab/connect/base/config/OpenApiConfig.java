package com.scglab.connect.base.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
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
	          new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").description("로그인을 통해 발급된 인증토큰을 삽입한다.<br>eyJhbGciOiJIUzI1NiJ9.eyJhdXRoIjoyLCJzcGVha2VyIjoxNzcsImVtcCI6MSwiZW1wbm8iOiJjc21hc3RlcjEiLCJleHAiOjE2MDI4OTI1MzAsImlhdCI6MTYwMjgwNjEzMCwiY2lkIjoxfQ.yI4Hkr3ZFGngCzQNTCJhws-8-ftWYtNn_3X1GCqwFog")));
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
	
	@Bean
	public GroupedOpenApi customer() {
		String[] paths = { "/customer/**" };
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
}
