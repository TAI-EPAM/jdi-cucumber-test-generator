package com.epam.test_generator.controllers.tag.request;

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
        if (!(o instanceof TagUpdateDTO)) {
            return false;
        }

        TagUpdateDTO tagDTO = (TagUpdateDTO) o;

        return name != null ? name.equals(tagDTO.name) : tagDTO.name == null;
    }

    @Override
    public int hashCode() {
        return (name != null ? name.hashCode() : 0);
    }
}
