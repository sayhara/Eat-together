package com.jpaproject.dto;

import com.jpaproject.domain.Study;
import lombok.Getter;

@Getter
public class StudyCreatedEvent {

    private Study study;

    public StudyCreatedEvent(Study study) {
        this.study=study;

    }
}
