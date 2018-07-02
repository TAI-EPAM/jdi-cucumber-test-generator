package com.epam.test_generator.dto;

import javax.validation.constraints.NotNull;

public class TokenDTO {

    public static final String TOKEN_HEADER = "Authorization";

    @NotNull
    private String token;

    public TokenDTO() {

    }

    public TokenDTO(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
