package com.eattogether.controller;

import com.eattogether.config.AuthUser;
import com.eattogether.domain.Account;
import com.eattogether.domain.Meeting;
import com.eattogether.dto.MeetingDescriptionForm;
import com.eattogether.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;

@Controller
@RequestMapping("/meeting/{url}/settings")
@RequiredArgsConstructor
public class MeetingSettingsController {

    private final MeetingService meetingService;
    private final ModelMapper modelMapper;

    @GetMapping("/description")
    public String viewMeetingSetting(@AuthUser Account account, @PathVariable String url, Model model)
            throws AccessDeniedException {

        Meeting meeting = meetingService.getMeetingUpdate(account, url);

        model.addAttribute(account);
        model.addAttribute(meeting);
        model.addAttribute(modelMapper.map(meeting,MeetingDescriptionForm.class));

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

    @GetMapping("/banner")
    public String viewMeetingBanner(@AuthUser Account account,
                                    @PathVariable String url, Model model) throws AccessDeniedException {
        Meeting meeting = meetingService.getMeetingUpdate(account, url);

        model.addAttribute(account);
        model.addAttribute(meeting);
        return "meeting/settings/banner";
    }

    @PostMapping("/banner")
    public String updateMeetingBanner(@AuthUser Account account,
                                      @PathVariable String url, Model model, String image,
                                      Errors errors, RedirectAttributes attributes) throws AccessDeniedException {
        Meeting meeting = meetingService.getMeetingUpdate(account, url);

        if(errors.hasErrors()){
            model.addAttribute(account);
            model.addAttribute(meeting);
            return "meeting/settings/banner";
        }

        meetingService.updateMeetingImage(meeting,image);
        attributes.addFlashAttribute("message","모임 배너이미지를 수정했습니다.");
        return "redirect:/meeting/"+url+"/settings/banner";
    }

    @PostMapping("/banner/enable")
    public String enableMeetingBanner(@AuthUser Account account,
                                      @PathVariable String url) throws AccessDeniedException {

        Meeting meeting = meetingService.getMeetingUpdate(account, url);
        meetingService.enableMeetingBanner(meeting);
        return "redirect:/meeting/"+url+"/settings/banner";
    }

    @PostMapping("/banner/disable")
    public String disableMeetingBanner(@AuthUser Account account,
                                       @PathVariable String url) throws AccessDeniedException {

        Meeting meeting = meetingService.getMeetingUpdate(account, url);
        meetingService.disableMeetingBanner(meeting);
        return "redirect:/meeting/"+url+"/settings/banner";
    }
}
