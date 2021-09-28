package com.jpaproject.validator;

import com.jpaproject.dto.EventForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
public class EventValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EventForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EventForm eventForm= (EventForm) target;

        if(eventForm.getEndEnrollmentDateTime().isBefore(LocalDateTime.now())) {
            errors.rejectValue("endEnrollmentDateTime", "wrong.datetime",
                    "등록 마감 일시가 현재 시간보다 이전입니다.");
        }

        if(eventForm.getEndDateTime().isBefore(eventForm.getStartDateTime())
            || eventForm.getEndDateTime().isBefore(eventForm.getEndEnrollmentDateTime())){
            errors.rejectValue("endDateTime","wrong.datetime",
                    "모임 종료 시간이 모임 시작시간이나 등록 마감시간보다 이전입니다.");
        }

        if(eventForm.getStartDateTime().isBefore(eventForm.getEndEnrollmentDateTime())){
            errors.rejectValue("endDateTime","wrong.datetime",
                    "모임 시작시간이 등록 마감시간보다 이전입니다.");
        }
    }
}
