package com.epam.test_generator.controllers.project.response;

import com.epam.test_generator.controllers.user.response.UserDTO;
import com.epam.test_generator.controllers.project.ProjectController;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Suit;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * DTO class, that fully represents {@link Project} entity (all it's fields). This DTO is used only for
 * retrieving Projects from API (see GET method in {@link ProjectController}), because there is no need
 * in container-fields (such as list of {@link Suit} with inner list of {@link Case} etc) for other
 * methods (PUT, POST, DELETE) - in this cases usage of {@link ProjectDTO} is more convenient for
 * API's users.
 */
public class ProjectFullDTO {

    private Long id;

    private String name;

    private String description;

    private List<SuitDTO> suits;

    private Set<UserDTO> users;

    private String jiraKey;

    private boolean active;

    public ProjectFullDTO(String name, String description,
                          List<SuitDTO> suits, Set<UserDTO> users, boolean active) {
        this.name = name;
        this.description = description;
        this.suits = suits;
        this.users = users;
        this.active = active;
    }

    public ProjectFullDTO() {

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

    public List<SuitDTO> getSuits() {
        return suits;
    }

    public void setSuits(List<SuitDTO> suits) {
        this.suits = suits;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectFullDTO that = (ProjectFullDTO) o;
        return active == that.active &&
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(suits, that.suits) &&
            Objects.equals(users, that.users) &&
            Objects.equals(jiraKey, that.jiraKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, suits, users, jiraKey, active);
    }
}
