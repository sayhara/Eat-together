package com.eattogether.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of="id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private String passwordRepeat; // 비밀번호 재확인

    @Lob @Basic(fetch = FetchType.EAGER)    // 대용량 & 즉시로딩
    private String profileImage;    // 프로필 이미지

    private LocalDateTime jointAt;  // 가입날짜

    private String bio; // 소개

    private int age;    // 나이
    
    private String major; // 전공

    private String location;    // 위치

    private boolean eatCreatedByWeb=true; // 개설 - 웹으로 받기

    private boolean eatEnrollmentResultByWeb=true; // 참가신청 - 웹으로 받기

    private boolean eatUpdatedByWeb=true; // - 관심있는 곳 - 웹으로 받기

//    @OneToOne(mappedBy = "manager",fetch = FetchType.LAZY) // 상대 테이블의 객체이름
//    private Meeting meeting;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="meeting_id") // 상대 테이블의 id이름
//    private Meeting meet_member;

    @ManyToMany
    private Set<Tag> tags=new HashSet<>();

    @ManyToMany
    private Set<Zone> zones=new HashSet<>();

    public boolean isManagerOf(Study study){
        return study.getManagers().contains(this);
    }

}
