package com.scglab.connect.base.config;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import redis.embedded.RedisServer;

@Profile("local")
@Configuration
public class EmbeddedRedisConfig {
	@Value("${spring.redis.port}")
	private int redisPort;
	
	private RedisServer redisServer;
	
	@PostConstruct
	public void redisServer() throws IOException {
		this.redisServer = new RedisServer(redisPort);
		this.redisServer.start();
	}
	
	@PreDestroy
	public void stopRedis() {
		if(this.redisServer != null) {
			this.redisServer.stop();
		}
	}
}
