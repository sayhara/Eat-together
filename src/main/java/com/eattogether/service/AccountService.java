package com.eattogether.service;

import com.eattogether.domain.Account;
import com.eattogether.domain.UserAccount;
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

    @Transactional
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
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {

        Account account=accountRepository.findByEmail(emailOrNickname);

        if(account==null){
            account=accountRepository.findByNickname(emailOrNickname);
        }

        if(account==null){
            throw new UsernameNotFoundException(emailOrNickname); // 닉네임 에러
        }
        return new UserAccount(account);
    }
}
