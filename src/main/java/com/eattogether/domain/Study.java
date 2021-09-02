package com.eattogether.domain;

import com.eattogether.controller.UserAccount;
import com.eattogether.dto.StudyCreatedEvent;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of="id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Study {

    @Id @GeneratedValue
    @Column(name = "study_id")
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
                && !this.managers.contains(userAccount.getAccount());
    }

    public boolean isMember(UserAccount userAccount){
        return this.members.contains(userAccount.getAccount());
    }

    public boolean isManager(UserAccount userAccount){
        return this.managers.contains(userAccount.getAccount());
    }

    public String getImage(){
        return image!=null ? image : "/images/default_banner.jpg";
    }

    public void publish() {
        if(!this.closed && !this.is_publish()){
            this.is_publish=true;
            startTime=LocalDateTime.now();
        } else{
            throw new RuntimeException("이미 공개했거나 종료된 스터디입니다.");
        }
    }

    public void close(){
        if(!this.closed && this.is_publish){
            this.closed=true;
            this.endTime=LocalDateTime.now();
        } else{
            throw new RuntimeException("공개하지 않았거나 종료된 스터디입니다.");
        }
    }

    public void startRecruit() {
        this.is_recruit=true;
        this.recruiting_time=LocalDateTime.now();
    }

    public void stopRecruit() {
        this.is_recruit=false;
        this.recruiting_time=LocalDateTime.now();
    }

    public boolean isRemovable() {
        return !this.is_publish();
    }

    public void addMember(Account account) {
        this.getMembers().add(account);
    }

    public void removeMember(Account account) {
        this.getMembers().remove(account);
    }
}
