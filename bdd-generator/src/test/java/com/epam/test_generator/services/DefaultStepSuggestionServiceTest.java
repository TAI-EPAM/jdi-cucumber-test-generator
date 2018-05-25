package com.epam.test_generator.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.epam.test_generator.dao.interfaces.DefaultStepSuggestionDAO;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.entities.DefaultStepSuggestion;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.controllers.stepsuggestion.DefaultStepSuggestionTransformer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class DefaultStepSuggestionServiceTest {

    private static final long SIMPLE_AUTOCOMPLETE_ID = 1L;
    private static final long SIMPLE_STEP_SUGGESTION_ID = 1L;
    private static final int PAGE_SIZE = 1;
    private static final String SEARCH_STRING = "I%20click";
    private static final int NUMBER_OF_RETURN_RESULTS = 10;

    @Mock
    private DefaultStepSuggestionTransformer defaultStepSuggestionTransformer;
    @Mock
    private DefaultStepSuggestionDAO defaultStepSuggestionDAO;
    @InjectMocks
    private DefaultStepSuggestionService defaultStepSuggestionService;
    private List<DefaultStepSuggestion> listSteps;
    private List<StepSuggestionDTO> expectedListSteps;

    @Before
    public void setUp() {
        listSteps = new ArrayList<>();
        listSteps.add(new DefaultStepSuggestion(1L, "DefaultStepSuggestion 1", StepType.GIVEN));
        listSteps.add(new DefaultStepSuggestion(2L, "DefaultStepSuggestion 2", StepType.THEN));

        expectedListSteps = new ArrayList<>();
        expectedListSteps.add(new StepSuggestionDTO(1L, "DefaultStepSuggestion 1", StepType.GIVEN));
        expectedListSteps.add(new StepSuggestionDTO(2L, "DefaultStepSuggestion 2", StepType.THEN));
    }

    @Test
    public void get_StepsSuggestions_Success() {
        when(defaultStepSuggestionDAO.findAll()).thenReturn(listSteps);
        when(defaultStepSuggestionTransformer.toDtoList(listSteps)).thenReturn(expectedListSteps);

        List<StepSuggestionDTO> getListStepsSuggestion = defaultStepSuggestionService
            .getStepsSuggestions();
        assertTrue(
            Arrays.deepEquals(expectedListSteps.toArray(), getListStepsSuggestion.toArray()));

        verify(defaultStepSuggestionDAO).findAll();
    }

    @Test
    public void get_StepsSuggestionsPage_Success() {
        Page<DefaultStepSuggestion> testPage = new PageImpl<>(listSteps.subList(0, 1));

        when(defaultStepSuggestionDAO.findAll(any(Pageable.class))).thenReturn(testPage);
        when(defaultStepSuggestionTransformer.toDto(any(DefaultStepSuggestion.class))).thenReturn(expectedListSteps.get(0));

        List<StepSuggestionDTO> getListStepsSuggestion = defaultStepSuggestionService
                .getStepsSuggestionsByTypeAndPage(StepType.ANY, 1, PAGE_SIZE);
        assertEquals(expectedListSteps.subList(0, 1), getListStepsSuggestion);

        verify(defaultStepSuggestionDAO).findAll(any(Pageable.class));
        verify(defaultStepSuggestionTransformer).toDto(any(DefaultStepSuggestion.class));
    }

    @Test
    public void get_StepsSuggestionsByTypePage1_Success() {
        Page<DefaultStepSuggestion> testPage = new PageImpl<>(listSteps.subList(0, 1));

        when(defaultStepSuggestionDAO.findAll(any(Pageable.class))).thenReturn(testPage);
        when(defaultStepSuggestionTransformer.toDto(any(DefaultStepSuggestion.class))).thenReturn(expectedListSteps.get(0));

        List<StepSuggestionDTO> getListStepsSuggestion = defaultStepSuggestionService
                .getStepsSuggestionsByTypeAndPage(StepType.GIVEN, 1, PAGE_SIZE);
        assertEquals(expectedListSteps.subList(0, 1), getListStepsSuggestion);

        verify(defaultStepSuggestionDAO).findAll(any(Pageable.class));
        verify(defaultStepSuggestionTransformer).toDto(any(DefaultStepSuggestion.class));
    }

    @Test
    public void get_StepsSuggestionsByTypePage2_Success() {
        Page<DefaultStepSuggestion> testPage = new PageImpl<>(listSteps.subList(1, 2));

        when(defaultStepSuggestionDAO.findAll(any(Pageable.class))).thenReturn(testPage);
        when(defaultStepSuggestionTransformer.toDto(any(DefaultStepSuggestion.class))).thenReturn(expectedListSteps.get(1));

        List<StepSuggestionDTO> getListStepsSuggestion = defaultStepSuggestionService
                .getStepsSuggestionsByTypeAndPage(StepType.THEN, 2, PAGE_SIZE);
        assertEquals(expectedListSteps.subList(1, 2), getListStepsSuggestion);

        verify(defaultStepSuggestionDAO).findAll(any(Pageable.class));
        verify(defaultStepSuggestionTransformer).toDto(any(DefaultStepSuggestion.class));
    }

    @Test
    public void get_StepSuggestionById_Success() {
        DefaultStepSuggestion expected = new DefaultStepSuggestion(SIMPLE_STEP_SUGGESTION_ID,
            "DefaultStepSuggestion 1",
            StepType.GIVEN);
        StepSuggestionDTO expectedDTO = new StepSuggestionDTO(SIMPLE_STEP_SUGGESTION_ID,
            "DefaultStepSuggestion 1", StepType.GIVEN);

        when(defaultStepSuggestionDAO.findById(anyLong())).thenReturn(Optional.of(expected));
        when(defaultStepSuggestionTransformer.toDto(any(DefaultStepSuggestion.class)))
            .thenReturn(expectedDTO);

        assertEquals(defaultStepSuggestionService.getStepSuggestion(SIMPLE_STEP_SUGGESTION_ID),
            expectedDTO);

        verify(defaultStepSuggestionDAO).findById(anyLong());
        verify(defaultStepSuggestionTransformer).toDto(any(DefaultStepSuggestion.class));
    }

    @Test(expected = NotFoundException.class)
    public void get_StepSuggestionById_NotFoundException() {
        when(defaultStepSuggestionDAO.findById(anyLong())).thenReturn(Optional.empty());

        defaultStepSuggestionService.getStepSuggestion(SIMPLE_STEP_SUGGESTION_ID);
    }

    @Test
    public void get_StepSuggestionByType_Success() {
        StepSuggestionDTO expectedDTO = new StepSuggestionDTO(2L, "DefaultStepSuggestion 2",
            StepType.WHEN);
        DefaultStepSuggestion expected = new DefaultStepSuggestion(2L, "DefaultStepSuggestion 2", StepType.WHEN);

        when(defaultStepSuggestionDAO.findAll()).thenReturn(Collections.singletonList(expected));
        when(defaultStepSuggestionTransformer.toDto(any(DefaultStepSuggestion.class)))
            .thenReturn(expectedDTO);

        List<StepSuggestionDTO> actual = defaultStepSuggestionService.getStepsSuggestionsByType
            (StepType.WHEN);

        assertEquals(Collections.singletonList(expectedDTO), actual);

        verify(defaultStepSuggestionDAO).findAll();
        verify(defaultStepSuggestionTransformer).toDto(any(DefaultStepSuggestion.class));
    }

    @Test
    public void findStepsSuggestions_CorrectSearchString_Success() {
        when(defaultStepSuggestionDAO.findByContentIgnoreCaseContaining(eq(SEARCH_STRING), any())).thenReturn(listSteps);
        when(defaultStepSuggestionTransformer.toDtoList(listSteps)).thenReturn(expectedListSteps);

        defaultStepSuggestionService.findStepsSuggestions(SEARCH_STRING, NUMBER_OF_RETURN_RESULTS);

        verify(defaultStepSuggestionDAO).findByContentIgnoreCaseContaining(eq(SEARCH_STRING), any());
        verify(defaultStepSuggestionTransformer).toDtoList(listSteps);
    }

}