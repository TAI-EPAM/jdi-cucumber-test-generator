package com.epam.test_generator.controllers.stepsuggestion;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionCreateDTO;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionUpdateDTO;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.services.StepSuggestionService;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class StepSuggestionControllerTest {

    private static final long SIMPLE_PROJECT_ID = 1L;
    private static final long SIMPLE_STEP_SUGGESTION_ID = 2L;
    private static final String SEARCH_STRING = "text";
    private static final int NUMBER_OF_RETURN_PAGE = 10;
    private static final int PAGE_SIZE = 10;
    private static final int PAGE_NUMBER = 1;

    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private List<StepSuggestionDTO> stepSuggestionDTOS = Collections.emptyList();

    @Mock
    private StepSuggestionService stepSuggestionService;

    @InjectMocks
    private StepSuggestionController stepSuggestionController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(stepSuggestionController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();
    }

    @Test
    public void getStepsSuggestions_CorrectData_StatusOk() throws Exception {
        when(stepSuggestionService
            .getStepsSuggestions(SIMPLE_PROJECT_ID, StepType.ANY, PAGE_NUMBER, PAGE_SIZE))
            .thenReturn(stepSuggestionDTOS);

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID +
            "/step-suggestions/?stepType=" + StepType.ANY + "&page=" + PAGE_NUMBER + "&size="
            + PAGE_SIZE))
            .andExpect(status().isOk())
            .andExpect(content().string("[]"));

        verify(stepSuggestionService)
            .getStepsSuggestions(SIMPLE_PROJECT_ID, StepType.ANY, PAGE_NUMBER, PAGE_SIZE);
    }

    @Test
    public void getStepsSuggestionsByType_CorrectData_StatusOk() throws Exception {
        when(stepSuggestionService.getStepsSuggestionsByType(SIMPLE_PROJECT_ID, StepType.ANY))
            .thenReturn(stepSuggestionDTOS);

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID +
            "/step-suggestions/" + StepType.ANY))
            .andExpect(status().isOk())
            .andExpect(content().string("[]"));

        verify(stepSuggestionService).getStepsSuggestionsByType(SIMPLE_PROJECT_ID, StepType.ANY);
    }

    @Test
    public void addStepSuggestion_CorrectData_StatusOk() throws Exception {
        StepSuggestionCreateDTO stepSuggestionCreateDTO = new StepSuggestionCreateDTO();
        stepSuggestionCreateDTO.setContent("content");
        stepSuggestionCreateDTO.setType(StepType.ANY);
        StepSuggestionDTO stepSuggestionDTO = new StepSuggestionDTO("content", StepType.ANY);
        when(stepSuggestionService.addStepSuggestion(SIMPLE_PROJECT_ID, stepSuggestionCreateDTO))
            .thenReturn(stepSuggestionDTO);

        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID +
            "/step-suggestions/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepSuggestionCreateDTO)))
            .andExpect(status().isCreated());
        verify(stepSuggestionService).addStepSuggestion(SIMPLE_PROJECT_ID, stepSuggestionCreateDTO);
    }

    @Test
    public void addStepSuggestion_IncorrectDTO_StatusBadRequest() throws Exception {
        StepSuggestionCreateDTO stepSuggestionCreateDTO = new StepSuggestionCreateDTO();
        stepSuggestionCreateDTO.setType(StepType.ANY);

        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID +
            "/step-suggestions/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepSuggestionCreateDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateStepSuggestion_CorrectData_StatusOk() throws Exception {
        StepSuggestionUpdateDTO stepSuggestionUpdateDTO = new StepSuggestionUpdateDTO();
        stepSuggestionUpdateDTO.setType(StepType.ANY);
        stepSuggestionUpdateDTO.setVersion(0L);

        StepSuggestionDTO stepSuggestionDTO = new StepSuggestionDTO(SIMPLE_STEP_SUGGESTION_ID,
            "content", StepType.ANY);

        when(stepSuggestionService.updateStepSuggestion(SIMPLE_PROJECT_ID,
            SIMPLE_STEP_SUGGESTION_ID, stepSuggestionUpdateDTO))
            .thenReturn(stepSuggestionDTO);

        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID +
            "/step-suggestions/" + SIMPLE_STEP_SUGGESTION_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepSuggestionUpdateDTO)))
            .andExpect(status().isOk());

        verify(stepSuggestionService).updateStepSuggestion(SIMPLE_PROJECT_ID,
            SIMPLE_STEP_SUGGESTION_ID, stepSuggestionUpdateDTO);
    }

    @Test
    public void removeStepSuggestion_CorrectData_StatusOk() throws Exception {
        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID +
            "/step-suggestions/" + SIMPLE_STEP_SUGGESTION_ID))
            .andExpect(status().isOk());

        verify(stepSuggestionService).removeStepSuggestion(SIMPLE_PROJECT_ID,
            SIMPLE_STEP_SUGGESTION_ID);
    }

    @Test
    public void searchStepsSuggestions_CorrectData_StatusOk() throws Exception {
        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID +
            "/step-suggestions/search?text=" + SEARCH_STRING + "&pageNumber="
            + NUMBER_OF_RETURN_PAGE
            + "&pageSize=" + PAGE_SIZE))
            .andExpect(status().isOk());

        verify(stepSuggestionService).findStepsSuggestions(eq(SIMPLE_PROJECT_ID), eq(SEARCH_STRING),
            eq(NUMBER_OF_RETURN_PAGE), eq(PAGE_SIZE));
    }

    @Test
    public void searchStepsSuggestions_IncorrectInputData_StatusBadRequest() throws Exception {
        int incorrectNumberOfReturnPage = -1;
        int incorrectPageSize = -1;
        when(stepSuggestionService.findStepsSuggestions(eq(SIMPLE_PROJECT_ID), eq(SEARCH_STRING),
            eq(incorrectNumberOfReturnPage), eq(incorrectPageSize)))
            .thenThrow(BadRequestException.class);

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID +
            "/step-suggestions/search?text=" + SEARCH_STRING + "&pageNumber="
            + incorrectNumberOfReturnPage
            + "&pageSize=" + incorrectPageSize))
            .andExpect(status().isBadRequest());

        verify(stepSuggestionService).findStepsSuggestions(eq(SIMPLE_PROJECT_ID), eq(SEARCH_STRING),
            eq(incorrectNumberOfReturnPage), eq(incorrectPageSize));
    }

    @Test
    public void searchStepsSuggestions_IncorrectProjectId_StatusAccessDenied() throws Exception {
        Long incorrectProjectId = -1L;
        when(stepSuggestionService.findStepsSuggestions(eq(incorrectProjectId), eq(SEARCH_STRING),
            eq(NUMBER_OF_RETURN_PAGE), eq(PAGE_SIZE))).thenThrow(AccessDeniedException.class);

        mockMvc.perform(get("/projects/" + incorrectProjectId +
            "/step-suggestions/search?text=" + SEARCH_STRING + "&pageNumber="
            + NUMBER_OF_RETURN_PAGE
            + "&pageSize=" + PAGE_SIZE))
            .andExpect(status().isForbidden());

        verify(stepSuggestionService)
            .findStepsSuggestions(eq(incorrectProjectId), eq(SEARCH_STRING),
                eq(NUMBER_OF_RETURN_PAGE), eq(PAGE_SIZE));
    }
}