package com.scglab.connect.services.sample;

import org.springframework.data.repository.CrudRepository;

public interface SampleRedisRepository extends CrudRepository<Sample, String> {

}
