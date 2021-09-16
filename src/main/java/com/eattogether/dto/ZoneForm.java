package com.eattogether.dto;

import com.eattogether.domain.Account;
import com.eattogether.domain.Zone;
import lombok.Data;

@Data
public class ZoneForm {

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
