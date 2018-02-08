package com.epam.test_generator.controllers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.services.SuitService;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class SuitControllerTest {

    private static final long SIMPLE_PROJECT_ID = 0L;
    private static final long TEST_SUIT_ID = 1L;
    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private SuitDTO suitDTO;
    @Mock
    private SuitService suitService;

    @InjectMocks
    private SuitController suitController;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(suitController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();
        suitDTO = new SuitDTO();
        suitDTO.setId(TEST_SUIT_ID);
        suitDTO.setName("Suit name");
        suitDTO.setRowNumber(1);
        suitDTO.setPriority(1);
        suitDTO.setDescription("Suit description");
    }

    @Test
    public void get_Suits_StatusOk() throws Exception {
        when(suitService.getSuitsFromProject(SIMPLE_PROJECT_ID))
            .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(suitService).getSuitsFromProject(SIMPLE_PROJECT_ID);
    }

    @Test
    public void getSuits_ThrowRuntimeException_StatusInternalServerError() throws Exception {
        when(suitService.getSuitsFromProject(SIMPLE_PROJECT_ID)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits"))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        verify(suitService).getSuitsFromProject(SIMPLE_PROJECT_ID);
    }

    @Test
    public void get_Suit_StatusOk() throws Exception {
        when(suitService.getSuitDTO(anyLong(), anyLong())).thenReturn(suitDTO);

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID))
            .andDo(print())
            .andExpect(status().isOk());

        verify(suitService).getSuitDTO(eq(SIMPLE_PROJECT_ID), eq(TEST_SUIT_ID));
    }

    @Test
    public void get_SuitNotExist_StatusNotFound() throws Exception {
        when(suitService.getSuitDTO(anyLong(), anyLong())).thenThrow(new NotFoundException());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(suitService).getSuitDTO(eq(SIMPLE_PROJECT_ID), eq(TEST_SUIT_ID));
    }

    @Test
    public void getSuit_ThrowRuntimeException_StatusInternalServerError() throws Exception {
        when(suitService.getSuitDTO(anyLong(), anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        verify(suitService).getSuitDTO(eq(SIMPLE_PROJECT_ID), eq(TEST_SUIT_ID));
    }

    @Test
    public void update_Suit_StatusOk() throws Exception {
        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitDTO)))
            .andDo(print())
            .andExpect(status().isOk());

        verify(suitService).updateSuit(anyLong(), anyLong(), any(SuitDTO.class));
    }

    @Test
    public void update_SuitWithNullName_StatusBadRequest() throws Exception {
        suitDTO.setName(null);

        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(suitService, times(0)).updateSuit(anyLong(), anyLong(), any(SuitDTO.class));
    }

    @Test
    public void update_SuitWithMoreThanTheRequiredPriority_StatusBadRequest() throws Exception {
        suitDTO.setPriority(6);

        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(suitService, times(0)).updateSuit(anyLong(), anyLong(), any(SuitDTO.class));
    }

    @Test
    public void update_SuitWithLessThanTheRequiredPriority_StatusBadRequest() throws Exception {
        suitDTO.setPriority(-1);

        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(suitService, times(0)).updateSuit(anyLong(), anyLong(), any(SuitDTO.class));
    }

    @Test
    public void update_SuitNotExist_StatusNotFound() throws Exception {
        doThrow(NotFoundException.class).when(suitService)
            .updateSuit(anyLong(), anyLong(), any(SuitDTO.class));

        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitDTO)))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(suitService).updateSuit(anyLong(), anyLong(), any(SuitDTO.class));
    }

    @Test
    public void updateSuit_ThrowRuntimeException_StatusInternalServerError() throws Exception {
        doThrow(RuntimeException.class).when(suitService)
            .updateSuit(anyLong(), anyLong(), any(SuitDTO.class));

        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitDTO)))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        verify(suitService).updateSuit(anyLong(), anyLong(), any(SuitDTO.class));
    }

    @Test
    public void remove_Suit_StatusOk() throws Exception {
        doNothing().when(suitService).removeSuit(anyLong(), anyLong());

        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID))
            .andDo(print())
            .andExpect(status().isOk());

        verify(suitService).removeSuit(anyLong(), anyLong());
    }

    @Test
    public void remove_SuitNotExist_StatusNotFound() throws Exception {
        doThrow(NotFoundException.class).when(suitService).removeSuit(anyLong(), anyLong());

        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(suitService).removeSuit(anyLong(), anyLong());
    }

    @Test
    public void removeSuit_ThrowRuntimeException_StatusInternalServerError() throws Exception {
        doThrow(RuntimeException.class).when(suitService).removeSuit(anyLong(), anyLong());

        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        verify(suitService).removeSuit(anyLong(), anyLong());
    }

    @Test
    public void add_Suit_StatusCreated() throws Exception {
        suitDTO.setId(null);
        when(suitService.addSuit(anyLong(), any(SuitDTO.class))).thenReturn(TEST_SUIT_ID);

        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitDTO)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().string(String.valueOf(TEST_SUIT_ID)));

        verify(suitService).addSuit(anyLong(), any(SuitDTO.class));
    }

    @Test
    public void add_SuitWithNullName_StatusBadRequest() throws Exception {
        suitDTO.setId(null);
        suitDTO.setName(null);
        when(suitService.addSuit(anyLong(), any(SuitDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(suitService, times(0)).addSuit(anyLong(), any(SuitDTO.class));
    }

    @Test
    public void add_SuitWithMoreThanTheRequiredPriority_StatusBadRequest() throws Exception {
        suitDTO.setId(null);
        suitDTO.setPriority(6);
        when(suitService.addSuit(anyLong(), any(SuitDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(suitService, times(0)).addSuit(anyLong(), any(SuitDTO.class));
    }

    @Test
    public void add_SuitWithLessThanTheRequiredPriority_StatusbadRequest() throws Exception {
        suitDTO.setId(null);
        suitDTO.setPriority(-1);
        when(suitService.addSuit(anyLong(), any(SuitDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(suitService, times(0)).addSuit(anyLong(), any(SuitDTO.class));
    }

    @Test
    public void addSuit_ThrowRuntimeException_StatusInternalServerError() throws Exception {
        suitDTO.setId(null);
        when(suitService.addSuit(anyLong(), any(SuitDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitDTO)))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        verify(suitService).addSuit(anyLong(), any(SuitDTO.class));
    }
}