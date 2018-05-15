package com.epam.test_generator.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.dto.SuitVersionDTO;
import com.epam.test_generator.services.SuitService;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.google.common.collect.Lists;
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
public class SuitVersionControllerTest {

    private static final long SIMPLE_PROJECT_ID = 0L;
    private static final long SIMPLE_SUIT_ID = 1L;
    private static final String SIMPLE_COMMIT_ID = "3.0";

    @Mock
    private SuitService suitService;

    @InjectMocks
    private SuitVersionController suitVersionController;

    private MockMvc mockMvc;

    private List<SuitVersionDTO> suitVersionDTOs;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(suitVersionController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();

        suitVersionDTOs = Lists.newArrayList(new SuitVersionDTO(), new SuitVersionDTO());
    }

    @Test
    public void getSuitVersions_CorrectIds_StatusOk() throws Exception {
        when(suitService.getSuitVersions(anyLong(), anyLong()))
            .thenReturn(suitVersionDTOs);

        mockMvc
            .perform(
                get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/versions"))
            .andExpect(status().isOk());

        verify(suitService).getSuitVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);
    }

    @Test
    public void getSuitVersions_NotFoundException_StatusNotFound() throws Exception {
        when(suitService.getSuitVersions(anyLong(), anyLong()))
            .thenThrow(new NotFoundException());

        mockMvc
            .perform(
                get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/versions"))
            .andExpect(status().isNotFound());

        verify(suitService).getSuitVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);
    }

    @Test
    public void getSuitVersions_BadRequestException_StatusBadRequest() throws Exception {
        when(suitService.getSuitVersions(anyLong(), anyLong()))
            .thenThrow(new BadRequestException());

        mockMvc
            .perform(
                get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/versions"))
            .andExpect(status().isBadRequest());

        verify(suitService).getSuitVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);
    }

    @Test
    public void restoreSuit_CorrectIds_StatusOk() throws Exception {

        mockMvc.perform(
            put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/versions/"
                + SIMPLE_COMMIT_ID))
            .andExpect(status().isOk());

        verify(suitService)
            .restoreSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_COMMIT_ID);
    }

    @Test
    public void restoreSuit_NotFoundException_StatusNotFound() throws Exception {
        doThrow(new NotFoundException()).when(suitService)
            .restoreSuit(anyLong(), anyLong(), anyString());

        mockMvc.perform(
            put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/versions/"
                + SIMPLE_COMMIT_ID))
            .andExpect(status().isNotFound());

        verify(suitService)
            .restoreSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_COMMIT_ID);
    }

    @Test
    public void restoreSuit_BadRequestException_StatusBadRequest() throws Exception {
        doThrow(new BadRequestException()).when(suitService)
            .restoreSuit(anyLong(), anyLong(), anyString());

        mockMvc.perform(
            put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/versions/"
                + SIMPLE_COMMIT_ID)
                .param("commitId", SIMPLE_COMMIT_ID))
            .andExpect(status().isBadRequest());

        verify(suitService)
            .restoreSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_COMMIT_ID);
    }
}
