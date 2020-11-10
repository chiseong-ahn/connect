package com.scglab.connect.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties("jwt")
@Getter
@Setter
@ToString
public class JwtProperties {
	private String secretKey;
	private String validTimeAdmin;
	private String validTimeCustomer;
	
	private Validtime validetime;
	
	class Validtime {
		private String customer;
		private String admin;
	}
}
