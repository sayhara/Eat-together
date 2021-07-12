package com.eattogether.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @EqualsAndHashCode(of="id")
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    @Lob @Basic(fetch = FetchType.EAGER)    // 대용량 & 즉시로딩
    private String profileImage;    // 프로필 이미지

    private LocalDateTime jointAt;  // 가입날짜

    private String bio; // 소개

    private int age;    // 나이

    private String location;    // 위치

    private boolean eatCreatedByEmail; // 개설 - 이메일로 받기

    private boolean eatCreatedByWeb; // 개설 - 웹으로 받기

    private boolean eatEnrollmentResultByEmail; // 참가신청 - 이메일로 받기

    private boolean eatEnrollmentResultByWeb; // 참가신청 - 웹으로 받기

    private boolean eatUpdatedByEmail; // 관심있는 곳 - 이메일로 받기

    private boolean eatUpdatedByWeb; // - 관심있는 곳 - 웹으로 받기

}
