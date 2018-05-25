package com.epam.test_generator.controllers;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.entities.Status;
import com.epam.test_generator.services.StatesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class StatesControllerTest {

    private Status status = Status.NOT_RUN;
    @Mock
    private StatesService statesService;
    private MockMvc mockMvc;
    @InjectMocks
    private StatesController statesController;


    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(statesController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();
    }

    @Test
    public void get_AvailableEvents_StatusOk() throws Exception {

        mockMvc.perform(get("/events/" + status))
            .andExpect(status().isOk());

        verify(statesService).availableTransitions(eq(status));

    }

    @Test
    public void get_AvailableEvents_StatusBadRequest() throws Exception {

        mockMvc.perform(get("/events/WRONG"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void get_Events_StatusOk() throws Exception {

        mockMvc.perform(get("/events/"))
            .andExpect(status().isOk());
    }

    @Test
    public void get_Statuses_StatusOk() throws Exception {

        mockMvc.perform(get("/events/statuses"))
            .andExpect(status().isOk());
    }
}