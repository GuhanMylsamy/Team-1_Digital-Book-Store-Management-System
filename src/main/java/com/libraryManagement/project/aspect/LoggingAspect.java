package com.libraryManagement.project.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // This pointcut targets all methods within your application's packages
    @Pointcut("within(com.libraryManagement.project..*) && !within(com.libraryManagement.project.config.RequestLoggingFilter)")
    public void applicationMethods() {}

    @Around("applicationMethods()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        if (log.isDebugEnabled()) {
            log.debug(" -> Entering: {}.{}() with args: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    Arrays.toString(joinPoint.getArgs()));
        }

        try {
            Object result = joinPoint.proceed();
            long timeTaken = System.currentTimeMillis() - startTime;

            if (log.isDebugEnabled()) {
                log.debug(" <- Exiting: {}.{}() with result: {} | Time: {}ms",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(),
                        result,
                        timeTaken);
            }
            return result;
        } catch (Throwable e) {
            log.error("!! Exception in {}.{}(): {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    e.getMessage(), e);
            throw e;
        }
    }
}