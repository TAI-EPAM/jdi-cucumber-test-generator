package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.StepSuggestionDTO;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.services.StepSuggestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class StepSuggestionControllerTest {

	private ObjectMapper mapper = new ObjectMapper();

	private MockMvc mockMvc;

	private StepSuggestionDTO stepSuggestionDTO;

	private static final long SIMPLE_AUTOCOMPLETE_ID = 1L;

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
        stepSuggestionDTO.setType(StepType.GIVEN.ordinal());
        stepSuggestionDTOS = new ArrayList<>();
    }

    @Test
    public void getSuggestionsList_return200whenGetStepsSuggestion() throws Exception {
        when(stepSuggestionService.getStepsSuggestions()).thenReturn(stepSuggestionDTOS);

        mockMvc.perform(get("/stepSuggestions"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(stepSuggestionService).getStepsSuggestions();
    }

    @Test
    public void getSuggestionsList_return500whenGetStepsSuggestion() throws Exception {
        when(stepSuggestionService.getStepsSuggestions()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/stepSuggestions"))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(stepSuggestionService).getStepsSuggestions();
    }

    @Test
    public void getStepsSuggestionsByType_return200whenGetStepsSuggestion() throws Exception{
        when(stepSuggestionService.getStepsSuggestionsByType(SIMPLE_AUTOCOMPLETE_ID)).thenReturn(stepSuggestionDTOS);

        mockMvc.perform(get("/stepSuggestions/" + SIMPLE_AUTOCOMPLETE_ID))
                .andDo(print())
                .andExpect(status().isOk());

        verify(stepSuggestionService).getStepsSuggestionsByType(eq(SIMPLE_AUTOCOMPLETE_ID));

    }

    @Test
    public void getStepsSuggestionsByType_return500whenGetStepsSuggestion() throws Exception {
        when(stepSuggestionService.getStepsSuggestionsByType(SIMPLE_AUTOCOMPLETE_ID)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/stepSuggestions/" + SIMPLE_AUTOCOMPLETE_ID))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(stepSuggestionService).getStepsSuggestionsByType(eq(SIMPLE_AUTOCOMPLETE_ID));
    }


    @Test
    public void testAddStepSuggestion_return200whenAddNewStepSuggestion() throws Exception {
        stepSuggestionDTO.setId(null);
        when(stepSuggestionService.addStepSuggestion(any(StepSuggestionDTO.class))).thenReturn(stepSuggestionDTO.getId());

        mockMvc.perform(post("/stepSuggestions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepSuggestionDTO)))
                .andExpect(status().isOk());

        verify(stepSuggestionService).addStepSuggestion(any(StepSuggestionDTO.class));
    }

    @Test
    public void testAddStepSuggestion_return400whenAddStepSuggestionWithNullContent() throws Exception {
        stepSuggestionDTO.setId(null);
        stepSuggestionDTO.setContent(null);

        mockMvc.perform(post("/stepSuggestions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepSuggestionDTO)))
                .andExpect(status().isBadRequest());

        verify(stepSuggestionService,times(0)).addStepSuggestion(any(StepSuggestionDTO.class));
    }

    @Test
    public void testAddStepSuggestion_return500whenAddStepsSuggestion() throws Exception {

        when(stepSuggestionService.addStepSuggestion(any(StepSuggestionDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/stepSuggestions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepSuggestionDTO)))
                .andExpect(status().isInternalServerError());

        verify(stepSuggestionService).addStepSuggestion(any(StepSuggestionDTO.class));
    }



    @Test
    public void testUpdateStepSuggestion_return200whenUpdateStepSuggestion() throws Exception{

        when(stepSuggestionService.getStepsSuggestion(anyLong())).thenReturn(stepSuggestionDTO);

        mockMvc.perform(put("/stepSuggestions/" + SIMPLE_AUTOCOMPLETE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepSuggestionDTO)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(stepSuggestionService).updateStepSuggestion(eq(SIMPLE_AUTOCOMPLETE_ID),any(StepSuggestionDTO.class));
    }

    @Test
    public void testUpdateStepSuggestion_return201WhenStepSuggestionNotExist() throws Exception{

        when(stepSuggestionService.getStepsSuggestion(anyLong())).thenReturn(null);

        mockMvc.perform(put("/stepSuggestions/" + SIMPLE_AUTOCOMPLETE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepSuggestionDTO)))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(stepSuggestionService,times(0)).updateStepSuggestion(eq(SIMPLE_AUTOCOMPLETE_ID),any(StepSuggestionDTO.class));
    }
    @Test
    public void testUpdateStepSuggestion_return500() throws Exception{

        when(stepSuggestionService.getStepsSuggestion(anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(put("/stepSuggestions/" + SIMPLE_AUTOCOMPLETE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepSuggestionDTO)))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(stepSuggestionService,times(0)).updateStepSuggestion(eq(SIMPLE_AUTOCOMPLETE_ID),any(StepSuggestionDTO.class));
    }

     @Test
    public void testRemoveSuggestion_return200whenRemoveStepSuggestion() throws Exception {
        when(stepSuggestionService.getStepsSuggestion(SIMPLE_AUTOCOMPLETE_ID)).thenReturn(stepSuggestionDTO);
        mockMvc.perform(delete("/stepSuggestions/" + SIMPLE_AUTOCOMPLETE_ID))
                .andExpect(status().isOk());

        verify(stepSuggestionService).removeStepSuggestion(eq(SIMPLE_AUTOCOMPLETE_ID));
    }

    @Test
    public void testRemoveSuggestion_return400whenStepSuggestionNotExist() throws Exception {
        when(stepSuggestionService.getStepsSuggestion(SIMPLE_AUTOCOMPLETE_ID)).thenReturn(null);
        mockMvc.perform(delete("/stepSuggestions/" + SIMPLE_AUTOCOMPLETE_ID))
                .andExpect(status().isNotFound());

        verify(stepSuggestionService,times(0)).removeStepSuggestion(eq(SIMPLE_AUTOCOMPLETE_ID));
    }




    @Test
    public void testRemoveSuggestion_return500whenRemoveStepSuggestion() throws Exception {
        when(stepSuggestionService.getStepsSuggestion(SIMPLE_AUTOCOMPLETE_ID)).thenThrow(new RuntimeException());

        mockMvc.perform(delete("/stepSuggestions/" + SIMPLE_AUTOCOMPLETE_ID))
                .andExpect(status().isInternalServerError());

        verify(stepSuggestionService,times(0)).removeStepSuggestion(eq(SIMPLE_AUTOCOMPLETE_ID));
    }
}