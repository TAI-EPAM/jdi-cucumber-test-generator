package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.StepSuggestionDAO;
import com.epam.test_generator.dto.StepSuggestionDTO;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.transformers.StepSuggestionTransformer;
import com.epam.test_generator.entities.StepType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StepSuggestionServiceTest {

    private static final long SIMPLE_AUTOCOMPLETE_ID = 1L;

    @Mock
    private StepSuggestionTransformer stepSuggestionTransformer;

    @Mock
    private StepSuggestionDAO stepSuggestionDAO;

    @InjectMocks
    private StepSuggestionService stepSuggestionService;

    private List<StepSuggestion> listSteps;
    private List<StepSuggestionDTO> expectedListSteps;

    private static final long SIMPLE_STEP_SUGGESTION_ID = 1L;

    @Before
    public void setUp() {
        listSteps = new ArrayList<>();
        listSteps.add(new StepSuggestion(1L, "StepSuggestion 1", StepType.GIVEN));
        listSteps.add(new StepSuggestion(2L, "StepSuggestion 2", StepType.THEN));

        expectedListSteps = new ArrayList<>();
        expectedListSteps.add(new StepSuggestionDTO(1L, "StepSuggestion 1", StepType.GIVEN.ordinal()));
        expectedListSteps.add(new StepSuggestionDTO(2L, "StepSuggestion 2", StepType.THEN.ordinal()));
    }

    @Test
    public void getStepsSuggestionsTest() throws Exception {
        when(stepSuggestionDAO.findAll()).thenReturn(listSteps);
        when(stepSuggestionTransformer.toDtoList(listSteps)).thenReturn(expectedListSteps);

        List<StepSuggestionDTO> getListStepsSuggestion = stepSuggestionService.getStepsSuggestions();
        assertEquals(true, Arrays.deepEquals(expectedListSteps.toArray(), getListStepsSuggestion.toArray()));

        verify(stepSuggestionDAO).findAll();
    }

    @Test
    public void getStepSuggestionByIdTest() {
        StepSuggestion expected = new StepSuggestion(SIMPLE_STEP_SUGGESTION_ID, "StepSuggestion 1", StepType.GIVEN);
        StepSuggestionDTO expectedDTO = new StepSuggestionDTO(SIMPLE_STEP_SUGGESTION_ID, "StepSuggestion 1", StepType.GIVEN.ordinal());

        when(stepSuggestionService.getStepsSuggestion(anyLong())).thenReturn(expectedDTO);
        when(stepSuggestionDAO.findOne(anyLong())).thenReturn(expected);
        when(stepSuggestionTransformer.toDto(any(StepSuggestion.class))).thenReturn(expectedDTO);

        assertEquals(stepSuggestionService.getStepsSuggestion(1L), expectedDTO);

        verify(stepSuggestionDAO).findOne(anyLong());
    }

    @Test
    public void getStepSuggestionByTypeTest() {
        StepSuggestionDTO expectedDTO = new StepSuggestionDTO(2L, "StepSuggestion 2", StepType.WHEN.ordinal());
        StepSuggestion expected = new StepSuggestion(2L, "StepSuggestion 2", StepType.WHEN);

        when(stepSuggestionDAO.findAll()).thenReturn(Collections.singletonList(expected));
        when(stepSuggestionTransformer.toDtoList(anyList())).thenReturn(Collections.singletonList(expectedDTO));

        List<StepSuggestionDTO> actual = stepSuggestionService.getStepsSuggestionsByType(0);

        assertEquals(Collections.singletonList(expectedDTO), actual);

        verify(stepSuggestionDAO).findAll();
    }

    @Test
    public void testAddStepSuggestionTest() throws Exception {
        StepSuggestion newStepSuggestion = new StepSuggestion(3L, "StepSuggestion 3", StepType.AND);
        StepSuggestionDTO newStepSuggestionDTO = new StepSuggestionDTO(3L, "StepSuggestion 3", StepType.AND.ordinal());

        when(stepSuggestionDAO.save(any(StepSuggestion.class))).thenReturn(newStepSuggestion);
        when(stepSuggestionTransformer.toDto(any(StepSuggestion.class))).thenReturn(newStepSuggestionDTO);
        when(stepSuggestionTransformer.fromDto(any(StepSuggestionDTO.class))).thenReturn(newStepSuggestion);

        Long id = stepSuggestionService.addStepSuggestion(newStepSuggestionDTO);
        assertEquals(newStepSuggestionDTO.getId(), id);

        verify(stepSuggestionDAO).save(any(StepSuggestion.class));
    }

    @Test
    public void updateStepSuggestionTest() {
        StepSuggestionDTO expectedDTO = new StepSuggestionDTO(2L, "StepSuggestion 2", StepType.WHEN.ordinal());
        StepSuggestion expected = new StepSuggestion(2L, "StepSuggestion 2", StepType.WHEN);

        when(stepSuggestionDAO.findOne(anyLong())).thenReturn(expected);
        when(stepSuggestionService.getStepsSuggestion(anyLong())).thenReturn(expectedDTO);

        stepSuggestionService.updateStepSuggestion(SIMPLE_STEP_SUGGESTION_ID, expectedDTO);
        StepSuggestionDTO actual = stepSuggestionService.getStepsSuggestion(2L);
        assertEquals(expectedDTO, actual);

        verify(stepSuggestionDAO).save(any(StepSuggestion.class));
    }

    @Test
    public void removeStepSuggestionTest() throws Exception {
        stepSuggestionService.removeStepSuggestion(SIMPLE_AUTOCOMPLETE_ID);

        verify(stepSuggestionDAO).delete(SIMPLE_AUTOCOMPLETE_ID);
    }

}