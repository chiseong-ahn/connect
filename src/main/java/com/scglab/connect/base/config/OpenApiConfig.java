package com.scglab.connect.base.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

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
	public GroupedOpenApi sample() {
		String[] paths = { "/samples/**" };
		return GroupedOpenApi.builder().setGroup("샘플 API").pathsToMatch(paths).build();
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
