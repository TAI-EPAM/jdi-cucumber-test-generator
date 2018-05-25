package com.epam.test_generator.controllers.project.response;

import com.epam.test_generator.controllers.user.response.UserDTO;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

public class ProjectDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    private boolean active;

    private Set<UserDTO> users;

    private String jiraKey;

    public ProjectDTO(String name, String description, boolean active, Set<UserDTO> users) {
        this.name = name;
        this.description = description;
        this.active = active;
        this.users = users;
    }

    public ProjectDTO() {

    }

    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectDTO that = (ProjectDTO) o;
        return active == that.active &&
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(users, that.users) &&
            Objects.equals(jiraKey, that.jiraKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, active, users, jiraKey);
    }
}
