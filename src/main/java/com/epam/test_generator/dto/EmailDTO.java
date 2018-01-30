package com.epam.test_generator.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

public class EmailDTO {
    @NotNull
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
