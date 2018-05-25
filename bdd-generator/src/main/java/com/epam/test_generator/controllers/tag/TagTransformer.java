package com.epam.test_generator.controllers.tag;

import com.epam.test_generator.controllers.tag.request.TagCreateDTO;
import com.epam.test_generator.controllers.tag.request.TagUpdateDTO;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import com.epam.test_generator.entities.Tag;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TagTransformer {

    public Tag fromDto(TagCreateDTO tagCreateDTO) {
        Tag tag = new Tag();
        tag.setName(tagCreateDTO.getName());
        return tag;
    }

    public Tag fromDto(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        return tag;
    }

    public TagDTO toDto(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName(tag.getName());
        return tagDTO;
    }

    public Set<TagDTO> toDtoSet(Set<Tag> tags) {
        return tags.stream().map(this::toDto).collect(Collectors.toSet());
    }

    public Set<Tag> fromDtoSet(Set<TagDTO> tags) {
        return tags.stream().map(this::fromDto).collect(Collectors.toSet());
    }

    public Tag updateFromDto(TagUpdateDTO updateDTO, Tag tag) {
        if (updateDTO.getName() != null) {
            tag.setName(updateDTO.getName());
        }
        return tag;
    }

}
