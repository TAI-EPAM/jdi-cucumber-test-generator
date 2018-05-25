package com.epam.test_generator.controllers.suit;

import com.epam.test_generator.controllers.caze.CaseTransformer;
import com.epam.test_generator.controllers.suit.request.SuitCreateDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.controllers.suit.request.SuitUpdateDTO;
import com.epam.test_generator.controllers.tag.TagTransformer;
import com.epam.test_generator.entities.Suit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SuitTransformer {

    @Autowired
    private TagTransformer tagTransformer;

    @Autowired
    private CaseTransformer caseTransformer;

    public Suit fromDto(SuitCreateDTO suitCreateDTO) {
        Suit suit = new Suit();
        suit.setName(suitCreateDTO.getName());
        suit.setPriority(suitCreateDTO.getPriority());
        suit.setDescription(suitCreateDTO.getDescription());
        if (suitCreateDTO.getTags() != null) {
            suit.setTags(tagTransformer.fromDtoSet(suitCreateDTO.getTags()));
        }
        return suit;
    }

    public SuitDTO toDto(Suit suit) {
        SuitDTO suitDTO = new SuitDTO();
        suitDTO.setId(suit.getId());
        suitDTO.setName(suit.getName());
        suitDTO.setDescription(suit.getDescription());
        suitDTO.setPriority(suit.getPriority());
        suitDTO.setCreationDate(suit.getCreationDate().toInstant().getEpochSecond());
        suitDTO.setUpdateDate(suit.getUpdateDate().toInstant().getEpochSecond());
        suitDTO.setRowNumber(suit.getRowNumber());
        suitDTO.setDisplayedStatusName(suit.getStatus().getStatusName());
        if (suit.getTags() != null) {
            suitDTO.setTags(tagTransformer.toDtoSet(suit.getTags()));
        }
        if (suit.getCases() != null) {
            suitDTO.setCases(caseTransformer.toDtoList(suit.getCases()));
        }
        return suitDTO;
    }

    public List<SuitDTO> toDtoList(List<Suit> suits) {
        return suits.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Suit updateFromDto(SuitUpdateDTO dto, Suit suit) {
        if (dto.getName() != null) {
            suit.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            suit.setDescription(dto.getDescription());
        }
        if (dto.getPriority() != null) {
            suit.setPriority(dto.getPriority());
        }
        if (dto.getTags() != null) {
            suit.updateTags(tagTransformer.fromDtoSet(dto.getTags()));
        }

        return suit;
    }

}