package com.eattogether.dto;

import com.eattogether.domain.Account;
import com.eattogether.domain.Zone;
import lombok.Data;

@Data
public class ZoneForm {

    private String zoneName;

    public String getPart1(){
        return zoneName.substring(0,zoneName.indexOf("("));
    }

    public String getPart2(){
        return zoneName.substring(zoneName.indexOf("(")+1,zoneName.indexOf(")"));
    }

    public String getPart3(){
        return zoneName.substring(zoneName.indexOf("/")+1);
    }

    public Zone getZone(){
        return Zone.builder().part1(this.getPart1())
                .part2(this.getPart2())
                .part3(this.getPart3()).build();
    }

}
