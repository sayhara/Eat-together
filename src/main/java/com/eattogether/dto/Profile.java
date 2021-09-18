package com.eattogether.dto;

import com.eattogether.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;

@Data
@NoArgsConstructor
public class Profile {

    @Length(max=35)
    private String bio; // 소개

    private int age; // 낭

    @Length(max=50)
    private String major; // 전공

    @Length(max=50)
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
