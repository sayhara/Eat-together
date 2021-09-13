package com.eattogether.controller;

import com.eattogether.config.AuthUser;
import com.eattogether.domain.Account;
import com.eattogether.domain.Study;
import com.eattogether.repository.NotificationRepository;
import com.eattogether.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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
