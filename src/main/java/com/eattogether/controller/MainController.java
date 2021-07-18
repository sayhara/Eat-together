package com.eattogether.controller;

import com.eattogether.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class MainController {

    public String mainPage(@AuthUser Account account, Model model){
        if(account!=null){
            model.addAttribute(account);
        }

        return "index";

    }
}
