package com.epam.test_generator.controllers.user.response;

import java.util.Objects;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

public class UserDTO {


    private Long id;

    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 1, max = 255)
    private String surname;

    @Email
    @Size(min = 1, max = 255)
    private String email;

    private String role;

    private Integer attempts;

    private Boolean locked;

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public UserDTO(String name, String surname, String role, String email) {
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.email = email;
    }

    public UserDTO() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
            "id=" + id +
            ", name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            ", email='" + email + '\'' +
            ", role='" + role + '\'' +
            ", attempts=" + attempts +
            ", locked=" + locked +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) &&
            Objects.equals(name, userDTO.name) &&
            Objects.equals(surname, userDTO.surname) &&
            Objects.equals(email, userDTO.email) &&
            Objects.equals(role, userDTO.role) &&
            Objects.equals(attempts, userDTO.attempts) &&
            Objects.equals(locked, userDTO.locked);
    }


    @Override
    public int hashCode() {

        return Objects.hash(id, name, surname, email, role, attempts, locked);
    }
}

