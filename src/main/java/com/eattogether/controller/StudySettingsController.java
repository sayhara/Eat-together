package com.eattogether.controller;

import com.eattogether.config.AuthUser;
import com.eattogether.domain.Account;
import com.eattogether.domain.Study;
import com.eattogether.dto.StudyDescriptionForm;
import com.eattogether.repository.StudyRepository;
import com.eattogether.service.StudyService;
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
@RequestMapping("/study/{url}/settings")
@RequiredArgsConstructor
public class StudySettingsController {

    private final StudyService studyService;
    private final ModelMapper modelMapper;
    private final StudyRepository studyRepository;

    @GetMapping("/description")
    public String viewStudySetting(@AuthUser Account account, @PathVariable String url, Model model)
            throws AccessDeniedException {

        Study study = studyService.getStudyUpdate(account, url);

        model.addAttribute(account);
        model.addAttribute(study);
        model.addAttribute(modelMapper.map(study, StudyDescriptionForm.class));

        return "study/settings/description";

    }

    @PostMapping("/description")
    public String updateStudyinfo(@AuthUser Account account, @PathVariable String url,
                                    @Valid StudyDescriptionForm descriptionForm, Errors errors,
                                    Model model, RedirectAttributes attributes) throws AccessDeniedException {

        Study study = studyService.getStudyUpdate(account, url);

        if(errors.hasErrors()){
            model.addAttribute(account);
            model.addAttribute(study);
            return "study/settings/description";
        }

        studyService.updateStudyDescription(study,descriptionForm);
        attributes.addFlashAttribute("message","스터디 소개를 수정했습니다.");
        return "redirect:/study/"+url+"/settings/description";
    }

    @GetMapping("/banner")
    public String viewStudyBanner(@AuthUser Account account,
                                    @PathVariable String url, Model model) throws AccessDeniedException {
        Study study = studyService.getStudyUpdate(account, url);

        model.addAttribute(account);
        model.addAttribute(study);
        return "study/settings/banner";
    }

    @PostMapping("/banner")
    public String updateStudyBanner(@AuthUser Account account,
                                      @PathVariable String url, Model model, String image,
                                      Errors errors, RedirectAttributes attributes) throws AccessDeniedException {
        Study study = studyService.getStudyUpdate(account, url);

        if(errors.hasErrors()){
            model.addAttribute(account);
            model.addAttribute(study);
            return "study/settings/banner";
        }

        studyService.updateStudyImage(study,image);
        attributes.addFlashAttribute("message","스터디 배너 이미지를 수정했습니다.");
        return "redirect:/study/"+url+"/settings/banner";
    }

    @PostMapping("/banner/enable")
    public String enableStudyBanner(@AuthUser Account account,
                                      @PathVariable String url) throws AccessDeniedException {

        Study study = studyService.getStudyUpdate(account, url);
        studyService.enableStudyBanner(study);
        return "redirect:/study/"+url+"/settings/banner";
    }

    @PostMapping("/banner/disable")
    public String disableStudyBanner(@AuthUser Account account,
                                       @PathVariable String url) throws AccessDeniedException {

        Study study = studyService.getStudyUpdate(account, url);
        studyService.disableStudyBanner(study);
        return "redirect:/study/"+url+"/settings/banner";
    }

    @GetMapping("/study")
    public String viewStudy(@AuthUser Account account,
                              @PathVariable String url, Model model) throws AccessDeniedException {

        Study study = studyService.getStudyUpdate(account, url);
        model.addAttribute(account);
        model.addAttribute(study);

        return "study/settings/study";
    }

    @PostMapping("/study/publish")
    public String publicStudy(@AuthUser Account account, @PathVariable String url,
                                RedirectAttributes attributes) throws AccessDeniedException {
        Study study = studyService.getStudyUpdateStatus(account, url);
        studyService.publish(study);
        attributes.addFlashAttribute("message","스터디를 공개했습니다.");
        return "redirect:/study/"+url+"/settings/study";
    }

    @PostMapping("/study/close")
    public String closeStudy(@AuthUser Account account, @PathVariable String url, RedirectAttributes attributes) throws AccessDeniedException {
        Study study = studyService.getStudyUpdateStatus(account, url);
        studyService.close(study);
        attributes.addFlashAttribute("message","스터디를 종료했습니다.");
        return "redirect:/study/"+url+"/settings/study";
    }

    @PostMapping("/recruit/start")
    public String startRecruit(@AuthUser Account account, @PathVariable String url,
                               RedirectAttributes attributes) throws AccessDeniedException {
        Study study = studyService.getStudyUpdateStatus(account, url);

        studyService.startRecruit(study);
        attributes.addFlashAttribute("message","인원 모집을 시작합니다.");
        return "redirect:/study/"+url+"/settings/study";
    }

    @PostMapping("/recruit/stop")
    public String stopRecruit(@AuthUser Account account, @PathVariable String url,
                              RedirectAttributes attributes) throws AccessDeniedException {

        Study study = studyService.getStudyUpdateStatus(account, url);

        studyService.stopRecruit(study);
        attributes.addFlashAttribute("message","인원 모집을 종료합니다.");
        return "redirect:/study/"+url+"/settings/study";
    }

    @PostMapping("/study/url")
    public String updateStudyUrl(@AuthUser Account account, @PathVariable String url,
                                    @RequestParam String newUrl, Model model, RedirectAttributes attributes) throws AccessDeniedException {

        Study study = studyService.getStudyUpdateStatus(account, url);

        if(!studyService.isValidUrl(newUrl)){
            model.addAttribute(account);
            model.addAttribute(study);
            model.addAttribute("studyUrlError","해당 스터디 경로는 사용할 수 없습니다.");
            return "study/settings/study";
        }

        studyService.updateStudyUrl(study,newUrl);
        attributes.addFlashAttribute("message","스터디 경로를 수정했습니다.");
        return "redirect:/study/"+newUrl+"/settings/study";
    }

    @PostMapping("/study/title")
    public String updateStudyTitle(@AuthUser Account account, @PathVariable String url,
                                     @RequestParam String newTitle, Model model, RedirectAttributes attributes) throws AccessDeniedException {

        Study study = studyService.getStudyUpdateStatus(account, url);
        if(!studyService.isValidTitle(newTitle)){
            model.addAttribute(account);
            model.addAttribute(study);
            model.addAttribute("studyTitleError","스터디 이름을 다시 입력하세요.");
            return "study/settings/study";
        }
        studyService.updateStudyTitle(study,newTitle);
        attributes.addFlashAttribute("message","스터디 이름을 수정했습니다.");
        return "redirect:/study/"+url+"/settings/study";

    }

    @PostMapping("/study/remove")
    public String removeStudy(@AuthUser Account account,
                                @PathVariable String url, Model model) throws AccessDeniedException {
        Study study = studyService.getStudyUpdateStatus(account, url);
        studyService.remove(study);
        return "redirect:/";
    }

}