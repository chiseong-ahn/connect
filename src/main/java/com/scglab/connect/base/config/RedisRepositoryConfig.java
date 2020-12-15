package com.scglab.connect.base.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisRepositoryConfig {

	@Value("${spring.redis.host}")
	private String redisHost;
	
	@Value("${spring.redis.port}")
	private int redisPort;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		this.logger.info("Redis host : " + this.redisHost);
		this.logger.info("Redis port : " + this.redisPort);
		
		return new LettuceConnectionFactory(this.redisHost, this.redisPort);
	}
	
//	@Bean
//	public RedisTemplate<?, ?> redisTemplate() {
//		RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
//		redisTemplate.setConnectionFactory(redisConnectionFactory());
//		return redisTemplate;
//	}
}
