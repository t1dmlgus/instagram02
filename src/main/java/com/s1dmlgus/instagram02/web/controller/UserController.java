package com.s1dmlgus.instagram02.web.controller;


import com.s1dmlgus.instagram02.config.auth.PrincipalDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/user")
@Controller
public class UserController {

    @GetMapping("/{id}")
    public String profile(@PathVariable int id) {

        return "user/profile";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        model.addAttribute("principal", principalDetails.getUser());

        return "user/update";
    }

}
