package com.eattogether.repository;

import com.eattogether.domain.Event;
import com.eattogether.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStudyOrderByStartDateTime(Study study);
}
