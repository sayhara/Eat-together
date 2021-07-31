package com.eattogether.controller;

import com.eattogether.domain.Account;
import com.eattogether.dto.PasswordForm;
import com.eattogether.dto.Profile;
import com.eattogether.repository.AccountRepository;
import com.eattogether.service.AccountService;
import com.eattogether.validator.PasswordFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final PasswordFormValidator passwordFormValidator;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(passwordFormValidator);
    }

    @GetMapping("/profile/{nickname}")
    public String ProfilePage(@AuthUser Account account,
                              @PathVariable String nickname, Model model){
        Account byNickname = accountRepository.findByNickname(nickname);

        if(byNickname==null){
            throw new IllegalArgumentException(nickname+"에 해당하는 사용자가 없습니다.");
        }

        model.addAttribute(byNickname);
        model.addAttribute("isOwner",byNickname.equals(account));
        return "account/profile";
    }

    @GetMapping("/settings/profile")
    public String profileSetting(@AuthUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new Profile(account));
        // model.addAttribute(String name, Object value)
        // model.addAttribute(Object value)
        return "settings/profile";
    }

    @PostMapping("/settings/profile")
    public String profileUpdate(@AuthUser Account account, Profile profile,
                                Errors errors, Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "settings/profile";
        }
        accountService.profileUpdate(account,profile);
        attributes.addFlashAttribute("message","프로필을 수정했습니다.");
        return "redirect:/settings/profile";
    }

    @GetMapping("/settings/password")
    public String passwordSettings(@AuthUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());
        return "settings/password";
    }

    @PostMapping("/settings/password")
    public String passwordUpdate(@AuthUser Account account, @Validated PasswordForm passwordForm,
                                 Errors errors, Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "settings/password";
        }
        accountService.passwordUpdate(account,passwordForm);
        attributes.addFlashAttribute("message","패스워드를 수정했습니다.");
        return "redirect:/settings/password";
    }
}
