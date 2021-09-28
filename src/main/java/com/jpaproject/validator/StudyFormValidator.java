package com.jpaproject.validator;

import com.jpaproject.dto.StudyForm;
import com.jpaproject.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class StudyFormValidator implements Validator {

    private final StudyRepository studyRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return StudyForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        StudyForm studyForm =(StudyForm) target;

        if(studyRepository.existsByUrl(studyForm.getUrl())){
            errors.rejectValue("url","wrong.url",
                    "해당 스터디의 경로가 이미 사용중입니다.");
        }

    }
}
