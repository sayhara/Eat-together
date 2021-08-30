package com.eattogether.service;

import com.eattogether.domain.Account;
import com.eattogether.domain.Event;
import com.eattogether.domain.Study;
import com.eattogether.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event createEvent(Event event, Study study, Account account){
        event.setCreatedBy(account); // 이벤트를 모집하는 사람
        event.setCreatedDateTime(LocalDateTime.now());
        event.setStudy(study);
        return eventRepository.save(event);
    }
}
