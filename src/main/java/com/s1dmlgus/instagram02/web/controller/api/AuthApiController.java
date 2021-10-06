package com.s1dmlgus.instagram02.web.controller.api;


import com.s1dmlgus.instagram02.handler.exception.CustomValidationException;
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
    public ResponseEntity<?> join(@Valid @RequestBody JoinDto joinDto, BindingResult bindingResult) {

        ResponseDto<?> joinUser = userService.join(joinDto);

        return new ResponseEntity<>(joinUser, HttpStatus.OK);
    }

}
