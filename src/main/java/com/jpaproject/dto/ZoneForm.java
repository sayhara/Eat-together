package com.jpaproject.dto;

import com.jpaproject.domain.Zone;
import lombok.Data;

@Data
public class ZoneForm {

    // 입력형태 : Seoul(서울)/None
    private String zoneName;

    public String getPart1Name(){
        return zoneName.substring(0,zoneName.indexOf("("));
    }

    public String getPart2Name(){
        return zoneName.substring(zoneName.indexOf("(")+1,zoneName.indexOf(")"));
    }

    public String getPart3Name(){
        return zoneName.substring(zoneName.indexOf("/")+1);
    }

    public Zone getZone(){
        return Zone.builder().part1(this.getPart1Name())
                .part2(this.getPart2Name())
                .part3(this.getPart3Name()).build();
    }

}
