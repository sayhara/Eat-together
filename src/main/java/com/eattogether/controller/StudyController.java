package com.eattogether.controller;

import com.eattogether.config.AuthUser;
import com.eattogether.domain.Account;
import com.eattogether.domain.Study;
import com.eattogether.dto.StudyForm;
import com.eattogether.repository.StudyRepository;
import com.eattogether.service.StudyService;
import com.eattogether.validator.StudyFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;
    private final StudyFormValidator studyFormValidator;
    private final StudyRepository studyRepository;
    private final ModelMapper modelMapper;

    @InitBinder("studyForm")
    public void studyFormValidator(WebDataBinder webDataBinder){
        webDataBinder.addValidators(studyFormValidator);
    }

    @GetMapping("/new-study")
    public String netStudyForm(@AuthUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new StudyForm());
        return "study/form";
    }

    @PostMapping("/new-study")
    public String newStudySubmit(@AuthUser Account account, @Valid StudyForm studyForm,
                                 Errors errors, Model model){

        if(errors.hasErrors()){
            model.addAttribute(account);
            return "study/form";
        }

        Study newStudy = studyService.createNewStudy(modelMapper.map(studyForm, Study.class), account);
        return "redirect:/study/"+ newStudy.getUrl();
    }

    @GetMapping("/study/{url}")
    public String viewStudy(@AuthUser Account account, @PathVariable String url,
                                  Model model){
        Study study = studyService.getStudy(url);
        model.addAttribute(account);
        model.addAttribute(study);

        return "study/view";
    }

    @GetMapping("/study/{url}/members")
    public String viewStudyMembers(@AuthUser Account account,
                                     @PathVariable String url, Model model){

        Study study = studyService.getStudy(url);
        model.addAttribute(study);
        model.addAttribute(account);
        model.addAttribute(studyRepository.findByUrl(url));
        return "study/members";
    }


    @GetMapping("/study/{url}/join")
    public String joinStudy(@AuthUser Account account, @PathVariable String url){

        Study study = studyRepository.findByUrl(url);
        studyService.addMember(study,account);
        return "redirect:/study/"+ study.getUrl()+"/members";
    }

    @GetMapping("/study/{url}/leave")
    public String leaveStudy(@AuthUser Account account, @PathVariable String url){

        Study study = studyRepository.findByUrl(url);
        studyService.removeMember(study,account);
        return "redirect:/study/"+ study.getUrl()+"/members";
    }

    @GetMapping("/search/study")
    public String searchStudy(String keyword, Model model,
                              @PageableDefault(size = 9,sort = "startTime",direction = Sort.Direction.ASC)
                                      Pageable pageable){
        Page<Study> studyPage = studyRepository.findByKeyword(keyword, pageable);
        model.addAttribute("studyPage",studyPage);
        model.addAttribute("keyword",keyword);
        model.addAttribute("sortProperty",
                pageable.getSort().toString().contains("startTime")?
                "startTime":"memberCount");
        return "search";
    }

    @GetMapping("/study/data")
    public String generateTestData(@AuthUser Account account){
        studyService.generateTestStudies(account);
        return "redirect:/";
    }


}
