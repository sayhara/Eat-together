package com.eattogether.repository;

import com.eattogether.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    boolean existsByUrl(String url);

    Meeting findByUrl(String url);
}
