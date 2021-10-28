package com.s1dmlgus.instagram02.web.controller;


import org.springframework.stereotype.Controller;
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
    public String update(@PathVariable int id){

        return "user/update";
    }

}
