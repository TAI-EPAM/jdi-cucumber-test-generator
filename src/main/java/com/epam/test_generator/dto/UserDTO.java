package com.epam.test_generator.dto;

import com.epam.test_generator.entities.Role;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Email;

public class UserDTO {


    public UserDTO(String password, Role role, String email) {
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public UserDTO() {
    }

    private Long id;

    @NotNull
    @Email
    @Size(min = 1, max = 255)
    private String email;

    @NotNull
    @Size(min = 1, max = 255)
    private String password;

    @ColumnDefault(value="USER")
    private Role role;

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

    public Role getRole() {
        return role;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public void setRole(Role role) {
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

