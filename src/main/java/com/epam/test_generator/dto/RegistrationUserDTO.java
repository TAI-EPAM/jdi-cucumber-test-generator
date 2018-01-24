package com.epam.test_generator.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegistrationUserDTO {

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


    public RegistrationUserDTO() {
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

    @Override
    public String toString() {
        return String.format("User {name= %s, surname= %s, email= %s, password= %s}",
                name, surname, email, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegistrationUserDTO)) {
            return false;
        }

        final RegistrationUserDTO registrationUserDTO = (RegistrationUserDTO) o;

        return (name != null ? name.equals(registrationUserDTO.name) : registrationUserDTO.name == null)
                && (surname != null ? surname.equals(registrationUserDTO.surname) : registrationUserDTO.surname == null)
                && (email != null ? email.equals(registrationUserDTO.email) : registrationUserDTO.email == null)
                && (password != null ? password.equals(registrationUserDTO.password) : registrationUserDTO.password == null);
    }


    @Override
    public int hashCode() {
        int result = (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

}
