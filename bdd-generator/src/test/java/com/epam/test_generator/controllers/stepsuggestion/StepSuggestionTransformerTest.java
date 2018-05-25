package com.epam.test_generator.controllers.stepsuggestion;

import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionCreateDTO;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionUpdateDTO;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.entities.StepType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class StepSuggestionTransformerTest {

    @InjectMocks
    private StepSuggestionTransformer stepSuggestionTransformer;

    private static final Long ID = 1L;
    private static final String CONTENT = "some content";
    private static final String NEW_CONTENT = "new content";
    private static final StepType STEP_TYPE = StepType.GIVEN;
    private static final StepType NEW_STEP_TYPE = StepType.WHEN;
    private static final Long VERSION = 1L;
    private StepSuggestion stepSuggestion;
    private StepSuggestionDTO stepSuggestionDto;

    @Before
    public void setUp() {
        stepSuggestion = new StepSuggestion();
        stepSuggestion.setId(ID);
        stepSuggestion.setContent(CONTENT);
        stepSuggestion.setType(STEP_TYPE);

        stepSuggestionDto = new StepSuggestionDTO();
        stepSuggestionDto.setId(ID);
        stepSuggestionDto.setContent(CONTENT);
        stepSuggestionDto.setType(STEP_TYPE);
    }

    @Test
    public void toDto_ValidStepSuggestion_Success() {
        StepSuggestionDTO expectedDto = stepSuggestionDto;

        StepSuggestionDTO actualDto = stepSuggestionTransformer.toDto(stepSuggestion);

        assertEquals(expectedDto, actualDto);

    }

    @Test
    public void toDtoList_ValidStepSuggestionList_Success() {
        List<StepSuggestion> stepSuggestionList = Collections.singletonList(stepSuggestion);

        StepSuggestionDTO expectedDto = stepSuggestionDto;
        List<StepSuggestionDTO> expectedDtoList = Collections.singletonList(expectedDto);

        List<StepSuggestionDTO> actualDtoList = stepSuggestionTransformer.toDtoList(stepSuggestionList);

        assertEquals(expectedDtoList, actualDtoList);

    }

    @Test
    public void fromDto_ValidStepSuggestionCreateDTO_Success() {
        StepSuggestionCreateDTO dto = new StepSuggestionCreateDTO();
        dto.setContent(CONTENT);
        dto.setType(STEP_TYPE);

        StepSuggestion expectedStepSuggestion = new StepSuggestion();
        expectedStepSuggestion.setContent(CONTENT);
        expectedStepSuggestion.setType(STEP_TYPE);

        StepSuggestion actualStepSuggestion = stepSuggestionTransformer.fromDto(dto);

        assertEquals(expectedStepSuggestion.getContent(), actualStepSuggestion.getContent());
        assertEquals(expectedStepSuggestion.getType(), actualStepSuggestion.getType());
    }

    @Test
    public void updateFromDto_ValidStepSuggestionUpdateDTO() {
        StepSuggestion stepSuggestionToUpdate = stepSuggestion;

        StepSuggestionUpdateDTO dto = new StepSuggestionUpdateDTO();
        dto.setContent(NEW_CONTENT);
        dto.setType(NEW_STEP_TYPE);
        dto.setVersion(VERSION);

        StepSuggestion expectedStepSuggestion = new StepSuggestion();
        expectedStepSuggestion.setContent(NEW_CONTENT);
        expectedStepSuggestion.setType(NEW_STEP_TYPE);

        stepSuggestionTransformer.updateFromDto(stepSuggestionToUpdate, dto);

        assertEquals(expectedStepSuggestion.getContent(), stepSuggestionToUpdate.getContent());
        assertEquals(expectedStepSuggestion.getType(), stepSuggestionToUpdate.getType());

    }
}