package com.s1dmlgus.instagram02.web.controller.api;


import com.s1dmlgus.instagram02.exception.CustomValidationException;
import com.s1dmlgus.instagram02.service.UserService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AuthApiController {

    private final UserService userService;


    @PostMapping("/auth/signup")
    public ResponseEntity<?> join(@RequestBody @Valid JoinDto joinDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            throw new CustomValidationException("유효성 검사 실패", errorMap);

        }

        ResponseDto<?> joinUser = userService.join(joinDto);

        return new ResponseEntity<>(joinUser, HttpStatus.OK);

    }

}
