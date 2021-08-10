package com.eattogether.controller;

import com.eattogether.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MeetingController {

    @GetMapping("/new_meeting")
    public String newMeeting(@AuthUser Account account, Model model){
        model.addAttribute(account);
        //model.addAttribute(new MeetingForm());
        return "meeting/form";
    }
}
