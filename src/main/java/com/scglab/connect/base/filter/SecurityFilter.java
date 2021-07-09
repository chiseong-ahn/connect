package com.scglab.connect.base.filter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityFilter implements Filter {
	
	private final String endpointBasePath = "/api/actuator/tomcat";
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		final String uri = ((HttpServletRequest)req).getRequestURI();
		
		
		if (StringUtils.contains(uri, endpointBasePath)) {
			chain.doFilter(req, res);
		}else {
			LocalTime startTime = LocalTime.now();
			logger.debug("프로세스 시작. : " + startTime);
			
			chain.doFilter(req, res);
			
			LocalTime endTime = LocalTime.now();
			logger.debug("프로세스 종료. : " + endTime);
	
			Duration duration = Duration.between(startTime, endTime);
	
			long diffMillis = duration.toMillis();
			logger.debug("프로세스 처리시간 : " + diffMillis + "ms");
		}
	}

}
