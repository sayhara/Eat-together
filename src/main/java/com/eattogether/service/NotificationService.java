package com.eattogether.service;

import com.eattogether.domain.Notification;
import com.eattogether.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void markAsRead(List<Notification> notifications) {
        notifications.forEach(s->s.setChecked(true));
        notificationRepository.saveAll(notifications);
    }
}
