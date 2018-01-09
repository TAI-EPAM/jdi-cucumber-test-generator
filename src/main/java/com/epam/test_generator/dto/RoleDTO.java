package com.epam.test_generator.dto;

import com.epam.test_generator.entities.User;
import java.util.Collection;
import javax.validation.constraints.NotNull;

public class RoleDTO {

    private Long id;

    @NotNull
    private String name;

    private Collection<User> users;


    public RoleDTO(String roleName) {
        this.name = roleName;
    }

    public RoleDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }
}
