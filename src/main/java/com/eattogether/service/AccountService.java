package com.eattogether.service;

import com.eattogether.domain.Account;
import com.eattogether.controller.UserAccount;
import com.eattogether.domain.Zone;
import com.eattogether.dto.*;
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
import java.util.Optional;
import java.util.Set;

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

    public void passwordUpdate(Account account, PasswordForm passwordForm) {
        account.setPassword(passwordEncoder.encode(passwordForm.getNewPassword()));
        accountRepository.save(account);
    }

    public void alarmUpdate(Account account, Alarm alarm) {
        account.setEatCreatedByWeb(alarm.isEatCreatedByWeb());
        account.setEatEnrollmentResultByWeb(alarm.isEatEnrollmentResultByWeb());
        account.setEatUpdatedByWeb(alarm.isEatUpdatedByWeb());
        accountRepository.save(account);
    }

    public void nicknameUpdate(Account account, NicknameForm nicknameForm) {
        account.setNickname(nicknameForm.getNickname());
        accountRepository.save(account);
    }

    public Set<Zone> getZones(Account account){
        Optional<Account> byId = accountRepository.findById(account.getId());
        return byId.orElseThrow().getZones();
    }

    public void addZone(Account account, Zone zone){
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a->a.getZones().add(zone));
    }

    public void removeZone(Account account, Zone zone){
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a->a.getZones().remove(zone));
    }

}
