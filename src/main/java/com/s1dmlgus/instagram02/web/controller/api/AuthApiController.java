package com.s1dmlgus.instagram02.web.controller.api;


import com.s1dmlgus.instagram02.service.UserService;
import com.s1dmlgus.instagram02.web.dto.JoinDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AuthApiController {

    private final UserService userService;


    @PostMapping("/auth/signup")
    public void join(@Valid JoinDto joinDto) {

        userService.join(joinDto);
    }

}
