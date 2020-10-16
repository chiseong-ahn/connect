package com.scglab.connect.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;

@ConfigurationProperties("domain")
@Getter
public class DomainProperties {
	private String sdtalk;
	private String relay;
}
