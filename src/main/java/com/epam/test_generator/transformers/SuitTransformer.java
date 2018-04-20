package com.epam.test_generator.transformers;

import com.epam.test_generator.controllers.suit.request.SuitCreateDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.controllers.suit.request.SuitUpdateDTO;
import com.epam.test_generator.controllers.tag.TagTransformer;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SuitTransformer {

    @Autowired
    private TagTransformer tagTransformer;

    public Suit fromDto(SuitCreateDTO suitCreateDTO) {
        Suit suit = new Suit();
        suit.setName(suitCreateDTO.getName());
        suit.setDescription(suitCreateDTO.getDescription());
        suit.setPriority(suitCreateDTO.getPriority());
        suit.setCreationDate(suitCreateDTO.getCreationDate());
        if (suitCreateDTO.getTags() != null) {
            List<TagDTO> tagDTOs = new ArrayList<>(suitCreateDTO.getTags());
            suit.setTags(getSetOfTagsFromListOfTagDTOs(tagDTOs));
        }
        return suit;
    }

    public SuitDTO toDto(Suit suit) {
        SuitDTO suitDTO = new SuitDTO();
        suitDTO.setId(suit.getId());
        suitDTO.setName(suit.getName());
        suitDTO.setDescription(suit.getDescription());
        suitDTO.setPriority(suit.getPriority());
        suitDTO.setCreationDate(suit.getCreationDate());
        if (suit.getTags() != null) {
            List<Tag> tags = new ArrayList<>(suit.getTags());
            suitDTO.setTags(getSetOfTagDTOsFromListOfTags(tags));
        }
        return suitDTO;
    }

    public List<SuitDTO> toDtoList(List<Suit> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public void mapDTOToEntity(SuitUpdateDTO suitUpdateDTO, Suit suit) {
        if (suitUpdateDTO.getName() != null) {
            suit.setName(suitUpdateDTO.getName());
        }
        if (suitUpdateDTO.getDescription() != null) {
            suit.setDescription(suitUpdateDTO.getDescription());
        }
        if (suitUpdateDTO.getPriority() != null) {
            suit.setPriority(suitUpdateDTO.getPriority());
        }
        if (suitUpdateDTO.getTags().size() != 0) {
            List<TagDTO> tagDTOs = new ArrayList<>(suitUpdateDTO.getTags());
            suit.setTags(getSetOfTagsFromListOfTagDTOs(tagDTOs));
        }
    }

    private Set<Tag> getSetOfTagsFromListOfTagDTOs(List<TagDTO> tagDTOList) {
        return new HashSet<>(tagTransformer.fromListDto(tagDTOList));
    }

    private Set<TagDTO> getSetOfTagDTOsFromListOfTags(List<Tag> tagList) {
        return new HashSet<>(tagTransformer.toListDto(tagList));
    }
}
