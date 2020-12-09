package com.scglab.connect.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties("server")
@Getter
@Setter
@ToString
public class ServerProperties {
	private int port;
}
