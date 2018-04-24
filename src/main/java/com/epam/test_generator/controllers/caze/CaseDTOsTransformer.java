package com.epam.test_generator.controllers.caze;

import com.epam.test_generator.controllers.caze.request.CaseCreateDTO;
import com.epam.test_generator.controllers.caze.request.CaseUpdateDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.step.StepTransformer;
import com.epam.test_generator.controllers.tag.TagTransformer;
import com.epam.test_generator.entities.Case;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CaseDTOsTransformer{

    private TagTransformer tagTransformer = new TagTransformer();

    private StepTransformer stepTransformer = new StepTransformer();

    public List<CaseDTO> toDtoList(List<Case> cases) {
        return cases.stream().map(this::toDto).collect(Collectors.toList());
    }

    public CaseDTO toDto(Case caze) {
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setId(caze.getId());
        caseDTO.setName(caze.getName());
        caseDTO.setDescription(caze.getDescription());
        caseDTO.setSteps(stepTransformer.toDtoList(caze.getSteps()));
        caseDTO.setCreationDate(caze.getCreationDate().toInstant().toEpochMilli());
        caseDTO.setUpdateDate(caze.getUpdateDate().toInstant().toEpochMilli());
        caseDTO.setPriority(caze.getPriority());
        caseDTO.setTags(caze.getTags().stream().map(tagTransformer::toDto).collect(Collectors.toSet()));
        caseDTO.setStatus(caze.getStatus());
        caseDTO.setComment(caze.getComment());
        return caseDTO;
    }

    public Case fromDto(CaseCreateDTO caseCreateDTO) {
        Case caze = new Case();
        caze.setName(caseCreateDTO.getName());
        caze.setDescription(caseCreateDTO.getDescription());
        caze.setPriority(caseCreateDTO.getPriority());
        caze.setComment(caseCreateDTO.getComment());
        caze.setTags(caseCreateDTO.getTags().stream().map(tagTransformer::fromDto).collect(Collectors.toSet()));
        return caze;
    }

    public Case updateFromDto(CaseUpdateDTO dto, Case caze) {
        if (dto.getName() != null) {
            caze.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            caze.setDescription(dto.getDescription());
        }
        if (dto.getPriority() != null) {
            caze.setPriority(dto.getPriority());
        }
        if (dto.getStatus() != null) {
            caze.setStatus(dto.getStatus());
        }
        if (dto.getTags() != null) {
            caze.setTags(dto.getTags().stream().map(tagTransformer::fromDto).collect(Collectors.toSet()));
        }
        if (dto.getComment() != null) {
            caze.setComment(dto.getComment());
        }

        caze.setUpdateDate(ZonedDateTime.now());
        caze.setLastModifiedDate(ZonedDateTime.now());

        return caze;
    }

}
