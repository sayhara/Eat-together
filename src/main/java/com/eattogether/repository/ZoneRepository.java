package com.eattogether.repository;

import com.eattogether.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository <Zone, Long> {

    Zone findByPart1AndPart2(String part1, String part2);
}
