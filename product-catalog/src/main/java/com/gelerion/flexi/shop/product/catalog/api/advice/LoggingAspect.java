package com.gelerion.flexi.shop.product.catalog.api.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class LoggingAspect {

    @Before("within(com.gelerion.flexi.shop.product.catalog.api.controllers..*)")
    public void logBeforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();
        log.info("Entering method: {} with arguments: {}", methodName, methodArgs);
    }

    @AfterReturning(pointcut = "within(com.gelerion.flexi.shop.product.catalog.api.controllers..*)",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Exiting method: {} with result: {}", methodName, result);
    }

    @AfterThrowing(pointcut = "within(com.gelerion.flexi.shop.product.catalog.api.controllers..*)",
            throwing = "exception")
    public void logMethodThrow(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.error("Exception thrown in method: {} with message: {}", methodName, exception.getMessage());
    }
}
