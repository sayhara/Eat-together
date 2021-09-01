package com.eattogether.service;

import com.eattogether.domain.Account;
import com.eattogether.domain.Enrollment;
import com.eattogether.domain.Event;
import com.eattogether.domain.Study;
import com.eattogether.dto.EventForm;
import com.eattogether.repository.EnrollmentRepository;
import com.eattogether.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EnrollmentRepository enrollmentRepository;

    public Event createEvent(Event event, Study study, Account account){
        event.setCreatedBy(account); // 이벤트를 모집하는 사람
        event.setCreatedDateTime(LocalDateTime.now());
        event.setStudy(study);
        return eventRepository.save(event);
    }

    public void updateEvent(Event event, EventForm eventForm) {
        modelMapper.map(eventForm,event);
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
    }

    public void newEnrollment(Event event, Account account) {
        if(!enrollmentRepository.existsByEventAndAccount(event,account)){
            Enrollment enrollment=new Enrollment();
            enrollment.setEnrolledAt(LocalDateTime.now());
            enrollment.setAccepted(event.isAbleToAcceptWaitingEnrollment());
            enrollment.setAccount(account);
            event.addEnrollment(enrollment);
            enrollmentRepository.save(enrollment);
        }
    }

    public void cancelEnrollment(Event event, Account account) {
        Enrollment enrollment=enrollmentRepository.findByEventAndAccount(event,account);
        event.removeEnrollment(enrollment);
        enrollmentRepository.delete(enrollment);
        event.acceptTheNextWaitingEnrollment();
    }
}
