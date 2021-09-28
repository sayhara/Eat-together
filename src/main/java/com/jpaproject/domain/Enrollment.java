package com.jpaproject.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @EqualsAndHashCode(of="id")
public class Enrollment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Event event; // 여러개의 참가신청은 하나의 이벤트에 맵핑

    @ManyToOne
    private Account account; // 신청한 사람

    private LocalDateTime enrolledAt; // 선착순

    private boolean accepted;

    private boolean attended;
}
