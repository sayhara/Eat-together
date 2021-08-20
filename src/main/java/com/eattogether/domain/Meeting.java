package com.eattogether.domain;

import com.eattogether.controller.UserAccount;
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
    @Column(name = "meeting_id")
    private Long id;

    @ManyToMany
    private Set<Account> managers = new HashSet<>();

    @ManyToMany
    private Set<Account> members=new HashSet<>();

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="manager_id")
//    private Account manager;
//
//    @ManyToMany
//    @JoinTable(name="MEMBER_MEETING")
//    private Set<Account> members=new HashSet<>();

//    @Column(unique = true)
    private String url;

    private String title;

    private String short_note;

    //@Lob @Basic(fetch = FetchType.EAGER)
    private String long_note;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    @ManyToMany
    private Set<Zone> zones=new HashSet<>();

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime recruiting_time;

    private boolean is_recruit; // 인원 모집여부

    private boolean is_publish; // 공개여부
    
    private boolean closed; // 종료여부
    
    private boolean useBanner; // 베너 사용여부

    public void addManager(Account account){
        this.managers.add(account);
    }

    public boolean isJoinable(UserAccount userAccount){
        Account account = userAccount.getAccount();

        return this.is_recruit() && this.is_publish()
                &&!this.members.contains(account) && !this.managers.contains(account);
    }

    public boolean isMember(UserAccount userAccount){
        return this.members.contains(userAccount.getAccount());
    }

    public boolean isManager(UserAccount userAccount){
        return this.managers.contains(userAccount.getAccount());
    }

}
