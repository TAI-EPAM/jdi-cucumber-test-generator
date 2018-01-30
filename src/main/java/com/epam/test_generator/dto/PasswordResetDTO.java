package com.epam.test_generator.dto;

import org.hibernate.validator.constraints.NotEmpty;

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