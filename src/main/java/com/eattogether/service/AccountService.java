package com.eattogether.service;

import com.eattogether.domain.Account;
import com.eattogether.controller.UserAccount;
import com.eattogether.dto.Profile;
import com.eattogether.dto.SignUpForm;
import com.eattogether.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account makeAccount(SignUpForm signUpForm){
        Account account = Account.builder()
                .nickname(signUpForm.getNickname())
                .email(signUpForm.getEmail())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .passwordRepeat(passwordEncoder.encode(signUpForm.getPasswordRepeat()))
                .eatCreatedByWeb(true)
                .build();

        Account newAccount = accountRepository.save(account);
        return newAccount;
    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {

        Account account=accountRepository.findByNickname(nickname);

        if(account==null){
            throw new UsernameNotFoundException(nickname); // 닉네임 에러
        }
        return new UserAccount(account);
    }

    public void profileUpdate(Account account, Profile profile){
        account.setAge(profile.getAge());
        account.setBio(profile.getBio());
        account.setMajor(profile.getMajor());
        account.setLocation(profile.getLocation());
        account.setProfileImage(profile.getProfileImage());
        accountRepository.save(account);
    }
}
