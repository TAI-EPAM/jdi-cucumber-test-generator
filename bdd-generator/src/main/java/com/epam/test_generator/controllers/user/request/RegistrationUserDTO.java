package com.epam.test_generator.controllers.user.request;

import java.util.Objects;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This DTO represents {@Link User} essence, but only at the time of it's registration in the system. DTO is used only
 * to retrieve information about user without his role in the system or profile's status. In other situations it's more
 * convenient to use {@Link UserDTO}.
 *
 */
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegistrationUserDTO that = (RegistrationUserDTO) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(surname, that.surname) &&
            Objects.equals(email, that.email) &&
            Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, surname, email, password);
    }
}
