package com.s1dmlgus.instagram02.handler.exception;


import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
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


}
