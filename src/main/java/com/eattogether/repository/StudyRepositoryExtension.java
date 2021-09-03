package com.eattogether.repository;

import com.eattogether.domain.Study;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface StudyRepositoryExtension {

    List<Study> findByKeyWord(String keyword);
}
