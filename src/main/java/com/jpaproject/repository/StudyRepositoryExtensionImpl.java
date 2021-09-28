package com.jpaproject.repository;

import com.jpaproject.domain.QAccount;
import com.jpaproject.domain.QStudy;
import com.jpaproject.domain.QZone;
import com.jpaproject.domain.Study;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class StudyRepositoryExtensionImpl
        extends QuerydslRepositorySupport implements StudyRepositoryExtension {

    public StudyRepositoryExtensionImpl() {
        super(Study.class);
    }

    @Override
    public Page<Study> findByKeyword(String keyword, Pageable pageable) {
        QStudy study=QStudy.study;
        JPQLQuery<Study> query=from(study).where(study.is_publish.isTrue()
            .and(study.title.containsIgnoreCase(keyword))
            .or(study.zones.any().part1.containsIgnoreCase(keyword)))
            .leftJoin(study.zones, QZone.zone).fetchJoin()
            .leftJoin(study.members, QAccount.account).fetchJoin()
            .distinct();
        JPQLQuery<Study> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Study> fetchResults = pageableQuery.fetchResults();
        return new PageImpl<>(fetchResults.getResults(),pageable,fetchResults.getTotal());
    }
}
