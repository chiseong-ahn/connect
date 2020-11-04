package com.scglab.connect.services.common.service;

import java.util.Map;

import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<Map<String, Object>, String>{

}
