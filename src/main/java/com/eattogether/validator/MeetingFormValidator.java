package com.eattogether.validator;

import com.eattogether.dto.MeetingForm;
import com.eattogether.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MeetingFormValidator implements Validator {

    private final MeetingRepository meetingRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return MeetingForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        MeetingForm meetingForm=(MeetingForm) target;

        if(meetingRepository.existsByUrl(meetingForm.getUrl())){
            errors.rejectValue("url","wrong.url",
                    "해당 모임경로가 이미 사용중입니다.");
        }

    }
}
