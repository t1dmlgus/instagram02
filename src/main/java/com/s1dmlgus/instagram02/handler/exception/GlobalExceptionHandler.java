package com.s1dmlgus.instagram02.handler.exception;


import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // 유효성 검사 예외
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ResponseDto<?>> validationException(CustomValidationException e) {

        return new ResponseEntity<>(new ResponseDto<>(e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<ResponseDto<?>> Exception(CustomApiException e) {

        return new ResponseEntity<>(new ResponseDto<>(e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    // 이미지 크기 예외처리
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<?> Exception(FileSizeLimitExceededException e) {

        return new ResponseEntity<>("첨부한 이미지는 2MB 이하여야 합니다.", HttpStatus.BAD_REQUEST);
    }

}
