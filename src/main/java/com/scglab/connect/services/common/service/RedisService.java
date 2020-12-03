package com.scglab.connect.services.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
//	@Autowired
//	private RedisRepository redisRepository;
//	
//	public void save(String id, Map<String, Object> data) {
//		data.put("id", id);
//		this.redisRepository.save(data);
//	}
//	
//	public Map<String, Object> findOne(String id) {
//		if(this.redisRepository.existsById(id)) {
//			return this.redisRepository.findById(id).get();
//		}
//		
//		return null;
//	}
}
