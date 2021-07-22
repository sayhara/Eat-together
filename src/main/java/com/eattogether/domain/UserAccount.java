package com.eattogether.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Getter
public class UserAccount extends User {
// 스프링 시큐리티 유저정보와 도메인 유저정보의 gap을 없앰
    private Account account;

    public UserAccount(Account account) {
        super(account.getNickname(), account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.account=account;
    }
}
