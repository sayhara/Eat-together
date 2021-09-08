package com.eattogether.dto;

import com.eattogether.domain.Study;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;

@Getter
@RequiredArgsConstructor
public class StudyUpdateEvent{

    private final Study study;

    private final String message;

}
