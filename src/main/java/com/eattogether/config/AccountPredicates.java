package com.eattogether.config;

import com.eattogether.domain.QAccount;
import com.eattogether.domain.Zone;
import com.querydsl.core.types.Predicate;

import java.util.Set;

public class AccountPredicates {

    public static Predicate findByZones(Set<Zone> zones){
        QAccount account=QAccount.account;
        return account.zones.any().in(zones);
    }
}
