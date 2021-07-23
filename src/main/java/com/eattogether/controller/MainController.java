package com.eattogether.controller;

import com.eattogether.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(@AuthUser Account account, Model model){

        if(account!=null){
            model.addAttribute(account);
        }

        return "main";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
