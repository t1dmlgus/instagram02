package com.s1dmlgus.instagram02.web.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/auth")
@Controller
public class AuthController {

    // 로그인
    @GetMapping("/signin")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model) {

        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "auth/signin";
    }


    @ResponseBody
    @GetMapping("")
    public Authentication auth(){

        return SecurityContextHolder.getContext().getAuthentication();
    }
}
