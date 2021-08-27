package com.eattogether.dto;

import com.eattogether.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;

@Data
@NoArgsConstructor
public class Profile {

    private String bio; // 소개

    private int age; // 낭

    private String major; // 전공

    private String location; // 지역

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage; // 프로필 이미지

    public Profile(Account account) {
        this.bio = account.getBio();
        this.age = account.getAge();
        this.major = account.getMajor();
        this.location=account.getLocation();
        this.profileImage=account.getProfileImage();
    }
}
