package com.eattogether.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class StudyDescriptionForm {

    @Length(max=100)
    private String short_note;

    private String long_note;
}
