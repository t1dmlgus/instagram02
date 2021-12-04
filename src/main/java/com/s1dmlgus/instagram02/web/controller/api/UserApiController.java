package com.s1dmlgus.instagram02.web.controller.api;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.service.UserService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.auth.JoinRequestDto;
import com.s1dmlgus.instagram02.web.dto.user.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserApiController {


    Logger logger = LoggerFactory.getLogger(UserApiController.class);

    public final UserService userService;
    

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody JoinRequestDto joinRequestDto, BindingResult bindingResult) {

        ResponseDto<?> joinUser = userService.join(joinRequestDto);
        logger.info(joinUser.toString());

        return new ResponseEntity<>(joinUser, HttpStatus.OK);
    }


    // 프로필 유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> profile(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        ResponseDto<?> profile = userService.profile(id, principalDetails);
        logger.info("profileDto : {}", profile);

        return new ResponseEntity<>(profile, HttpStatus.OK);
    }


    // 회원 수정
    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto, BindingResult bindingResult,  @AuthenticationPrincipal PrincipalDetails principalDetails) {

        ResponseDto<?> updateUser = userService.update(userUpdateRequestDto, principalDetails);

        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

}
