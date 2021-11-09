package com.s1dmlgus.instagram02.handler.exception;


import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {


    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 유효성 검사 예외
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ResponseDto<?>> validationException(CustomValidationException e) {

        logger.info("validation Exception ! : {} ", e.toString(), e);
        return new ResponseEntity<>(new ResponseDto<>(e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<ResponseDto<?>> CustomApiException(CustomApiException e) {

        logger.info("Exception ! : {} ", e.toString(), e);
        return new ResponseEntity<>(new ResponseDto<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }


}
