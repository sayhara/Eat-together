package com.jpaproject.config;

import com.jpaproject.domain.QAccount;
import com.jpaproject.domain.Tag;
import com.jpaproject.domain.Zone;
import com.querydsl.core.types.Predicate;

import java.util.Set;

public class AccountPredicates {

    public static Predicate findByTagsAndZones(Set<Tag> tags, Set<Zone> zones){
        QAccount account=QAccount.account;
        return account.zones.any().in(zones).and(account.tags.any().in(tags));
    }
}
