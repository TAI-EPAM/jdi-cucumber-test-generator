package com.epam.test_generator.controllers.featurefile;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.entities.request.FeatureFileDTO;
import com.epam.test_generator.dto.wrapper.ListWrapper;
import com.epam.test_generator.services.IOService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.io.IOException;
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
public class FeatureFileControllerTest {

    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;

    private static final long SIMPLE_PROJECT_ID = 1L;
    private FeatureFileDTO featureFileDTO;
    private ListWrapper<FeatureFileDTO> listWrapper;

    @Mock
    private IOService ioService;

    @InjectMocks
    private FeatureFileController featureFileController;


    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(featureFileController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();
        featureFileDTO = new FeatureFileDTO();
        featureFileDTO.setSuitId(1L);
        featureFileDTO.setCaseIds(Lists.newArrayList(1L));
        listWrapper = new ListWrapper<>(Collections.singletonList(featureFileDTO));
    }

    @Test
    public void downloadFile_CorrectDTO_StatusOk() throws Exception {
        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/feature-file")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(listWrapper)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE));

        verify(ioService, times(1)).generateZipFile(eq(SIMPLE_PROJECT_ID), anyList());
    }

    @Test
    public void downloadFile_IncorrectDTO_StatusBadRequest() throws Exception {
        featureFileDTO.setSuitId(null);

        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/feature-file")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(listWrapper)))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        verify(ioService, never()).generateZipFile(eq(SIMPLE_PROJECT_ID), anyList());
    }

    @Test
    public void downloadFile_CorrectDTO_StatusInternalServerError() throws Exception {
        when(ioService.generateZipFile(anyLong(), anyList())).thenThrow(IOException.class);
        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/feature-file")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(listWrapper)))
            .andExpect(status().isInternalServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        verify(ioService, times(1)).generateZipFile(eq(SIMPLE_PROJECT_ID), anyList());
    }
}