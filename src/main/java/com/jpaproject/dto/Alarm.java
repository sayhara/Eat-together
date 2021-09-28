package com.jpaproject.dto;

import com.jpaproject.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Alarm {

    private boolean eatCreatedByWeb; // 개설 - 웹으로 받기

    private boolean eatEnrollmentResultByWeb; // 참가신청 - 웹으로 받기

    private boolean eatUpdatedByWeb; // - 관심있는 곳 - 웹으로 받기

    public Alarm(Account account) {
        this.eatCreatedByWeb=account.isEatCreatedByWeb();
        this.eatEnrollmentResultByWeb=account.isEatEnrollmentResultByWeb();
        this.eatUpdatedByWeb=account.isEatUpdatedByWeb();
    }
}
