package com.scglab.connect.base.filter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		LocalTime startTime = LocalTime.now();
		logger.info("프로세스 시작. : " + startTime);
		
		chain.doFilter(req, res);
		
		LocalTime endTime = LocalTime.now();
		logger.info("프로세스 종료. : " + endTime);

		
		Duration duration = Duration.between(startTime, endTime);

		//long diffSeconds = duration.getSeconds();
		long diffMillis = duration.toMillis();
		logger.info("프로세스 처리시간 : " + diffMillis + "ms");
	}

}
