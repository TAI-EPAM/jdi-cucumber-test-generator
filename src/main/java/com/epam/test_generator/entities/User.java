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

    public User() {
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

    @Override
    public String toString() {
        return String.format("User {id= %s, password= %s, role= %s}", id, password, role);
    }

}

