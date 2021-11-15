package com.s1dmlgus.instagram02.web.controller;

import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import com.s1dmlgus.instagram02.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@RequiredArgsConstructor
@Controller
public class ImageController {

    private final ImageService imageService;


    @GetMapping("/")
    public String index() {

        return "image/story";
    }

    @GetMapping("/image/popular")
    public String popular(){

        return "image/popular";
    }

    @GetMapping("/image/upload")
    public String upload(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){

        model.addAttribute("principal", principalDetails.getUser());
        return "image/upload";
    }

}
