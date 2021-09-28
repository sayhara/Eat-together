package com.jpaproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class StudyDescriptionForm {

    @Length(max=100)
    private String short_note;

    private String long_note;
}
