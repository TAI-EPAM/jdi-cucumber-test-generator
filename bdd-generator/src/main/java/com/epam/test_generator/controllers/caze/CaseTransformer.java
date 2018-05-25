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
public class CaseTransformer {

    private TagTransformer tagTransformer = new TagTransformer();

    private StepTransformer stepTransformer = new StepTransformer();

    public CaseDTO toDto(Case caze) {
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setId(caze.getId());
        caseDTO.setName(caze.getName());
        caseDTO.setDescription(caze.getDescription());
        if (caze.getSteps() != null) {
            caseDTO.setSteps(stepTransformer.toDtoList(caze.getSteps()));
        }
        caseDTO.setCreationDate(caze.getCreationDate().toInstant().getEpochSecond());
        caseDTO.setUpdateDate(caze.getUpdateDate().toInstant().getEpochSecond());
        caseDTO.setPriority(caze.getPriority());
        if (caze.getTags() != null) {
            caseDTO.setTags(tagTransformer.toDtoSet(caze.getTags()));
        }
        caseDTO.setDisplayedStatusName(caze.getStatus().getStatusName());
        caseDTO.setComment(caze.getComment());
        caseDTO.setRowNumber(caze.getRowNumber());
        return caseDTO;
    }

    public Case fromDto(CaseCreateDTO caseCreateDTO) {
        Case caze = new Case();
        caze.setName(caseCreateDTO.getName());
        caze.setDescription(caseCreateDTO.getDescription());
        caze.setPriority(caseCreateDTO.getPriority());
        caze.setComment(caseCreateDTO.getComment());
        if (caseCreateDTO.getTags() != null) {
            caze.setTags(tagTransformer.fromDtoSet(caseCreateDTO.getTags()));
        }
        return caze;
    }

    public List<CaseDTO> toDtoList(List<Case> cases) {
        return cases.stream().map(this::toDto).collect(Collectors.toList());
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
            caze.updateTags(tagTransformer.fromDtoSet(dto.getTags()));
        }
        if (dto.getComment() != null) {
            caze.setComment(dto.getComment());
        }

        caze.setUpdateDate(ZonedDateTime.now());
        caze.setLastModifiedDate(ZonedDateTime.now());

        return caze;
    }

}
