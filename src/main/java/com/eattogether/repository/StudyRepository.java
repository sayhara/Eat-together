package com.eattogether.repository;

import com.eattogether.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface StudyRepository extends JpaRepository<Study, Long>{

    boolean existsByUrl(String url);

    Study findByUrl(String url);

    Study findStudyOnlyByUrl(String url);

    Study findStudyWithZonesById(Long id);

    Study findStudyWithManagersAndMembersById(Long id);
}
