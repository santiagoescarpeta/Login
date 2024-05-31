package com.spring.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(){
        return "redirect:http://localhost:4200/login";
    }
    @PostMapping("/logout")
    public String login1(){
        return "redirect:http://localhost:4200/login";
    }
    @GetMapping("/403")
    public String forbidden(){
        return "403";
    }
}
