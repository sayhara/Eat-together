package com.eattogether.config;

import com.eattogether.domain.Account;
import com.eattogether.domain.Notification;
import com.eattogether.domain.Study;
import com.eattogether.dto.NotificationType;
import com.eattogether.dto.StudyCreatedEvent;
import com.eattogether.dto.StudyUpdateEvent;
import com.eattogether.repository.AccountRepository;
import com.eattogether.repository.NotificationRepository;
import com.eattogether.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Async
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyEventListener {

    private final StudyRepository studyRepository;
    private final AccountRepository accountRepository;
    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleStudyCreatedEvent(StudyCreatedEvent studyCreatedEvent){
        Study study = studyRepository.findStudyWithZonesById(
                studyCreatedEvent.getStudy().getId());

        Iterable<Account> accounts =
                accountRepository.findAll(AccountPredicates.findByZones(study.getZones()));

        accounts.forEach(account -> {
            if(account.isEatCreatedByWeb()){
                createNotification(study,account,study.getShort_note(),NotificationType.STUDY_CREATED);
            }
        });
    }

    @EventListener
    public void handleStudyUpdateEvent(StudyUpdateEvent studyUpdateEvent){

        Study study=studyRepository.findStudyWithManagersAndMembersById(studyUpdateEvent.getStudy().getId());
        Set<Account> accounts=new HashSet<>();
        accounts.addAll(study.getManagers());
        accounts.addAll(study.getManagers());

        accounts.forEach(account->{
            if(account.isEatUpdatedByWeb()){
                createNotification(study,account,study.getShort_note(),NotificationType.STUDY_UPDATED);
            }
        });
    }

    public void createNotification(Study study, Account account, String message,
                                   NotificationType notificationType){
        Notification notification=new Notification();
        notification.setTitle(study.getTitle());
        notification.setLink("/study/"+study.getUrl());
        notification.setChecked(false);
        notification.setCreatedDateTime(LocalDateTime.now());
        notification.setMessage(message);
        notification.setAccount(account);
        notification.setNotificationType(notificationType);
        notificationRepository.save(notification);
    }
}
