package com.eattogether.controller;

import com.eattogether.config.AuthUser;
import com.eattogether.domain.Account;
import com.eattogether.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/")
    public String mainPage(@AuthUser Account account, Model model){

        if(account!=null){
            model.addAttribute(account);
        }

        long count=notificationRepository.countByAccountAndChecked(account,false);
        model.addAttribute("hasNotification",count>0);

        return "main";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
