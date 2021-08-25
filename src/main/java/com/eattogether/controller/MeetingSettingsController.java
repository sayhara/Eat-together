package com.eattogether.controller;

import com.eattogether.config.AuthUser;
import com.eattogether.domain.Account;
import com.eattogether.domain.Meeting;
import com.eattogether.dto.MeetingDescriptionForm;
import com.eattogether.repository.MeetingRepository;
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
    private final MeetingRepository meetingRepository;

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

    @GetMapping("/meeting")
    public String viewMeeting(@AuthUser Account account,
                              @PathVariable String url, Model model) throws AccessDeniedException {

        Meeting meeting = meetingService.getMeetingUpdate(account, url);
        model.addAttribute(account);
        model.addAttribute(meeting);

        return "meeting/settings/meeting";
    }

    @PostMapping("/meeting/publish")
    public String publicMeeting(@AuthUser Account account, @PathVariable String url,
                                RedirectAttributes attributes) throws AccessDeniedException {
        Meeting meeting = meetingService.getMeetingUpdateStatus(account, url);
        meetingService.publish(meeting);
        attributes.addFlashAttribute("message","모임을 공개했습니다.");
        return "redirect:/meeting/"+url+"/settings/meeting";
    }

    @PostMapping("/meeting/close")
    public String closeMeeting(@AuthUser Account account, @PathVariable String url, RedirectAttributes attributes) throws AccessDeniedException {
        Meeting meeting = meetingService.getMeetingUpdateStatus(account, url);
        meetingService.close(meeting);
        attributes.addFlashAttribute("message","모임을 종료했습니다.");
        return "redirect:/meeting/"+url+"/settings/meeting";
    }

    @PostMapping("/recruit/start")
    public String startRecruit(@AuthUser Account account, @PathVariable String url,
                               RedirectAttributes attributes) throws AccessDeniedException {
        Meeting meeting = meetingService.getMeetingUpdateStatus(account, url);

        meetingService.startRecruit(meeting);
        attributes.addFlashAttribute("message","인원 모집을 시작합니다.");
        return "redirect:/meeting/"+url+"/settings/meeting";
    }

    @PostMapping("/recruit/stop")
    public String stopRecruit(@AuthUser Account account, @PathVariable String url,
                              RedirectAttributes attributes) throws AccessDeniedException {

        Meeting meeting = meetingService.getMeetingUpdateStatus(account, url);

        meetingService.stopRecruit(meeting);
        attributes.addFlashAttribute("message","인원 모집을 종료합니다.");
        return "redirect:/meeting/"+url+"/settings/meeting";
    }

    @PostMapping("/meeting/path")
    public String updateMeetingPath(@AuthUser Account account, @PathVariable String url,
                                    @RequestParam String newUrl, Model model, RedirectAttributes attributes) throws AccessDeniedException {

        Meeting meeting = meetingService.getMeetingUpdateStatus(account, url);

        if(!meetingService.isValidUrl(newUrl)){
            model.addAttribute(account);
            model.addAttribute(meeting);
            model.addAttribute("meetingUrlError","해당 모임 경로는 사용할 수 없습니다.");
            return "meeting/settings/meeting";
        }

        meetingService.updateMeetingUrl(meeting,newUrl);
        attributes.addFlashAttribute("message","모임 경로를 수정했습니다.");
        return "redirect:/meeting/"+newUrl+"/settings/meeting";
    }

    @PostMapping("/meeting/title")
    public String updateMeetingTitle(@AuthUser Account account, @PathVariable String url,
                                     @RequestParam String newTitle, Model model, RedirectAttributes attributes) throws AccessDeniedException {

        Meeting meeting = meetingService.getMeetingUpdateStatus(account, url);
        if(!meetingService.isValidTitle(newTitle)){
            model.addAttribute(account);
            model.addAttribute(meeting);
            model.addAttribute("meetingTitleError","모임 이름을 다시 입력하세요.");
            return "meeting/settings/meeting";
        }
        meetingService.updateMeetingTitle(meeting,newTitle);
        attributes.addFlashAttribute("message","모임 이름을 수정했습니다.");
        return "redirect:/meeting/"+url+"/settings/meeting";

    }

    @PostMapping("/meeting/remove")
    public String removeMeeting(@AuthUser Account account,
                                @PathVariable String url, Model model) throws AccessDeniedException {
        Meeting meeting = meetingService.getMeetingUpdateStatus(account, url);
        meetingService.remove(meeting);
        return "redirect:/";
    }

    @GetMapping("/meeting/{url}/join")
    public String joinMeeting(@AuthUser Account account, @PathVariable String url){

        Meeting meeting = meetingRepository.findByUrl(url);
        meetingService.addMember(meeting,account);
        return "redirect:/meeting/"+meeting.getUrl()+"/members";
    }

    @GetMapping("/meeting/{url}/leave")
    public String leaveMeeting(@AuthUser Account account, @PathVariable String url){

        Meeting meeting = meetingRepository.findByUrl(url);
        meetingService.removeMember(meeting,account);
        return "redirect:/meeting/"+meeting.getUrl()+"/members";
    }

}