package com.epam.test_generator.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * This class represents role essence. Role is a named group for some privileges. Each user is assigned with a role that
 * determines his rights in the system. 
 */
@Entity
public class Role {

    @GeneratedValue
    @Id
    private Long id;

    private String name;


    public Role(String roleName) {
        this.name = roleName;
    }

    public Role() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Role{ id= %s ,name= %s};",
            id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }

        final Role aRole = (Role) o;

        return (id != null ? id.equals(aRole.id) : aRole.id == null)
            && (name != null ? name.equals(aRole.name) : aRole.name == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
