package com.epam.test_generator.controllers.tag.request;

import com.epam.test_generator.controllers.tag.response.TagDTO;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TagCreateDTO {

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    public TagCreateDTO() {
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
        TagCreateDTO that = (TagCreateDTO) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
