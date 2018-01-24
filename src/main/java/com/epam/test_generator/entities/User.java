package com.epam.test_generator.entities;


import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
public class User {

    @GeneratedValue
    @Id
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
        this.setAttempts(0);
        this.locked = false;
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

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
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
        if (!(o instanceof User)) {
            return false;
        }

        final User aUser = (User) o;

        return (id != null ? id.equals(aUser.id) : aUser.id == null)
                && (name != null ? name.equals(aUser.name) : aUser.name == null)
                && (surname != null ? surname.equals(aUser.surname) : aUser.surname == null)
                && (email != null ? email.equals(aUser.email) : aUser.email == null)
                && (password != null ? password.equals(aUser.password) : aUser.password == null)
                && (role != null ? role.equals(aUser.role) : aUser.role == null)
                && (attempts != null ? attempts.equals(aUser.attempts) : aUser.attempts == null)
                && (locked != null ? locked.equals(aUser.locked) : aUser.locked == null);

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

