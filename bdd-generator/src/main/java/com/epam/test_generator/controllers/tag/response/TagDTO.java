package com.epam.test_generator.controllers.tag.response;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TagDTO implements Serializable {

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    public TagDTO() {
    }

    public TagDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TagDTO tagDTO = (TagDTO) o;
        return Objects.equals(name, tagDTO.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "TagDTO{" +
            "name='" + name + '\'' +
            '}';
    }

}
