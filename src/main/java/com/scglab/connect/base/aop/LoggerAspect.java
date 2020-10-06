package com.scglab.connect.base.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Logger;

@Component
@Aspect
public class LoggerAspect {
protected final Logger logger = (Logger) LoggerFactory.getLogger(getClass());
	
	@Pointcut("execution(* com.scglab.connect..*Controller.*(..))")
	public void controllerPointcut(){}
	
	@Pointcut("execution(* com.scglab.connect..*Service.*(..))")
	public void servicePointcut(){}
	
	@Pointcut("execution(* com.scglab.connect..*Dao.*(..))")
	public void daoPointcut(){}
	
	@Before("controllerPointcut()")
	public void beforeController(JoinPoint jp){
		String className = jp.getTarget().getClass().getName();
		String methodName = jp.getSignature().getName();
		this.logger.debug(className + "." + methodName + " Before ==========================================");
		
		Object[] args = jp.getArgs();
		if(args != null){
			if(args.length > 0){
				String logMessage; 
				
				int k = 1;
				for(Object arg : args){
					logMessage = "Argument[" + k + "] : " + arg.toString();
					k++;
					this.logger.debug(logMessage);
				}
			}
		}		
	}
	
	@Before("servicePointcut()")
	public void beforeService(JoinPoint jp){
		String className = jp.getTarget().getClass().getName();
		String methodName = jp.getSignature().getName();
		this.logger.debug(className + "." + methodName + " Before ==========================================");
		
		Object[] args = jp.getArgs();
		if(args != null){
			if(args.length > 0){
				
				String logMessage; 
				
				int k = 1;
				for(Object arg : args){
					//logMessage = "Argument[" + k + "] : " + arg.toString();
					k++;
					//this.logger.debug(logMessage);
				}
			}
		}		
	}
	
	@AfterReturning(pointcut = "servicePointcut()", returning = "returnData")
	public void afterReturningService(JoinPoint jp, Object returnData){
		String className = jp.getTarget().getClass().getName();
		String methodName = jp.getSignature().getName();
		this.logger.debug(className + "." + methodName + " AfterReturn ==========================================");
		
		if(returnData != null){
			String logMessage  = "returnData : " + returnData.toString();
			this.logger.debug(logMessage);
		}
	}
	
	@Before("daoPointcut()")
	public void beforeDao(JoinPoint jp){
		String className = jp.getTarget().getClass().getName();
		String methodName = jp.getSignature().getName();
		this.logger.debug(className + "." + methodName + " Before ==========================================");
		
		Object[] args = jp.getArgs();
		if(args != null){
			if(args.length > 0){
				String logMessage; 
				
				int k = 1;
				for(Object arg : args){
					logMessage = "Argument[" + k + "] : " + arg.toString();
					k++;
					this.logger.debug(logMessage);
				}
			}
		}		
	}
	
	@AfterReturning(pointcut = "daoPointcut()", returning = "returnData")
	public void afterReturningDao(JoinPoint jp, Object returnData){
		String className = jp.getTarget().getClass().getName();
		String methodName = jp.getSignature().getName();
		this.logger.debug(className + "." + methodName + " After Return ==========================================");
		
		if(returnData != null){
			String logMessage  = "returnData : " + returnData.toString();
			this.logger.debug(logMessage);
		}
	}
}