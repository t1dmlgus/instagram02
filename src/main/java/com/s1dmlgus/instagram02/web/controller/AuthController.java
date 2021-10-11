package com.s1dmlgus.instagram02.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
@Controller
public class AuthController {

    // 회원가입
    @GetMapping("/signup")
    public String join(){
        return "auth/signup";
    }

    // 로그인
    @GetMapping("/signin")
    public String login(){
        return "auth/signin";
    }

}
