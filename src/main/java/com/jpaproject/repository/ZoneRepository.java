package com.jpaproject.repository;

import com.jpaproject.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository <Zone, Long> {

    Zone findByPart1AndPart3(String part1, String part3);
}
