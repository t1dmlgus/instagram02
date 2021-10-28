package com.s1dmlgus.instagram02.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class BoardController {

    @GetMapping("/")
    public String index() {

        return "board/story";
    }

    @GetMapping("/board/popular")
    public String popular(){

        return "board/popular";
    }

    @GetMapping("/board/upload")
    public String upload(){

        return "board/upload";
    }

}
