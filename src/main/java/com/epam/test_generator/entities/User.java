package com.epam.test_generator.entities;


import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
public class User {

    @GeneratedValue
    @Id
    private Long id;

    @Column(unique = true)
    private String email;
    @JsonIgnore
    private String password;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Role role;
    @JsonIgnore
    private Integer attempts;
    @JsonIgnore
    private Boolean locked;



    public User(String email, String password, Role role) {
        this();
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
        return String.format("User {id= %s, password= %s, role= %s}", id, password, role);
    }

}

