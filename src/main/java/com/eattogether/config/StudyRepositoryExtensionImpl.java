package com.eattogether.config;

import com.eattogether.domain.Study;
import com.eattogether.repository.StudyRepositoryExtension;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class StudyRepositoryExtensionImpl
    extends QuerydslRepositorySupport implements StudyRepositoryExtension {

    public StudyRepositoryExtensionImpl(){
        super(Study.class);
    }

    @Override
    public List<Study> findByKeyWord(String keyword) {


        return null;
    }
}
