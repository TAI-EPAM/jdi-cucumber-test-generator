package com.epam.test_generator.controllers.user.request;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;


/**
 * This DTO is used in password recovery scenario. The first step is to identify user by email and send him letter
 * with instructions.
 */
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
