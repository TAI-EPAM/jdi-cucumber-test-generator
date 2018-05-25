package com.epam.test_generator.controllers.user.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.springframework.stereotype.Component;

@Component
public class LoginUserDTO {

    @NotNull
    @Size(min = 1, max = 255)
    @Email
    private String email;

    @NotNull
    @Size(min = 1, max = 255)
    private String password;

    public LoginUserDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
