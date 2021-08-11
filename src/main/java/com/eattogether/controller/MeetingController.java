package com.eattogether.controller;

import com.eattogether.config.AuthUser;
import com.eattogether.domain.Account;
import com.eattogether.domain.Meeting;
import com.eattogether.dto.MeetingForm;
import com.eattogether.service.MeetingService;
import com.eattogether.validator.MeetingFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final MeetingFormValidator meetingFormValidator;

    @InitBinder("meetingForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(meetingFormValidator);
    }

    @GetMapping("/new_meeting")
    public String newMeetingGet(@AuthUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new MeetingForm());
        return "meeting/form";
    }

    @PostMapping("/new_meeting")
    public String newMeetingPost(@AuthUser Account account, @Valid MeetingForm meetingForm,
                                 Errors errors,Model model){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "meeting/form";
        }

        Meeting newMeeting = meetingService.createNewMeeting(meetingForm, account);
        return "redirect:/meeting/"+newMeeting.getUrl();
    }
}
