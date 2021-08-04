package com.eattogether.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter @EqualsAndHashCode
@Builder @AllArgsConstructor @NoArgsConstructor
public class Zone {

    @Id @GeneratedValue
    private Long id;

    private String part1;

    private String part2;

    private String part3;

}
