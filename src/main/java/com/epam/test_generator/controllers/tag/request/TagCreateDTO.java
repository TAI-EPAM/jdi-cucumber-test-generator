package com.epam.test_generator.controllers.tag.request;

import com.epam.test_generator.controllers.tag.response.TagDTO;
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
        if (!(o instanceof TagCreateDTO)) {
            return false;
        }

        TagCreateDTO tagDTO = (TagCreateDTO) o;

        return name != null ? name.equals(tagDTO.name) : tagDTO.name == null;
    }

    @Override
    public int hashCode() {
        return (name != null ? name.hashCode() : 0);
    }

}
