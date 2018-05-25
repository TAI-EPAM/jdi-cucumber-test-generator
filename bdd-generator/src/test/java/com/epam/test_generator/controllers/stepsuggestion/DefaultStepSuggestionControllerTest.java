package com.epam.test_generator.controllers.stepsuggestion;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.controllers.stepsuggestion.DefaultStepSuggestionController;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionUpdateDTO;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.services.DefaultStepSuggestionService;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class DefaultStepSuggestionControllerTest {

    private static final long SIMPLE_AUTOCOMPLETE_ID = 1L;
    private static final int PAGE_NUMBER = 1;
    private static final int PAGE_SIZE = 2;
    private static final StepType STEP_TYPE = StepType.GIVEN;
    private static final String SEARCH_STRING = "I%20click";
    private static final int NUMBER_OF_RETURN_RESULTS = 10;
    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private StepSuggestionDTO stepSuggestionDTO;
    private StepSuggestionUpdateDTO stepSuggestionUpdateDTO;
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
        stepSuggestionDTO = new StepSuggestionDTO();
        stepSuggestionDTO.setId(SIMPLE_AUTOCOMPLETE_ID);
        stepSuggestionDTO.setContent("Some step description");
        stepSuggestionDTO.setType(StepType.GIVEN);
        stepSuggestionDTOS = new ArrayList<>();

        stepSuggestionUpdateDTO = new StepSuggestionUpdateDTO("step content", StepType.GIVEN, 0L);
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
        int limit = -1;
        mockMvc.perform(get("/step-suggestions/search?text=" + SEARCH_STRING + "&limit=" + limit))
            .andExpect(status().isBadRequest());
    }
}