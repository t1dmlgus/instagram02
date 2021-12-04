package com.s1dmlgus.instagram02.web.controller;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.service.UserService;
import com.s1dmlgus.instagram02.web.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/user")
@RequiredArgsConstructor
@Controller
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;


    // 회원가입
    @GetMapping("/join")
    public String join(){
        return "user/join";
    }

    // 회원정보 조회
    @GetMapping("/{id}")
    public String profile(@PathVariable Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        ResponseDto<?> profile = userService.profile(id, principalDetails);
        logger.info("profile : {}",profile);
        
        model.addAttribute("principal", principalDetails.getUser());
        model.addAttribute("profileDto", profile.getData());

        return "user/profile";
    }

    // 회원 수정
    @GetMapping("/update")
    public String update(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        model.addAttribute("principal", principalDetails.getUser());

        return "user/update";
    }

}
