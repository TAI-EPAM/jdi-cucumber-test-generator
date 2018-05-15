package com.epam.test_generator.controllers.suit;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.controllers.suit.request.SuitCreateDTO;
import com.epam.test_generator.controllers.suit.request.SuitUpdateDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.services.SuitService;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
    private SuitCreateDTO suitCreateDTO;
    private SuitUpdateDTO suitUpdateDTO;

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
        suitDTO.setDisplayedStatusName(Status.NOT_RUN.getStatusName());

        suitCreateDTO = new SuitCreateDTO();
        suitCreateDTO.setName("Suit name");
        suitCreateDTO.setPriority(1);

        suitUpdateDTO = new SuitUpdateDTO();
        suitUpdateDTO.setPriority(5);
    }

    @Test
    public void get_Suits_StatusOk() throws Exception {
        when(suitService.getSuitsFromProject(SIMPLE_PROJECT_ID))
            .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits"))
            .andExpect(status().isOk());

        verify(suitService).getSuitsFromProject(SIMPLE_PROJECT_ID);
    }

    @Test
    public void getSuits_ThrowRuntimeException_StatusInternalServerError() throws Exception {
        when(suitService.getSuitsFromProject(SIMPLE_PROJECT_ID)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits"))
            .andExpect(status().isInternalServerError());

        verify(suitService).getSuitsFromProject(SIMPLE_PROJECT_ID);
    }

    @Test
    public void get_Suit_StatusOk() throws Exception {
        when(suitService.getSuitDTO(anyLong(), anyLong())).thenReturn(suitDTO);

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID))
            .andExpect(status().isOk());

        verify(suitService).getSuitDTO(eq(SIMPLE_PROJECT_ID), eq(TEST_SUIT_ID));
    }

    @Test
    public void get_SuitNotExist_StatusNotFound() throws Exception {
        when(suitService.getSuitDTO(anyLong(), anyLong())).thenThrow(new NotFoundException());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID))
            .andExpect(status().isNotFound());

        verify(suitService).getSuitDTO(eq(SIMPLE_PROJECT_ID), eq(TEST_SUIT_ID));
    }

    @Test
    public void getSuit_ThrowRuntimeException_StatusInternalServerError() throws Exception {
        when(suitService.getSuitDTO(anyLong(), anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID))
            .andExpect(status().isInternalServerError());

        verify(suitService).getSuitDTO(eq(SIMPLE_PROJECT_ID), eq(TEST_SUIT_ID));
    }

    @Test
    public void updateSuit_StatusOk() throws Exception {
        doReturn(suitDTO).when(suitService)
                .updateSuit(anyLong(), anyLong(), any(SuitUpdateDTO.class));

        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(suitUpdateDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is(suitDTO.getName())))
                .andExpect(jsonPath("$.priority", is(suitDTO.getPriority())));

        verify(suitService).updateSuit(anyLong(), anyLong(), any(SuitUpdateDTO.class));
        verifyNoMoreInteractions(suitService);
    }

    @Test
    public void update_SuitWithMoreThanTheRequiredPriority_StatusBadRequest() throws Exception {
        suitUpdateDTO.setPriority(6);

        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitUpdateDTO)))
            .andExpect(status().isBadRequest());

        verify(suitService, times(0)).updateSuit(anyLong(), anyLong(), any(SuitUpdateDTO.class));
    }

    @Test
    public void update_SuitWithLessThanTheRequiredPriority_StatusBadRequest() throws Exception {
        suitUpdateDTO.setPriority(-1);

        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitUpdateDTO)))
            .andExpect(status().isBadRequest());

        verify(suitService, times(0)).updateSuit(anyLong(), anyLong(), any(SuitUpdateDTO.class));
    }

    @Test
    public void update_SuitNotExist_StatusNotFound() throws Exception {
        doThrow(NotFoundException.class).when(suitService)
            .updateSuit(anyLong(), anyLong(), any(SuitUpdateDTO.class));

        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitUpdateDTO)))
            .andExpect(status().isNotFound());

        verify(suitService).updateSuit(anyLong(), anyLong(), any(SuitUpdateDTO.class));
    }

    @Test
    public void updateSuit_ThrowRuntimeException_StatusInternalServerError() throws Exception {
        doThrow(RuntimeException.class).when(suitService)
            .updateSuit(anyLong(), anyLong(), any(SuitUpdateDTO.class));

        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitUpdateDTO)))
            .andExpect(status().isInternalServerError());

        verify(suitService).updateSuit(anyLong(), anyLong(), any(SuitUpdateDTO.class));
    }

    @Test
    public void remove_Suit_StatusOk() throws Exception {
        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID))
            .andExpect(status().isOk());

        verify(suitService).removeSuit(anyLong(), anyLong());
    }

    @Test
    public void remove_SuitNotExist_StatusNotFound() throws Exception {
        doThrow(NotFoundException.class).when(suitService).removeSuit(anyLong(), anyLong());

        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID))
            .andExpect(status().isNotFound());

        verify(suitService).removeSuit(anyLong(), anyLong());
    }

    @Test
    public void removeSuit_ThrowRuntimeException_StatusInternalServerError() throws Exception {
        doThrow(RuntimeException.class).when(suitService).removeSuit(anyLong(), anyLong());

        mockMvc.perform(delete("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + TEST_SUIT_ID))
            .andExpect(status().isInternalServerError());

        verify(suitService).removeSuit(anyLong(), anyLong());
    }

    @Test
    public void add_Suit_StatusCreated() throws Exception {
        suitDTO.setId(null);
        when(suitService.addSuit(anyLong(), any(SuitCreateDTO.class))).thenReturn(suitDTO);

        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitDTO)))
            .andExpect(status().isCreated())
            .andExpect(content().string(mapper.writeValueAsString(suitDTO)));

        verify(suitService).addSuit(anyLong(), any(SuitCreateDTO.class));
    }

    @Test
    public void add_SuitWithNullName_StatusBadRequest() throws Exception {
        suitDTO.setId(null);
        suitDTO.setName(null);
        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitDTO)))
            .andExpect(status().isBadRequest());

        verify(suitService, times(0)).addSuit(anyLong(), any(SuitCreateDTO.class));
    }

    @Test
    public void add_SuitWithMoreThanTheRequiredPriority_StatusBadRequest() {
        suitDTO.setId(null);
        suitDTO.setPriority(6);

        verify(suitService, times(0)).addSuit(anyLong(), any(SuitCreateDTO.class));
    }

    @Test
    public void add_SuitWithLessThanTheRequiredPriority_StatusbadRequest() {
        suitDTO.setPriority(-1);

        verify(suitService, times(0)).addSuit(anyLong(), any(SuitCreateDTO.class));
    }

    @Test
    public void addSuit_ThrowRuntimeException_StatusInternalServerError() throws Exception {
        when(suitService.addSuit(anyLong(), any(SuitCreateDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(suitCreateDTO)))
            .andExpect(status().isInternalServerError());

        verify(suitService).addSuit(anyLong(), any(SuitCreateDTO.class));
    }
}