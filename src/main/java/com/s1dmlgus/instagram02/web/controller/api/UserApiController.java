package com.s1dmlgus.instagram02.web.controller.api;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.service.UserService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import com.s1dmlgus.instagram02.web.dto.user.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
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

    public final UserService userService;


    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto, BindingResult bindingResult,  @AuthenticationPrincipal PrincipalDetails principalDetails) {

        ResponseDto<?> updateUser = userService.update(id, userUpdateRequestDto);
        if (updateUser.getData() != null) {
            principalDetails.setUser((User) updateUser.getData());
        }

        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

}
