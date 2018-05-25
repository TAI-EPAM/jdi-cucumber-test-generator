package com.epam.test_generator.controllers.admin.request;

import java.util.Objects;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRoleUpdateDTO {

    @NotNull
    @Email
    @Size(min = 1, max = 255)
    private String email;

    @NotNull
    private String role;

    public UserRoleUpdateDTO() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRoleUpdateDTO that = (UserRoleUpdateDTO) o;
        return Objects.equals(email, that.email) &&
            Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, role);
    }
}
