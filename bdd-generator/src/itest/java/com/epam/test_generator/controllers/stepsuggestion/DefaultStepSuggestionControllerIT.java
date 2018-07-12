package com.epam.test_generator.controllers.stepsuggestion;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.services.DefaultStepSuggestionService;
import com.epam.test_generator.services.exceptions.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class DefaultStepSuggestionControllerIT {

    private static final int PAGE_NUMBER = 1;
    private static final int PAGE_SIZE = 2;
    private static final StepType STEP_TYPE = StepType.GIVEN;
    private static final String SEARCH_STRING = "text";
    private static final int NUMBER_OF_RETURN_RESULTS = 10;
    private MockMvc mockMvc;
    private List<StepSuggestionDTO> stepSuggestionDTOS;

    @Mock
    private DefaultStepSuggestionService defaultStepSuggestionService;

    @InjectMocks
    private DefaultStepSuggestionController defaultStepSuggestionController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(defaultStepSuggestionController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();
        stepSuggestionDTOS = new ArrayList<>();
    }


    @Test
    public void getStepSuggestions_CorrectPageNumberAndPageSize_StatusOk() throws Exception {
        when(defaultStepSuggestionService.getStepsSuggestionsByTypeAndPage(StepType.ANY, PAGE_NUMBER, PAGE_SIZE))
                .thenReturn(stepSuggestionDTOS);

        mockMvc.perform(get("/step-suggestions?stepType=" + StepType.ANY  + "&page=" + PAGE_NUMBER + "&size=" + PAGE_SIZE))
                .andExpect(status().isOk());

        verify(defaultStepSuggestionService).getStepsSuggestionsByTypeAndPage(StepType.ANY, PAGE_NUMBER, PAGE_SIZE);
    }

    @Test
    public void getStepSuggestions_CorrectPageNumberAndPageSizeButThrowRuntimeException_StatusInternalServerError()
            throws Exception {
        when(defaultStepSuggestionService.getStepsSuggestionsByTypeAndPage(StepType.ANY, PAGE_NUMBER, PAGE_SIZE))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/step-suggestions?stepType=" + StepType.ANY  + "&page=" + PAGE_NUMBER + "&size=" + PAGE_SIZE))
                .andExpect(status().isInternalServerError());

        verify(defaultStepSuggestionService).getStepsSuggestionsByTypeAndPage(StepType.ANY, PAGE_NUMBER, PAGE_SIZE);
    }

    @Test
    public void getStepSuggestions_CorrectTypePageNumberAndPageSize_StatusOk() throws Exception {
        when(defaultStepSuggestionService.getStepsSuggestionsByTypeAndPage(STEP_TYPE, PAGE_NUMBER, PAGE_SIZE)).thenReturn
                (stepSuggestionDTOS);

        mockMvc.perform(get("/step-suggestions?stepType=" + STEP_TYPE  + "&page=" + PAGE_NUMBER + "&size=" + PAGE_SIZE))
                .andExpect(status().isOk());

        verify(defaultStepSuggestionService).getStepsSuggestionsByTypeAndPage(eq(STEP_TYPE), eq(PAGE_NUMBER), eq(PAGE_SIZE));
    }

    @Test
    public void getStepsSuggestionsPageByType_WithTypePageNumberAndPageSizeButThrowRuntimeException_StatusInternalServerError()
            throws Exception {
        when(defaultStepSuggestionService.getStepsSuggestionsByTypeAndPage(STEP_TYPE, PAGE_NUMBER, PAGE_SIZE))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/step-suggestions?stepType=" + STEP_TYPE + "&page=" + PAGE_NUMBER + "&size=" + PAGE_SIZE))
                .andExpect(status().isInternalServerError());

        verify(defaultStepSuggestionService).getStepsSuggestionsByTypeAndPage(eq(STEP_TYPE), eq(PAGE_NUMBER), eq(PAGE_SIZE));
    }

    @Test
    public void getStepsSuggestionsByType_CorrectType_StatusOk() throws Exception {
        when(defaultStepSuggestionService.getStepsSuggestionsByType(STEP_TYPE)).thenReturn
            (stepSuggestionDTOS);

        mockMvc.perform(get("/step-suggestions/" + STEP_TYPE))
            .andExpect(status().isOk());

        verify(defaultStepSuggestionService).getStepsSuggestionsByType(eq(STEP_TYPE));

    }

    @Test
    public void getStepsSuggestionsByType_ThrowRuntimeException_StatusInternalServerError()
        throws Exception {
        when(defaultStepSuggestionService.getStepsSuggestionsByType(STEP_TYPE))
            .thenThrow(new RuntimeException());

        mockMvc.perform(get("/step-suggestions/" + STEP_TYPE))
            .andExpect(status().isInternalServerError());

        verify(defaultStepSuggestionService).getStepsSuggestionsByType(eq(STEP_TYPE));
    }

    @Test
    public void searchStepsSuggestions_CorrectSearchString_StatusOk() throws Exception {
        mockMvc.perform(get("/step-suggestions/search?text=" + SEARCH_STRING + "&limit=" + NUMBER_OF_RETURN_RESULTS))
            .andExpect(status().isOk());

        verify(defaultStepSuggestionService).findStepsSuggestions(eq(SEARCH_STRING), eq(NUMBER_OF_RETURN_RESULTS));
    }

    @Test
    public void searchStepsSuggestions_IncorrectLimit_StatusBadRequest() throws Exception {
        int incorrectLimit = -1;
        when(defaultStepSuggestionService.findStepsSuggestions(eq(SEARCH_STRING), eq(incorrectLimit))).thenThrow(BadRequestException.class);
        mockMvc.perform(get("/step-suggestions/search?text=" + SEARCH_STRING + "&limit=" + incorrectLimit))
            .andExpect(status().isBadRequest());

        verify(defaultStepSuggestionService).findStepsSuggestions(eq(SEARCH_STRING), eq(incorrectLimit));
    }
}