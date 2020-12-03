package com.scglab.connect.batch;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Profile("dev | live1")
@Component
public class ScheduleTask {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 
	 * @Method Name : dailyStatistics
	 * @작성일 : 2020. 11. 10.
	 * @작성자 : anchiseong
	 * @변경이력 : 일일집계 처리 - 개발 및 운영서버에서만 실행. (운영서버의 경우 메인 1개의 서버에서만 실행되도록 설정.)
	 * @Method 설명 :
	 */
	@Scheduled(cron = "0 10 0 * * *")
	public void dailyStatistics() {
		this.logger.debug("schedule - current data : " + LocalDateTime.now());
	}
}
