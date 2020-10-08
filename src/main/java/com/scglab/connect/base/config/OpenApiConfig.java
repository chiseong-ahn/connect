package com.scglab.connect.base.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
		info = @Info(
				title = "상담톡 API Document", 
				description = "API Document", 
				version = "v1", 
				contact = @Contact(
						name = "csahn", 
						email = "csahn@scglab.com"
				)
		)
)
@Configuration
public class OpenApiConfig {
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
