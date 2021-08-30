package com.eattogether.controller;

import com.eattogether.config.AuthUser;
import com.eattogether.domain.Account;
import com.eattogether.domain.Event;
import com.eattogether.domain.Study;
import com.eattogether.dto.EventForm;
import com.eattogether.repository.EventRepository;
import com.eattogether.service.EventService;
import com.eattogether.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;

@Controller
@RequestMapping("/study/{url}")
@RequiredArgsConstructor
public class EventController {

    private final StudyService studyService;
    private final EventService eventService;
    private final ModelMapper modelMapper;
    private final EventRepository eventRepository;

    @GetMapping("/new-event")
    public String newEventForm(@AuthUser Account account,
                               @PathVariable String url, Model model) throws AccessDeniedException {

        Study study = studyService.getStudyUpdateStatus(account, url);

        model.addAttribute(account);
        model.addAttribute(study);
        model.addAttribute(new EventForm());
        return "event/form";
    }

    @PostMapping("/new-event")
    public String newEventSubmit(@AuthUser Account account, @PathVariable String url,
                                 @Valid EventForm eventForm, Errors errors, Model model) throws AccessDeniedException {
        Study study = studyService.getStudyUpdateStatus(account, url);

        if(errors.hasErrors()){
            model.addAttribute(account);
            model.addAttribute(study);
            return "event/form";
        }

        Event event = eventService.createEvent(modelMapper.map(eventForm, Event.class), study, account);
        return "redirect:/study/"+study.getUrl()+"/events/"+event.getId();
    }

    @GetMapping("/events/{id}")
    public String getEvent(@AuthUser Account account, @PathVariable String url,
                           @PathVariable Long id, Model model){
        model.addAttribute(account);
        model.addAttribute(eventRepository.findById(id).orElseThrow());
        model.addAttribute(studyService.getStudy(url));
        return "event/view";
    }

}
