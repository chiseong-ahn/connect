package com.scglab.connect.services.talk;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;

@Getter
@RedisHash("Space")
public class Talk implements Serializable {
	
	@Id
    private int id;
	private int emp;
	private int cid;
	private int chatid;
	private String customer;
	private String prehistory;	
    private LocalDateTime createDate;
   
}
