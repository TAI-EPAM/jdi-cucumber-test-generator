package com.epam.test_generator.controllers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.dto.StepSuggestionCreateDTO;
import com.epam.test_generator.dto.StepSuggestionDTO;
import com.epam.test_generator.dto.StepSuggestionUpdateDTO;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.services.StepSuggestionService;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class StepSuggestionControllerTest {

    private static final long SIMPLE_AUTOCOMPLETE_ID = 1L;
    private static final StepType STEP_TYPE = StepType.GIVEN;
    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private StepSuggestionDTO stepSuggestionDTO;
    private StepSuggestionUpdateDTO stepSuggestionUpdateDTO;
    private List<StepSuggestionDTO> stepSuggestionDTOS;

    @Mock
    private StepSuggestionService stepSuggestionService;

    @InjectMocks
    private StepSuggestionController stepSuggestionController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(stepSuggestionController)
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
    public void getSuggestionsList_StepsSuggestion_StatusOk() throws Exception {
        when(stepSuggestionService.getStepsSuggestions()).thenReturn(stepSuggestionDTOS);

        mockMvc.perform(get("/stepSuggestions"))
            .andExpect(status().isOk());

        verify(stepSuggestionService).getStepsSuggestions();
    }

    @Test
    public void getSuggestionsList_ThrowRuntimeException_StatusInternalServerError()
        throws Exception {
        when(stepSuggestionService.getStepsSuggestions()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/stepSuggestions"))
            .andExpect(status().isInternalServerError());

        verify(stepSuggestionService).getStepsSuggestions();
    }

    @Test
    public void getStepsSuggestionsByType_StepsSuggestion_StatusOk() throws Exception {
        when(stepSuggestionService.getStepsSuggestionsByType(STEP_TYPE)).thenReturn
            (stepSuggestionDTOS);

        mockMvc.perform(get("/stepSuggestions/" + STEP_TYPE))
            .andExpect(status().isOk());

        verify(stepSuggestionService).getStepsSuggestionsByType(eq(STEP_TYPE));

    }

    @Test
    public void getStepsSuggestionsByType_ThrowRuntimeException_StatusInternalServerError()
        throws Exception {
        when(stepSuggestionService.getStepsSuggestionsByType(STEP_TYPE))
            .thenThrow(new RuntimeException());

        mockMvc.perform(get("/stepSuggestions/" + STEP_TYPE))
            .andExpect(status().isInternalServerError());

        verify(stepSuggestionService).getStepsSuggestionsByType(eq(STEP_TYPE));
    }


    @Test
    public void add_NewStepSuggestion_StatusOk() throws Exception {
        stepSuggestionDTO.setId(null);
        when(stepSuggestionService.addStepSuggestion(any(StepSuggestionCreateDTO.class)))
            .thenReturn(stepSuggestionDTO.getId());

        mockMvc.perform(post("/stepSuggestions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepSuggestionDTO)))
            .andExpect(status().isOk());

        verify(stepSuggestionService).addStepSuggestion(any(StepSuggestionCreateDTO.class));
    }

    @Test
    public void add_StepSuggestionWithNullContent_StatusBadRequest()
        throws Exception {
        stepSuggestionDTO.setId(null);
        stepSuggestionDTO.setContent(null);

        mockMvc.perform(post("/stepSuggestions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepSuggestionDTO)))
            .andExpect(status().isBadRequest());

        verify(stepSuggestionService, times(0)).addStepSuggestion(any(StepSuggestionCreateDTO.class));
    }

    @Test
    public void addStepSuggestion_ThrowRuntimeException_StatusInternalServerError()
        throws Exception {
        when(stepSuggestionService.addStepSuggestion(any(StepSuggestionCreateDTO.class)))
            .thenThrow(new RuntimeException());

        mockMvc.perform(post("/stepSuggestions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepSuggestionDTO)))
            .andExpect(status().isInternalServerError());

        verify(stepSuggestionService).addStepSuggestion(any(StepSuggestionCreateDTO.class));
    }

    @Test
    public void update_StepSuggestion_StatusOk() throws Exception {
        when(stepSuggestionService.getStepsSuggestion(anyLong())).thenReturn(stepSuggestionDTO);

        mockMvc.perform(put("/stepSuggestions/" + SIMPLE_AUTOCOMPLETE_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepSuggestionUpdateDTO)))
            .andExpect(status().isOk());

        verify(stepSuggestionService)
            .updateStepSuggestion(eq(SIMPLE_AUTOCOMPLETE_ID), any(StepSuggestionUpdateDTO.class));
    }

    @Test
    public void update_StepSuggestion_StatusConflict() throws Exception {
        doThrow(
            OptimisticLockingFailureException.class).when(stepSuggestionService)
            .updateStepSuggestion(anyLong(),
            any(StepSuggestionUpdateDTO.class));

        mockMvc.perform(put("/stepSuggestions/" + SIMPLE_AUTOCOMPLETE_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepSuggestionUpdateDTO)))
            .andExpect(status().isConflict());

        verify(stepSuggestionService)
            .updateStepSuggestion(eq(SIMPLE_AUTOCOMPLETE_ID), any(StepSuggestionUpdateDTO.class));
    }

    @Test
    public void update_StepSuggestionNotExist_StatusNotFound() throws Exception {
        doThrow(NotFoundException.class).when(stepSuggestionService)
            .updateStepSuggestion(anyLong(), any(StepSuggestionUpdateDTO.class));

        mockMvc.perform(put("/stepSuggestions/" + SIMPLE_AUTOCOMPLETE_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepSuggestionUpdateDTO)))
            .andExpect(status().isNotFound());

        verify(stepSuggestionService)
            .updateStepSuggestion(eq(SIMPLE_AUTOCOMPLETE_ID), any(StepSuggestionUpdateDTO.class));
    }

    @Test
    public void updateStepSuggestion_ThrowRuntimeException_StatusInternalServerError() throws Exception {
        doThrow(RuntimeException.class).when(stepSuggestionService)
            .updateStepSuggestion(anyLong(), any(StepSuggestionUpdateDTO.class));

        mockMvc.perform(put("/stepSuggestions/" + SIMPLE_AUTOCOMPLETE_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepSuggestionUpdateDTO)))
            .andExpect(status().isInternalServerError());

        verify(stepSuggestionService)
            .updateStepSuggestion(eq(SIMPLE_AUTOCOMPLETE_ID), any(StepSuggestionUpdateDTO.class));
    }

    @Test
    public void remove_StepSuggestion_StatusOk() throws Exception {
        when(stepSuggestionService.getStepsSuggestion(SIMPLE_AUTOCOMPLETE_ID))
            .thenReturn(stepSuggestionDTO);

        mockMvc.perform(delete("/stepSuggestions/" + SIMPLE_AUTOCOMPLETE_ID))
            .andExpect(status().isOk());

        verify(stepSuggestionService).removeStepSuggestion(eq(SIMPLE_AUTOCOMPLETE_ID));
    }

    @Test
    public void remove_StepSuggestionNotExist_StatusNotFound() throws Exception {
        doThrow(NotFoundException.class).when(stepSuggestionService)
            .removeStepSuggestion(anyLong());

        mockMvc.perform(delete("/stepSuggestions/" + SIMPLE_AUTOCOMPLETE_ID))
            .andExpect(status().isNotFound());

        verify(stepSuggestionService).removeStepSuggestion(eq(SIMPLE_AUTOCOMPLETE_ID));
    }

    @Test
    public void removeSuggestion_ThrowRuntimeException_StatusInternalServerError() throws Exception {
        doThrow(RuntimeException.class).when(stepSuggestionService).removeStepSuggestion(anyLong());

        mockMvc.perform(delete("/stepSuggestions/" + SIMPLE_AUTOCOMPLETE_ID))
            .andExpect(status().isInternalServerError());

        verify(stepSuggestionService).removeStepSuggestion(eq(SIMPLE_AUTOCOMPLETE_ID));
    }
}