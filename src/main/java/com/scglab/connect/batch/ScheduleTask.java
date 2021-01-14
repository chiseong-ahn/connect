package com.scglab.connect.batch;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.scglab.connect.services.stats.StatsService;

@Profile("local | dev1 | live1")
@Component
public class ScheduleTask {
	
	/*
	 * 개발 및 운영서버에서만 실행. (운영서버의 경우 메인 1개의 서버에서만 실행되도록 설정.)
	 * https://copycoding.tistory.com/305 참고.
	 * 
	 * @Scheduled(cron = "* * * * * *") > 초(0~59) 분(0~59) 시간(0~23) 일(1-31) 월(1~12) 요일(0~7)
	 * @Scheduled(fixedRate = (1000 * 1)) > 시간(1초)마다 실행(이전 작업 종료와 상관없이 시작)
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private StatsService statsService;

	/**
	 * 
	 * @Method Name : dailyStatistics
	 * @작성일 : 2020. 11. 10.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 일일집계 처리.
	 */
	@Scheduled(cron = "0 10 00 * * *")
	public void dailyStatistics() {
		LocalTime startTime = LocalTime.now();
		this.logger.info("일일집계처리 시작. : " + startTime);
		
		// TODO : 상담 일일집계.
		this.statsService.createStatsCompanyDaily();

		LocalTime endTime = LocalTime.now();
		this.logger.info("일일집계처리 종료. : " + endTime);
		
		Duration duration = Duration.between(startTime, endTime);
		
		long diffSeconds = duration.getSeconds();
		this.logger.info("일일집계처리 소요시간(초) : " + diffSeconds);
	}
	
	/**
	 * 
	 * @Method Name : syncMinwonCodes
	 * @작성일 : 2020. 12. 11.
	 * @작성자 : anchiseong
	 * @변경이력 : 
	 * @Method 설명 : 기간계 민원코드 동기화.
	 */
	@Scheduled(cron = "0 30 14 * * *")
	public void syncMinwonCodes() {
		LocalTime startTime = LocalTime.now();
		this.logger.info("기간계 민원코드 동기화 시작. : " + LocalDateTime.now());
		// todo:

		this.logger.info("기간계 민원코드 동기화 종료. : " + LocalDateTime.now());
		LocalTime endTime = LocalTime.now();
		
		Duration duration = Duration.between(startTime, endTime);
		long diffSeconds = duration.getSeconds();
		this.logger.info("기간계 민원코드 동기화 소요시간(초) : " + diffSeconds);
	}
}
