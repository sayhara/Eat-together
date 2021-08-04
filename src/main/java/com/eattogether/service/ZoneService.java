package com.eattogether.service;

import com.eattogether.domain.Zone;
import com.eattogether.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;

    @PostConstruct
    public void initData() throws IOException {
        if(zoneRepository.count()==0){
            Resource resource=new ClassPathResource("zone.xlsx");
            List<Zone> zoneList = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8).stream()
                    .map(line->{
                        String[] split = line.split(",");
                        return Zone.builder().part1(split[0]).part2(split[1]).part3(split[2]).build();
                    }).collect(Collectors.toList());
            zoneRepository.saveAll(zoneList);
        }
    }
}
