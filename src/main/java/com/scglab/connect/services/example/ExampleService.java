package com.scglab.connect.services.example;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ExampleService {
	
	public Map<String, Object> list(Map<String, Object> params) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("total", 100);
		
		return data;
	}
}
