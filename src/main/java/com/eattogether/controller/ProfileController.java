package com.eattogether.controller;

import com.eattogether.domain.Account;
import com.eattogether.dto.Profile;
import com.eattogether.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final AccountRepository accountRepository;

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
}
