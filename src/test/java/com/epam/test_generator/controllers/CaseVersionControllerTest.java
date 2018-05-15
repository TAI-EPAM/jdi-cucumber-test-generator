package com.epam.test_generator.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.controllers.version.caze.CaseVersionController;
import com.epam.test_generator.controllers.version.caze.response.CaseVersionDTO;
import com.epam.test_generator.services.CaseService;
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
public class CaseVersionControllerTest {

    private static final long SIMPLE_PROJECT_ID = 0L;
    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 2L;
    private static final String SIMPLE_COMMIT_ID = "3.0";

    @Mock
    private CaseService caseService;

    @InjectMocks
    private CaseVersionController caseVersionController;

    private MockMvc mockMvc;

    private List<CaseVersionDTO> caseVersionDTOS;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(caseVersionController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();

        caseVersionDTOS = Lists.newArrayList(new CaseVersionDTO(), new CaseVersionDTO());
    }

    @Test
    public void getCaseVersions_CorrectIds_StatusOk() throws Exception {
        when(caseService.getCaseVersions(anyLong(), anyLong(), anyLong()))
            .thenReturn(caseVersionDTOS);

        mockMvc
            .perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID + "/versions"))
            .andExpect(status().isOk());

        verify(caseService).getCaseVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test
    public void getCaseVersions_NotFoundException_StatusNotFound() throws Exception {
        when(caseService.getCaseVersions(anyLong(), anyLong(), anyLong()))
            .thenThrow(new NotFoundException());

        mockMvc
            .perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID + "/versions"))
            .andExpect(status().isNotFound());

        verify(caseService).getCaseVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test
    public void getCaseVersions_BadRequestException_StatusBadRequest() throws Exception {
        when(caseService.getCaseVersions(anyLong(), anyLong(), anyLong()))
            .thenThrow(new BadRequestException());

        mockMvc
            .perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID + "/versions"))
            .andExpect(status().isBadRequest());

        verify(caseService).getCaseVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
    }

    @Test
    public void restoreCase_CorrectIds_StatusOk() throws Exception {

        mockMvc.perform(
            put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID + "/versions/"
                + SIMPLE_COMMIT_ID))
            .andExpect(status().isOk());

        verify(caseService)
            .restoreCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_COMMIT_ID);
    }

    @Test
    public void restoreCase_NotFoundException_StatusNotFound() throws Exception {
        doThrow(new NotFoundException()).when(caseService)
            .restoreCase(anyLong(), anyLong(), anyLong(),
                anyString());

        mockMvc.perform(
            put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID + "/versions/"
                + SIMPLE_COMMIT_ID))
            .andExpect(status().isNotFound());

        verify(caseService)
            .restoreCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_COMMIT_ID);
    }

    @Test
    public void restoreCase_BadRequestException_StatusBadRequest() throws Exception {
        doThrow(new BadRequestException()).when(caseService)
            .restoreCase(anyLong(), anyLong(), anyLong(),
                anyString());

        mockMvc.perform(
            put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID + "/versions/"
                + SIMPLE_COMMIT_ID)
                .param("commitId", SIMPLE_COMMIT_ID))
            .andExpect(status().isBadRequest());

        verify(caseService)
            .restoreCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_COMMIT_ID);
    }
}