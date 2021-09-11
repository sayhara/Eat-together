package com.eattogether.repository;

import com.eattogether.domain.QStudy;
import com.eattogether.domain.Study;
import com.eattogether.repository.StudyRepositoryExtension;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class StudyRepositoryExtensionImpl
        extends QuerydslRepositorySupport implements StudyRepositoryExtension {

    public StudyRepositoryExtensionImpl() {
        super(Study.class);
    }

    @Override
    public List<Study> findByKeyword(String keyword, Pageable pageable) {
        QStudy study=QStudy.study;
        JPQLQuery<Study> query=from(study).where(study.is_publish.isTrue()
            .and(study.title.containsIgnoreCase(keyword))
            .or(study.zones.any().part1.containsIgnoreCase(keyword)));
        return query.fetch();
    }
}
