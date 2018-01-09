package com.epam.test_generator.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

public class UserDTO {


    private Long id;

    @NotNull
    @Email
    @Size(min = 1, max = 255)
    private String email;

    @NotNull
    @Size(min = 1, max = 255)
    private String password;

    private String role;

    public UserDTO(String password, String role, String email) {
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public UserDTO() {
    }

    public Long getId() {
        return id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            ", password='" + password + '\'' +
            ", role='" + role + '\'' +
            ", email='" + email + '\'' +
            '}';
    }
}

