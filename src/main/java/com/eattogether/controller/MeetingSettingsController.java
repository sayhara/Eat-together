package com.eattogether.controller;

import com.eattogether.config.AuthUser;
import com.eattogether.domain.Account;
import com.eattogether.domain.Meeting;
import com.eattogether.dto.MeetingDescriptionForm;
import com.eattogether.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;

@Controller
@RequestMapping("/meeting/{url}/settings")
@RequiredArgsConstructor
public class MeetingSettingsController {

    private final MeetingService meetingService;

    @GetMapping("/description")
    public String viewMeetingSetting(@AuthUser Account account, @PathVariable String url, Model model,
                                     @Valid MeetingDescriptionForm descriptionForm)
            throws AccessDeniedException {

        Meeting meeting = meetingService.getMeetingUpdate(account, url);
        Meeting meetingDescriptionForm = meetingService.createMeetingDescriptionForm(descriptionForm);

        model.addAttribute(account);
        model.addAttribute(meeting);
        model.addAttribute(meetingDescriptionForm);

        return "meeting/settings/description";

    }

    @PostMapping("/description")
    public String updateMeetinginfo(@AuthUser Account account, @PathVariable String url,
                                    @Valid MeetingDescriptionForm descriptionForm, Errors errors,
                                    Model model, RedirectAttributes attributes) throws AccessDeniedException {

        Meeting meeting = meetingService.getMeetingUpdate(account, url);

        if(errors.hasErrors()){
            model.addAttribute(account);
            model.addAttribute(meeting);
            return "meeting/settings/description";
        }

        meetingService.updateMeetingDescription(meeting,descriptionForm);
        attributes.addFlashAttribute("message","모임 소개를 수정했습니다.");
        return "redirect:/meeting/"+url+"/settings/description";
    }
}
