package com.jpaproject.dto;

import com.jpaproject.domain.Study;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StudyUpdateEvent{

    private final Study study;

    private final String message;

}
