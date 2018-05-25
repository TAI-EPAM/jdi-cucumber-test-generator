package com.epam.test_generator.entities;


import com.epam.test_generator.entities.api.UserTrait;
import java.util.Objects;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

/**
 * This class represents user essence. User is a person who uses system. Besides simple fields like personal information,
 * data for log in and status it includes {@Link Role}. One user can have only one role. Role represents set of
 * privileges for users of current role.
 */
@Entity
public class User implements UserTrait {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    @Column(unique = true)
    private String email;
    @JsonIgnore
    private String password;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Role role;

    private Integer attempts;

    private Boolean locked;


    public User(String name, String surname, String email, String password, Role role) {
        this();
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User() {
        this.setLoginAttempts(0);
        this.locked = false;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getLoginAttempts() {
        return attempts;
    }

    public void setLoginAttempts(int attempts) {
        this.attempts = attempts;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) &&
            Objects.equals(name, user.name) &&
            Objects.equals(surname, user.surname) &&
            Objects.equals(email, user.email) &&
            Objects.equals(password, user.password) &&
            Objects.equals(role, user.role) &&
            Objects.equals(attempts, user.attempts) &&
            Objects.equals(locked, user.locked);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, surname, email, password, role, attempts, locked);
    }
}

