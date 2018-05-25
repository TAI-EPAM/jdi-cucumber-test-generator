package com.epam.test_generator.controllers.caze;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import com.epam.test_generator.controllers.caze.request.CaseCreateDTO;
import com.epam.test_generator.controllers.caze.request.CaseUpdateDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.step.StepTransformer;
import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.controllers.tag.TagTransformer;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Tag;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CaseTransformerTest {

    @InjectMocks
    private CaseTransformer caseTransformer;

    @Mock
    private TagTransformer tagTransformer;

    @Mock
    private StepTransformer stepTransformer;

    private static final Long ID = 1L;
    private static final String NAME = "name";
    private static final String NEW_NAME = "new name";
    private static final String DESCRIPTION = "description";
    private static final String NEW_DESCRIPTION = "new description";
    private static final Integer PRIORITY = 1;
    private static final Integer NEW_PRIORITY = 2;
    private static final String COMMENT = "comment";
    private static final String NEW_COMMENT = "new comment";
    private static final Tag TAG_1 = new Tag("tag1");
    private static final Tag TAG_2 = new Tag("tag2");
    private static final Set<Tag> TAGS = new HashSet<>(Collections.singletonList(TAG_1));
    private static final Set<Tag> NEW_TAGS = new HashSet<>(Arrays.asList(TAG_1, TAG_2));
    private static final TagDTO TAG_DTO_1 = new TagDTO("tag1");
    private static final TagDTO TAG_DTO_2 = new TagDTO("tag2");
    private static final Set<TagDTO> TAG_DTOS = new HashSet<>(Collections.singletonList(TAG_DTO_1));
    private static final Set<TagDTO> NEW_TAG_DTOS = new HashSet<>(Arrays.asList(TAG_DTO_1, TAG_DTO_2));
    private static final Status STATUS = Status.PASSED;
    private static final Status NEW_STATUS = Status.NOT_RUN;
    private static final Step STEP = new Step();
    private static final List<Step> STEPS = Collections.singletonList(STEP);
    private static final StepDTO STEP_DTO = new StepDTO();
    private static final List<StepDTO> STEP_DTOS = Collections.singletonList(STEP_DTO);
    private static final Integer ROW_NUMBER = 1;

    @Test
    public void toDto_ValidCase_Success() {
        Case caze = new Case();
        caze.setId(ID);
        caze.setName(NAME);
        caze.setDescription(DESCRIPTION);
        caze.setPriority(PRIORITY);
        caze.setComment(COMMENT);
        caze.setTags(TAGS);
        caze.setStatus(STATUS);
        caze.setSteps(STEPS);
        caze.setRowNumber(ROW_NUMBER);
        List<Case> cases = Collections.singletonList(caze);

        CaseDTO caseDto = new CaseDTO();
        caseDto.setId(ID);
        caseDto.setName(NAME);
        caseDto.setDescription(DESCRIPTION);
        caseDto.setPriority(PRIORITY);
        caseDto.setComment(COMMENT);
        caseDto.setTags(TAG_DTOS);
        caseDto.setDisplayedStatusName(STATUS.getStatusName());
        caseDto.setSteps(STEP_DTOS);
        caseDto.setRowNumber(ROW_NUMBER);

        when(tagTransformer.toDtoSet(anySet())).thenReturn(TAG_DTOS);
        when(stepTransformer.toDtoList(anyList())).thenReturn(STEP_DTOS);
        CaseDTO actualCaseDTO = caseTransformer.toDto(caze);

        assertEquals(caseDto, actualCaseDTO);

        verify(tagTransformer).toDtoSet(anySet());
        verify(stepTransformer).toDtoList(anyList());
    }

    @Test
    public void fromDto_ValidCaseCreateDTO_Success() {
        Case caze = new Case();
        caze.setName(NAME);
        caze.setDescription(DESCRIPTION);
        caze.setPriority(PRIORITY);
        caze.setComment(COMMENT);
        caze.setTags(TAGS);

        CaseCreateDTO dto = new CaseCreateDTO();
        dto.setName(NAME);
        dto.setDescription(DESCRIPTION);
        dto.setPriority(PRIORITY);
        dto.setComment(COMMENT);
        dto.setTags(TAG_DTOS);

        when(tagTransformer.fromDtoSet(anySet())).thenReturn(TAGS);
        Case actualCase = caseTransformer.fromDto(dto);

        assertEquals(caze, actualCase);

        verify(tagTransformer).fromDtoSet(anySet());
    }

    @Test
    public void toDtoList() {
        Case caze = new Case();
        caze.setId(ID);
        caze.setName(NAME);
        caze.setDescription(DESCRIPTION);
        caze.setPriority(PRIORITY);
        caze.setComment(COMMENT);
        caze.setTags(NEW_TAGS);
        caze.setStatus(STATUS);
        caze.setSteps(STEPS);
        caze.setRowNumber(ROW_NUMBER);
        List<Case> cases = Collections.singletonList(caze);

        CaseDTO caseDto = new CaseDTO();
        caseDto.setId(ID);
        caseDto.setName(NAME);
        caseDto.setDescription(DESCRIPTION);
        caseDto.setPriority(PRIORITY);
        caseDto.setComment(COMMENT);
        caseDto.setTags(NEW_TAG_DTOS);
        caseDto.setDisplayedStatusName(STATUS.getStatusName());
        caseDto.setSteps(STEP_DTOS);
        caseDto.setRowNumber(ROW_NUMBER);
        List<CaseDTO> caseDtos = Collections.singletonList(caseDto);

        when(tagTransformer.toDtoSet(anySet())).thenReturn(NEW_TAG_DTOS);
        when(stepTransformer.toDtoList(anyList())).thenReturn(STEP_DTOS);
        List<CaseDTO> actualCaseDTOs = caseTransformer.toDtoList(cases);

        assertEquals(caseDtos, actualCaseDTOs);

        verify(tagTransformer).toDtoSet(anySet());
        verify(stepTransformer).toDtoList(anyList());
    }

    @Test
    public void updateFromDto_EmptyCaseAndCaseUpdateDTOWithNoNullValues_AllFieldInCaseUpdated() {
        Case caze = new Case();

        CaseUpdateDTO dto = new CaseUpdateDTO();
        dto.setName(NEW_NAME);
        dto.setDescription(NEW_DESCRIPTION);
        dto.setPriority(NEW_PRIORITY);
        dto.setStatus(NEW_STATUS);
        dto.setTags(NEW_TAG_DTOS);
        dto.setComment(NEW_COMMENT);

        when(tagTransformer.fromDtoSet(anySet())).thenReturn(NEW_TAGS);

        Case updatedCase = caseTransformer.updateFromDto(dto, caze);

        assertEquals(dto.getName(), updatedCase.getName());
        assertEquals(dto.getDescription(), updatedCase.getDescription());
        assertEquals(dto.getPriority(), updatedCase.getPriority());
        assertEquals(dto.getStatus(), updatedCase.getStatus());
        assertEquals(NEW_TAGS, updatedCase.getTags());
        assertEquals(dto.getComment(), updatedCase.getComment());

        verify(tagTransformer).fromDtoSet(anySet());
    }

    @Test
    public void updateFromDto_CaseUpdateDTOWithNullNameAndTags_OnlySomeFieldsAreUpdated() {
        Case caze = new Case();

        CaseUpdateDTO dto = new CaseUpdateDTO();
        dto.setDescription(NEW_DESCRIPTION);
        dto.setPriority(NEW_PRIORITY);
        dto.setStatus(NEW_STATUS);
        dto.setComment(NEW_COMMENT);

        Case updatedCase = caseTransformer.updateFromDto(dto, caze);

        assertEquals(caze.getName(), updatedCase.getName());
        assertEquals(caze.getTags(), updatedCase.getTags());
        assertEquals(dto.getDescription(), updatedCase.getDescription());
        assertEquals(dto.getPriority(), updatedCase.getPriority());
        assertEquals(dto.getStatus(), updatedCase.getStatus());
        assertEquals(dto.getComment(), updatedCase.getComment());

        verifyZeroInteractions(tagTransformer);
    }

    @Test
    public void updateFromDto_CaseWithSomeTagsAndCaseUpdateDTOWithEmptySetOfTags_Success() {
        Case caze = new Case();
        caze.setTags(TAGS);

        CaseUpdateDTO dto = new CaseUpdateDTO();
        dto.setTags(Collections.emptySet());

        when(tagTransformer.fromDtoSet(anySet())).thenReturn(Collections.emptySet());

        Case updatedCase = caseTransformer.updateFromDto(dto, caze);

        assertTrue(updatedCase.getTags().isEmpty());

        verify(tagTransformer).fromDtoSet(anySet());
    }

}