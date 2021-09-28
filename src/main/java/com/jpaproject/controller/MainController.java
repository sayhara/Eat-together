package com.jpaproject.controller;

import com.jpaproject.config.AuthUser;
import com.jpaproject.domain.Account;
import com.jpaproject.repository.NotificationRepository;
import com.jpaproject.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final NotificationRepository notificationRepository;
    private final StudyRepository studyRepository;

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
