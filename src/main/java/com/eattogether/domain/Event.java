package com.eattogether.domain;

import com.eattogether.controller.UserAccount;
import com.eattogether.dto.EventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @EqualsAndHashCode(of="id")
public class Event {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Study study;

    @ManyToOne
    private Account createdBy;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private LocalDateTime endEnrollmentDateTime;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Column
    private Integer limitOfEnrollments; // int로 할시에 null로 초기화 불가능

    @OneToMany(mappedBy = "event")
    private List<Enrollment> enrollments; // 하나의 이벤트에 여러개의 신청서

    @Enumerated(EnumType.STRING)
    private EventType eventType; // 이벤트 타입은 2개

    private boolean isNotClosed(){
        return this.endEnrollmentDateTime.isAfter(LocalDateTime.now());
    }

    private boolean isAlreadyEnrolled(UserAccount userAccount){
        Account account = userAccount.getAccount();
        for (Enrollment e : enrollments) {
            if(e.getAccount().equals(account)){
                return true;
            }
        }
        return false;
    }

    public boolean isEnrollableFor(UserAccount userAccount){
        return isNotClosed() && !isAlreadyEnrolled(userAccount);
    }

    public boolean isDisenrollableFor(UserAccount userAccount){
        return isNotClosed() && isAlreadyEnrolled(userAccount);
    }

    public boolean isAttended(UserAccount userAccount){
        Account account = userAccount.getAccount();
        for (Enrollment e : enrollments) {
            if(e.getAccount().equals(account) &&
                e.isAttended()){
                return true;
            }
        }
        return false;
    }

    public int numberOfRemainSpots(){
        return (int) (limitOfEnrollments-enrollments.
                        stream().filter(Enrollment::isAccepted).count());
    }

    public long getNumberOfAcceptedEnrollments(){
        return enrollments.stream().filter(Enrollment::isAccepted).count();
    }

    public boolean canAccept(Enrollment enrollment){
        return this.eventType==EventType.CONFIRMATIVE
                && this.enrollments.contains(enrollment)
                && !enrollment.isAttended()
                && !enrollment.isAccepted();
    }

    public boolean canReject(Enrollment enrollment){
        return this.eventType==EventType.CONFIRMATIVE
                && this.enrollments.contains(enrollment)
                && !enrollment.isAttended()
                && enrollment.isAccepted();
    }

    public boolean isAbleToAcceptWaitingEnrollment() {
        return this.eventType==EventType.FCFS &&
                this.limitOfEnrollments>this.getNumberOfAcceptedEnrollments();
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
        enrollment.setEvent(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        this.enrollments.remove(enrollment);
        enrollment.setEvent(null);
    }

    public Enrollment getTheFirstWaitingEnrollment(){
        for (Enrollment enrollment : enrollments) {
            if(!enrollment.isAccepted()){
                return enrollment;
            }
        }
        return null;
    }

    public void acceptTheNextWaitingEnrollment() {
        if(this.isAbleToAcceptWaitingEnrollment()){
            Enrollment enrollment=this.getTheFirstWaitingEnrollment();
            if(enrollment!=null){
                enrollment.setAccepted(true);
            }
        }
    }
}
