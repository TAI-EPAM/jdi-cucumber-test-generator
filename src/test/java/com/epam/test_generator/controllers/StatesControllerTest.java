package com.epam.test_generator.controllers;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.entities.Status;
import com.epam.test_generator.services.StatesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class StatesControllerTest {

    Status status = Status.NOT_RUN;
    @Mock
    StatesService statesService;
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
    public void getAvailableEvents_status200() throws Exception {

        mockMvc.perform(get("/events/" + status))
            .andDo(print())
            .andExpect(status().isOk());

        verify(statesService).availableTransitions(eq(status));

    }

    @Test
    public void getAvailableEvents_status400() throws Exception {

        mockMvc.perform(get("/events/WRONG"))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void getEvents_status200() throws Exception {

        mockMvc.perform(get("/events/"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void getStatuses_status200() throws Exception {

        mockMvc.perform(get("/statuses/"))
            .andDo(print())
            .andExpect(status().isOk());
    }
}