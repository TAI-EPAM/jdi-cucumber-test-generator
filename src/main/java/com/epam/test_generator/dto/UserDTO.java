package com.epam.test_generator.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDTO {


    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    @Size(min = 1, max = 255)
    private String surname;
    
    @NotNull
    @Email
    @Size(min = 1, max = 255)
    private String email;

    @NotNull
    @Size(min = 1, max = 255)
    private String password;

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

    public UserDTO(String name, String surname, String password, String role, String email) {
        this.name = name;
        this.surname = surname;
        this.password = password;
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
        return String.format("User {id= %s, name= %s, surname= %s, email= %s, password= %s, role= %s, attempts =%s, locked = %s}",
                id, name, surname, email, password, role, attempts, locked);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDTO)) {
            return false;
        }

        final UserDTO userDTO = (UserDTO) o;

        return (id != null ? id.equals(userDTO.id) : userDTO.id == null)
                && (name != null ? name.equals(userDTO.name) : userDTO.name == null)
                && (surname != null ? surname.equals(userDTO.surname) : userDTO.surname == null)
                && (email != null ? email.equals(userDTO.email) : userDTO.email == null)
                && (password != null ? password.equals(userDTO.password) : userDTO.password == null)
                && (role != null ? role.equals(userDTO.role) : userDTO.role == null)
                && (attempts != null ? attempts.equals(userDTO.attempts) : userDTO.attempts == null)
                && (locked != null ? locked.equals(userDTO.locked) : userDTO.locked == null);

    }


    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (locked != null ? locked.hashCode() : 0);
        result = 31 * result + (attempts != null ? attempts.hashCode() : 0);
        return result;
    }

}

