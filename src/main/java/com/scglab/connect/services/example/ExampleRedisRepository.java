package com.scglab.connect.services.example;

import org.springframework.data.repository.CrudRepository;

public interface ExampleRedisRepository extends CrudRepository<Example, String> {

}
