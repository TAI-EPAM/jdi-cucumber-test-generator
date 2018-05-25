package com.epam.test_generator.controllers.tag.request;

import java.util.Objects;
import javax.validation.constraints.Size;

public class TagUpdateDTO {

    @Size(min = 1, max = 255)
    private String name;

    public TagUpdateDTO() {
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
        TagUpdateDTO that = (TagUpdateDTO) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
