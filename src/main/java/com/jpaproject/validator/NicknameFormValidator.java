package com.jpaproject.validator;

import com.jpaproject.domain.Account;
import com.jpaproject.dto.NicknameForm;
import com.jpaproject.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class NicknameFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return NicknameForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        NicknameForm nicknameForm= (NicknameForm) target;
        Account byNickname = accountRepository.findByNickname(nicknameForm.getNickname());

        if(byNickname!=null){
            errors.rejectValue("nickname","wrong.value","이미 사용중인 닉네임입니다.");
        }

    }
}
