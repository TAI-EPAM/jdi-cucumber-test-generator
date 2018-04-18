package com.epam.test_generator.controllers.user.request;

import org.hibernate.validator.constraints.NotEmpty;


/**
 * This DTO is used in password recovery scenario. User is identified by the given token. Password is a new password
 * edited by current user.
 */
public class PasswordResetDTO {

    @NotEmpty
    private String password;


    @NotEmpty
    private String token;

    public PasswordResetDTO() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}