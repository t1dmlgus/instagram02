package com.s1dmlgus.instagram02.handler.aop;


import com.s1dmlgus.instagram02.handler.exception.CustomValidationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Component
@Aspect     // AOP 처리를 할 수 있는 핸들러
public class ValidationAdvice {

    Logger logger = LoggerFactory.getLogger(ValidationAdvice.class);


    // 어떤 특정함수 전, 후로 실행
    @Around("execution(* com.s1dmlgus.instagram02.web.controller.api.*Controller.*(..))")
    public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object[] args = proceedingJoinPoint.getArgs();

        logger.info(Arrays.toString(args));

        for (Object arg : args) {

            if (arg instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult) arg;

                if (bindingResult.hasErrors()) {
                    Map<String, String> errorMap = new HashMap<>();

                    for (FieldError fieldError : bindingResult.getFieldErrors()) {
                        errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());

                        logger.info(fieldError.getField());
                        logger.info(fieldError.getDefaultMessage());
                    }
                    throw new CustomValidationException("유효성 검사 실패", errorMap);
                }
            }
        }

        return proceedingJoinPoint.proceed();
    }



}
