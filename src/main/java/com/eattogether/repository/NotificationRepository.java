package com.eattogether.repository;

import com.eattogether.domain.Account;
import com.eattogether.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    long countByAccountAndChecked(Account account, boolean b);
}
