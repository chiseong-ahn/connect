package com.scglab.connect.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisRunner implements ApplicationRunner {
	
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		ValueOperations<String, String> values = this.redisTemplate.opsForValue();
		values.set("test1", "value1");
		values.set("test2", "value2");
		values.set("test3", "value3");
	}
	
}
