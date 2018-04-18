package com.epam.test_generator.controllers.caze;

import com.epam.test_generator.controllers.caze.request.CaseCreateDTO;
import com.epam.test_generator.controllers.caze.request.CaseUpdateDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.controllers.tag.TagTransformer;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.controllers.step.StepTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.naming.NameAlreadyBoundException;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CaseDTOsTransformerTest {

    @InjectMocks
    private CaseDTOsTransformer caseDTOsTransformer;

    @Mock
    private TagTransformer tagTransformer;

    @Mock
    private StepTransformer stepTransformer;

    private static final Long ID = 1L;
    private static final String NAME = "name";
    private static final String NEW_NAME = "new name";
    private static final String DESCRIPTION = "desciption";
    private static final String NEW_DESCRIPTION = "new description";
    private static final Integer PRIORITY = 1;
    private static final Integer NEW_PRIORITY = 2;
    private static final String COMMENT = "comment";
    private static final String NEW_COMMENT = "new comment";
    private static final Tag TAG = new Tag();
    private static final Set<Tag> TAGS = new HashSet<>(Collections.singletonList(TAG));
    private static final Set<Tag> NEW_TAGS = new HashSet<>(Collections.singletonList(TAG));
    private static final TagDTO TAG_DTO = new TagDTO();
    private static final Set<TagDTO> TAG_DTOS = new HashSet<>(Collections.singletonList(TAG_DTO));
    private static final Set<TagDTO> NEW_TAG_DTOS = new HashSet<>(Collections.singletonList(TAG_DTO));
    private static final Status STATUS = Status.PASSED;
    private static final Status NEW_STATUS = Status.NOT_RUN;
    private static final Step STEP = new Step();
    private static final List<Step> STEPS = Collections.singletonList(STEP);
    private static final StepDTO STEP_DTO = new StepDTO();
    private static final List<StepDTO> STEP_DTOS = Collections.singletonList(STEP_DTO);

    @Before
    public void setUp() {

    }

    @Test
    public void toDtoList() {
        Case caze = new Case();
        caze.setId(ID);
        caze.setName(NAME);
        caze.setDescription(DESCRIPTION);
        caze.setPriority(PRIORITY);
        caze.setComment(COMMENT);
        caze.setTags(TAGS);
        caze.setStatus(STATUS);
        caze.setSteps(STEPS);
        List<Case> cases = Collections.singletonList(caze);

        CaseDTO caseDto = new CaseDTO();
        caseDto.setId(ID);
        caseDto.setName(NAME);
        caseDto.setDescription(DESCRIPTION);
        caseDto.setPriority(PRIORITY);
        caseDto.setComment(COMMENT);
        caseDto.setTags(TAG_DTOS);
        caseDto.setStatus(STATUS);
        caseDto.setSteps(STEP_DTOS);
        List<CaseDTO> caseDtos = Collections.singletonList(caseDto);

        when(tagTransformer.toDto(any(Tag.class))).thenReturn(TAG_DTO);
        when(stepTransformer.toDtoList(anyList())).thenReturn(STEP_DTOS);
        List<CaseDTO> actualCaseDTOs = caseDTOsTransformer.toDtoList(cases);

        assertEquals(caseDtos, actualCaseDTOs);

        verify(tagTransformer).toDto(any(Tag.class));
        verify(stepTransformer).toDtoList(anyList());
    }

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

        CaseDTO caseDto = new CaseDTO();
        caseDto.setId(ID);
        caseDto.setName(NAME);
        caseDto.setDescription(DESCRIPTION);
        caseDto.setPriority(PRIORITY);
        caseDto.setComment(COMMENT);
        caseDto.setTags(TAG_DTOS);
        caseDto.setStatus(STATUS);
        caseDto.setSteps(STEP_DTOS);

        when(tagTransformer.toDto(any(Tag.class))).thenReturn(TAG_DTO);
        when(stepTransformer.toDtoList(anyList())).thenReturn(STEP_DTOS);
        CaseDTO actualCaseDTO = caseDTOsTransformer.toDto(caze);

        assertEquals(caseDto, actualCaseDTO);

        verify(tagTransformer).toDto(any(Tag.class));
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

        when(tagTransformer.fromDto(any(TagDTO.class))).thenReturn(TAG);
        Case actualCase = caseDTOsTransformer.fromDto(dto);

        assertEquals(caze, actualCase);

        verify(tagTransformer).fromDto(any(TagDTO.class));
    }

    @Test
    public void updateFromDto_CaseUpdateDTOWithNoNullValues_Success() {
        Case caze = new Case();

        CaseUpdateDTO dto = new CaseUpdateDTO();
        dto.setName(NEW_NAME);
        dto.setDescription(NEW_DESCRIPTION);
        dto.setPriority(NEW_PRIORITY);
        dto.setStatus(NEW_STATUS);
        dto.setTags(NEW_TAG_DTOS);
        dto.setComment(NEW_COMMENT);

        Case updatedCase = new Case();
        updatedCase.setName(NEW_NAME);
        updatedCase.setDescription(NEW_DESCRIPTION);
        updatedCase.setPriority(NEW_PRIORITY);
        updatedCase.setStatus(NEW_STATUS);
        updatedCase.setTags(NEW_TAGS);
        updatedCase.setComment(NEW_COMMENT);

        when(tagTransformer.fromDto(any(TagDTO.class))).thenReturn(TAG);

        Case actualUpdatedCase = caseDTOsTransformer.updateFromDto(dto, caze);

        assertThat(actualUpdatedCase, is(updatedCase));

        verify(tagTransformer).fromDto(any(TagDTO.class));
    }

    @Test
    public void updateFromDto_CaseUpdateDTOWithNullNameAndTags_Success() {
        Case caze = new Case();

        CaseUpdateDTO dto = new CaseUpdateDTO();
        dto.setDescription(NEW_DESCRIPTION);
        dto.setPriority(NEW_PRIORITY);
        dto.setStatus(NEW_STATUS);
        dto.setComment(NEW_COMMENT);

        Case updatedCase = new Case();
        updatedCase.setDescription(NEW_DESCRIPTION);
        updatedCase.setPriority(NEW_PRIORITY);
        updatedCase.setStatus(NEW_STATUS);
        updatedCase.setComment(NEW_COMMENT);

        Case actualUpdatedCase = caseDTOsTransformer.updateFromDto(dto, caze);

        assertThat(actualUpdatedCase, is(updatedCase));

        verifyZeroInteractions(tagTransformer);
    }

}