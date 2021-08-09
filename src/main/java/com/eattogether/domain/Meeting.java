package com.eattogether.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of="id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Meeting {

    @Id @GeneratedValue
    @Column(name="meeting_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="account_id") // 상대 테이블의 id이름
    private Account manager;

    @OneToMany(mappedBy = "meet_member") // 상대 테이블의 객체이름
    private Set<Account> members=new HashSet<>();

    @Column(unique = true)
    private String url;

    private String title;

    private String note;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    @ManyToMany
    private Set<Zone> zones=new HashSet<>();

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime recruiting_time;

    private boolean is_recruit; // 인원 모집여부

    private boolean is_publish; // 공개여부
    
    private boolean close; // 종료여부
    
    private boolean useBanner; // 베너 사용여부
}
