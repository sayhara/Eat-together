package com.eattogether.validator;

import com.eattogether.dto.SignUpForm;
import com.eattogether.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component // Bean으로 명시적으로 등록
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {      // 검증할 클래스
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        SignUpForm signUpForm= (SignUpForm) target;

        if(!signUpForm.getPassword().equals(signUpForm.getPasswordRepeat())){
            errors.rejectValue("password","invalid password",
                    "비밀번호가 서로 일치하지 않습니다.");
        }

        if(accountRepository.existsByNickname(signUpForm.getNickname())){
            errors.rejectValue("nickname","invalid nickname",
                    new Object[]{signUpForm.getNickname()},"이미 사용중인 아이디 입니다.");
        }

        if(accountRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invalid email",
                    new Object[]{signUpForm.getEmail()}, "이미 사용중인 이메일 입니다.");
        }
    }
}
