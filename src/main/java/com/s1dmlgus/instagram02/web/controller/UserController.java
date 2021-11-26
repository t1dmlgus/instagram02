package com.s1dmlgus.instagram02.web.controller;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.domain.user.User;
import com.s1dmlgus.instagram02.service.UserService;
import com.s1dmlgus.instagram02.web.dto.user.UserProfileResponseDto;
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


    @GetMapping("/{id}")
    public String profile(@PathVariable Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        UserProfileResponseDto profileDto = userService.getProfile(id);

        logger.info("profileDto : {}", profileDto);

        model.addAttribute("profileDto", profileDto);
        //model.addAttribute("principal", principalDetails);
        return "user/profile";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        model.addAttribute("principal", principalDetails.getUser());

        return "user/update";
    }

}
