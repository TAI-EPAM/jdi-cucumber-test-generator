package com.epam.test_generator.controllers.stepsuggestion;

import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.entities.DefaultStepSuggestion;
import com.epam.test_generator.entities.StepType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultStepSuggestionTransformerTest {

    @InjectMocks
    private DefaultStepSuggestionTransformer defaultStepSuggestionTransformer;

    private static final Long ID = 1L;
    private static final String CONTENT = "some content";
    private static final StepType STEP_TYPE = StepType.GIVEN;
    private DefaultStepSuggestion defaultStepSuggestion;
    private StepSuggestionDTO stepSuggestionDto;

    @Before
    public void setUp() {
        defaultStepSuggestion = new DefaultStepSuggestion();
        defaultStepSuggestion.setId(ID);
        defaultStepSuggestion.setContent(CONTENT);
        defaultStepSuggestion.setType(STEP_TYPE);

        stepSuggestionDto = new StepSuggestionDTO();
        stepSuggestionDto.setId(ID);
        stepSuggestionDto.setContent(CONTENT);
        stepSuggestionDto.setType(STEP_TYPE);
    }

    @Test
    public void toDto_ValidStepSuggestion_Success() {
        StepSuggestionDTO expectedDto = stepSuggestionDto;

        StepSuggestionDTO actualDto = defaultStepSuggestionTransformer.toDto(defaultStepSuggestion);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    public void toDtoList_ValidStepSuggestionList_Success() {
        List<DefaultStepSuggestion> stepSuggestionList = Collections.singletonList(defaultStepSuggestion);

        StepSuggestionDTO expectedDto = stepSuggestionDto;
        List<StepSuggestionDTO> expectedDtoList = Collections.singletonList(expectedDto);

        List<StepSuggestionDTO> actualDtoList = defaultStepSuggestionTransformer.toDtoList(stepSuggestionList);

        assertEquals(expectedDtoList, actualDtoList);

    }
}