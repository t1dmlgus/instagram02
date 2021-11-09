package com.s1dmlgus.instagram02.web.controller.api;


import com.s1dmlgus.instagram02.service.UserService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AuthApiController {

    Logger logger = LoggerFactory.getLogger(AuthApiController.class);

    private final UserService userService;

    @PostMapping("/auth/signup")
    public ResponseEntity<?> join(@Valid @RequestBody JoinRequestDto joinRequestDto, BindingResult bindingResult) {

        ResponseDto<?> joinUser = userService.join(joinRequestDto);

        logger.info(joinUser.toString());
        return new ResponseEntity<>(joinUser, HttpStatus.OK);
    }

}
