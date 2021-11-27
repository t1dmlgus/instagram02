package com.s1dmlgus.instagram02.web.controller;


import org.apache.coyote.Request;
import org.dom4j.rule.Mode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
