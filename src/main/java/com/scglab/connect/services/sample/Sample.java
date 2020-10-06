package com.scglab.connect.services.sample;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Getter
@RedisHash("sample")
public class Sample implements Serializable {
	
	@Id
    private String id;
    private Long amount;
    private LocalDateTime refreshTime;

    @Builder
    public Sample(String id, Long amount, LocalDateTime refreshTime) {
        this.id = id;
        this.amount = amount;
        this.refreshTime = refreshTime;
    }

    public void refresh(long amount, LocalDateTime refreshTime){
        if(refreshTime.isAfter(this.refreshTime)){ // 저장된 데이터보다 최신 데이터일 경우
            this.amount = amount;
            this.refreshTime = refreshTime;
        }
    }   
}
