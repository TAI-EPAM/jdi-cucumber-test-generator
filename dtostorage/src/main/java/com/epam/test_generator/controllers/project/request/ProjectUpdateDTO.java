package com.epam.test_generator.controllers.project.request;

import java.util.Objects;
import javax.validation.constraints.Size;

public class ProjectUpdateDTO {

    @Size(min = 1, max = 255)
    private String name;

    @Size(max = 255)
    private String description;

    public ProjectUpdateDTO() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectUpdateDTO that = (ProjectUpdateDTO) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}
