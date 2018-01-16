package com.epam.test_generator.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ChangeUserRoleDTO {

    @NotNull
    @Email
    @Size(min = 1, max = 255)
    private String email;

    @NotNull
    private String role;

    public ChangeUserRoleDTO() {
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
