package com.jpaproject.controller;

import com.jpaproject.config.AuthUser;
import com.jpaproject.domain.Account;
import com.jpaproject.domain.Enrollment;
import com.jpaproject.domain.Event;
import com.jpaproject.domain.Study;
import com.jpaproject.dto.EventForm;
import com.jpaproject.repository.EnrollmentRepository;
import com.jpaproject.repository.EventRepository;
import com.jpaproject.service.EventService;
import com.jpaproject.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/study/{url}")
@RequiredArgsConstructor
public class EventController {

    private final StudyService studyService;
    private final EventService eventService;
    private final ModelMapper modelMapper;
    private final EventRepository eventRepository;
    private final EnrollmentRepository enrollmentRepository;

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

    @GetMapping("/events")
    public String getEvents(@AuthUser Account account, @PathVariable String url,
                            Model model){
        Study study = studyService.getStudy(url);
        model.addAttribute(account);
        model.addAttribute(study);

        List<Event> events=eventRepository.findByStudyOrderByStartDateTime(study); // 시작시간으로 찾기
        List<Event> newEvents=new ArrayList<>();
        List<Event> oldEvents=new ArrayList<>();

        events.forEach(e->{
            if(e.getEndDateTime().isBefore(LocalDateTime.now())){
                oldEvents.add(e);
            } else{
                newEvents.add(e);
            }
        });

        model.addAttribute("newEvents",newEvents);
        model.addAttribute("oldEvents",oldEvents);

        return "study/events";
    }

    @GetMapping("/events/{id}/edit")
    public String updateEventForm(@AuthUser Account account, @PathVariable String url,
                                  @PathVariable Long id, Model model)
            throws AccessDeniedException {

        Study study = studyService.getStudyUpdate(account, url);
        Event event = eventRepository.findById(id).orElseThrow();
        model.addAttribute(study);
        model.addAttribute(account);
        model.addAttribute(event);
        model.addAttribute(modelMapper.map(event,EventForm.class));

        return "event/update-form";
    }

    @PostMapping("/events/{id}/edit")
    public String updateEventSubmit(@AuthUser Account account, @PathVariable String url,
                                    @PathVariable Long id, EventForm eventForm, Errors errors,
                                    Model model) throws AccessDeniedException {
        Study study = studyService.getStudyUpdate(account, url);
        Event event = eventRepository.findById(id).orElseThrow();
        eventForm.setEventType(event.getEventType());

        if(eventForm.getLimitOfEnrollments()<event.getNumberOfAcceptedEnrollments()){
            errors.rejectValue("limitOfEnrollments","wrong.value",
                    "확인된 참가 신청보다 모집 인원수가 커야 합니다.");
        }

        if(errors.hasErrors()){
            model.addAttribute(account);
            model.addAttribute(study);
            model.addAttribute(event);
            return "event/update-form";
        }

        eventService.updateEvent(event,eventForm);
        return "redirect:/study/"+study.getUrl()+"/events/"+event.getId();
    }

    @PostMapping("/events/{id}/delete")
    public String cancelEvent(@AuthUser Account account, @PathVariable String url,
                              @PathVariable Long id) throws AccessDeniedException {

        Study study = studyService.getStudyUpdateStatus(account, url);
        eventService.deleteEvent(eventRepository.findById(id).orElseThrow());
        return "redirect:/study/"+study.getUrl()+"/events";
    }

    @PostMapping("/events/{id}/enroll")
    public String newEnrollment(@AuthUser Account account, @PathVariable String url,
                                @PathVariable Long id){
        Study study = studyService.getStudyToEnroll(url);
        eventService.newEnrollment(eventRepository.findById(id).orElseThrow(),account);
        return "redirect:/study/"+study.getUrl()+"/events/"+id;
    }

    @PostMapping("/events/{id}/disenroll")
    public String cancelEnrollment(@AuthUser Account account, @PathVariable String url,
                                   @PathVariable Long id){
        Study study = studyService.getStudyToEnroll(url);
        eventService.cancelEnrollment(eventRepository.findById(id).orElseThrow(),account);
        return "redirect:/study/"+study.getUrl()+"/events/"+id;
    }

    @GetMapping("/events/{eventId}/enrollments/{enrollmentId}/accept")
    public String acceptEnrollment(@AuthUser Account account, @PathVariable String url,
                                   @PathVariable("eventId") Event event,
                                   @PathVariable("enrollmentId") Enrollment enrollment)
            throws AccessDeniedException {
        Study study = studyService.getStudyUpdate(account, url);
        eventService.acceptEnrollment(event,enrollment);
        return "redirect:/study/"+study.getUrl()+"/events/"+event.getId();
    }

    @GetMapping("/events/{eventId}/enrollments/{enrollmentId}/reject")
    public String rejectEnrollment(@AuthUser Account account, @PathVariable String url,
                                   @PathVariable("eventId") Event event,
                                   @PathVariable("enrollmentId") Enrollment enrollment)
            throws AccessDeniedException {
        Study study = studyService.getStudyUpdate(account, url);
        eventService.rejectEnrollment(event,enrollment);
        return "redirect:/study/"+study.getUrl()+"/events/"+event.getId();
    }

    @GetMapping("/events/{eventId}/enrollments/{enrollmentId}/checkin")
    public String checkInEnrollment(@AuthUser Account account, @PathVariable String url,
                                    @PathVariable("eventId") Event event,
                                    @PathVariable("enrollmentId") Enrollment enrollment)
            throws AccessDeniedException {
        Study study = studyService.getStudyUpdate(account, url);
        eventService.checkInEnrollment(enrollment);
        return "redirect:/study/" + study.getUrl() + "/events/" + event.getId();
    }

    @GetMapping("/events/{eventId}/enrollments/{enrollmentId}/cancel-checkin")
    public String cancelCheckInEnrollment(@AuthUser Account account, @PathVariable String url,
                                          @PathVariable("eventId") Event event,
                                          @PathVariable("enrollmentId") Enrollment enrollment)
            throws AccessDeniedException {
        Study study = studyService.getStudyUpdate(account, url);
        eventService.cancelCheckInEnrollment(enrollment);
        return "redirect:/study/" + study.getUrl() + "/events/" + event.getId();
    }
}
