package com.jpaproject.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PasswordForm {

    @Length(min=8, max=20)
    private String newPassword;

    @Length(min=8, max=20)
    private String newPasswordRepeat;
}
