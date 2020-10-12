package com.scglab.connect.services.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.ToString;

@ConfigurationProperties("path")
@Getter
@ToString
public class PathProperties {
	private String upload;
}
