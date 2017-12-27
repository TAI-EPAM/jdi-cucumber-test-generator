package com.epam.test_generator.controllers;


import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.StepService;
import com.epam.test_generator.services.SuitService;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class StepControllerTest {

    private ObjectMapper mapper = new ObjectMapper();


    private MockMvc mockMvc;

    private CaseDTO caseDTO;

    private SuitDTO suitDTO;

    private StepDTO stepDTO;

    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 2L;
    private static final long SIMPLE_STEP_ID = 3L;

    private List<StepDTO> stepDTOS;
    private List<CaseDTO> caseDTOS;

    @InjectMocks
    StepController stepController;

    @Mock
    StepService stepService;

    @Mock
    CaseService caseService;

    @Mock
    SuitService suitService;


    @Before
    public void setUp() throws ParseException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(stepController)
                .setControllerAdvice(new GlobalExceptionController())
                .build();
        stepDTO = new StepDTO();
        stepDTO.setId(SIMPLE_STEP_ID);
        stepDTO.setDescription("description of step");
        stepDTO.setRowNumber(1);
        stepDTO.setType(StepType.THEN);

        stepDTOS= new ArrayList<>();
        stepDTOS.add(stepDTO);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        caseDTO = new CaseDTO();
        caseDTO.setId(SIMPLE_CASE_ID);
        caseDTO.setCreationDate(formatter.parse("12.05.2017"));
        caseDTO.setDescription("description of case");
        caseDTO.setSteps(stepDTOS);
        caseDTO.setPriority(2);
        caseDTO.setTags(null);
        caseDTO.setUpdateDate(formatter.parse("13.06.2017"));

        caseDTOS = new ArrayList<>();
        caseDTOS.add(caseDTO);

        suitDTO = new SuitDTO();

        suitDTO.setId(SIMPLE_SUIT_ID);
        suitDTO.setCases(caseDTOS);
        suitDTO.setCreationDate(formatter.parse("11.11.2011"));
        suitDTO.setDescription("description of suit");
        suitDTO.setName("the name of suit");
        suitDTO.setPriority(1);
        suitDTO.setTags("tags of suit");
    }

    @Test
     public void testGetStepsByCaseId_return200whenGetSteps() throws Exception{
        when(stepService.getStepsByCaseId(anyLong(), anyLong())).thenReturn(stepDTOS);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(stepService).getStepsByCaseId(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetStepsByCaseId_return404whenSuitNotExistOrCaseNotExist() throws Exception{
        when(stepService.getStepsByCaseId(anyLong(), anyLong())).thenThrow(new NotFoundException());

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(stepService).getStepsByCaseId(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetStepsByCaseId_return400whenSuitNotContainsCase() throws Exception{
        when(stepService.getStepsByCaseId(anyLong(), anyLong())).thenThrow(new BadRequestException());

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(stepService).getStepsByCaseId(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetStepsByCaseId_return500whenRuntimeException() throws Exception{
        when(stepService.getStepsByCaseId(anyLong(), anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps"))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(stepService).getStepsByCaseId(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetStepsById_return200whenGetSteps() throws Exception{
        when(stepService.getStep(anyLong(), anyLong(), anyLong())).thenReturn(stepDTO);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
            .andDo(print())
            .andExpect(status().isOk());

        verify(stepService).getStep(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testGetStepsById_return404whenSuitNotExistOrCaseNotExistOrStepNotExist() throws Exception{
        when(stepService.getStep(anyLong(), anyLong(), anyLong())).thenThrow(new NotFoundException());

        mockMvc.perform(get("/suits/" +SIMPLE_SUIT_ID+ "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(stepService).getStep(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testGetStepsById_return400whenSuitNotContainsCaseOrCaseNotContainsStep() throws Exception{
        when(stepService.getStep(anyLong(), anyLong(), anyLong())).thenThrow(new BadRequestException());

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(stepService).getStep(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testGetStepsById_return500whenRuntimeException() throws Exception{
        when(stepService.getStep(anyLong(), anyLong(), anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(stepService).getStep(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testAddStepToCase_return201whenAddStepToCase() throws Exception {
        when(stepService.addStepToCase(anyLong(), anyLong(),any(StepDTO.class))).thenReturn(SIMPLE_STEP_ID);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(SIMPLE_STEP_ID)));

        verify(stepService).addStepToCase(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), any(StepDTO.class));

    }

    @Test
    public void testAddStepToCase_return404whenSuitNotExistOrCaseNotExist() throws Exception {
        when(stepService.addStepToCase(anyLong(), anyLong(),any(StepDTO.class))).thenThrow(new NotFoundException());

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(stepService).addStepToCase(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), any(StepDTO.class));
    }

    @Test
    public void testAddStepToCase_return400whenSuitNotContainsCase() throws Exception {
        when(stepService.addStepToCase(anyLong(), anyLong(),any(StepDTO.class))).thenThrow(new BadRequestException());

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(stepService).addStepToCase(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), any(StepDTO.class));
    }

    @Test
    public void testAddStepToCase_return500whenRuntimeException() throws Exception {
        when(stepService.addStepToCase(anyLong(), anyLong(),any(StepDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(stepService).addStepToCase(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), any(StepDTO.class));
    }

    @Test
    public void testUpdateStep_return200whenUpdateStep() throws Exception {
        mockMvc.perform(put(  "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID          )
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(stepService).updateStep(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID), any(StepDTO.class));
    }

    @Test
    public void testUpdateStep_return404whenSuitNotExistOrCaseNotExistOrStepNotExist() throws Exception {
        doThrow(NotFoundException.class).when(stepService).updateStep(anyLong(), anyLong(), anyLong(), any(StepDTO.class));

        mockMvc.perform(put(  "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID          )
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(stepService).updateStep(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID), any(StepDTO.class));
    }

    @Test
    public void testUpdateStep_return400whenSuitNotContainsCaseOrCaseNotContainsStep() throws Exception {
        doThrow(BadRequestException.class).when(stepService).updateStep(anyLong(), anyLong(), anyLong(), any(StepDTO.class));

        mockMvc.perform(put(  "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID          )
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(stepService).updateStep(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID), any(StepDTO.class));
    }

    @Test
    public void testUpdateStep_return500whenRuntimeException() throws Exception {
        doThrow(RuntimeException.class).when(stepService).updateStep(anyLong(), anyLong(), anyLong(), any(StepDTO.class));

        mockMvc.perform(put(  "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID          )
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(stepService).updateStep(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID), any(StepDTO.class));
    }

    @Test
    public void testRemoveCase_return200whenRemoveCase() throws Exception {
        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status().isOk());

        verify(stepService).removeStep(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testRemoveCase_return404whenSuitNotExistOrCaseNotExistOrStepNotExist() throws Exception {
        doThrow(NotFoundException.class).when(stepService).removeStep(anyLong(), anyLong(), anyLong());

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(stepService).removeStep(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testRemoveCase_return400whenSuitNotContainsCase() throws Exception {
        doThrow(BadRequestException.class).when(stepService).removeStep(anyLong(), anyLong(), anyLong());

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(stepService).removeStep(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testRemoveCase_return500whenRuntimeException() throws Exception{
        doThrow(RuntimeException.class).when(stepService).removeStep(anyLong(), anyLong(), anyLong());

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(stepService).removeStep(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }


    @Test
    public void updateSteps_return200whenUpdateSteps() throws Exception {
        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTOS)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(stepService).cascadeUpdateSteps(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID),anyList());
        verify(stepService, never()).addStepToCase(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID),any(StepDTO.class));
        verify(stepService, never()).removeStep(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
        verify(stepService, never()).updateStep(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID),eq(SIMPLE_STEP_ID) ,any(StepDTO.class));
    }

    @Test
    public void updateSteps_return404whenSuitNotExistOrCaseNotExistOrStepNotExist() throws Exception {
        Mockito.doCallRealMethod().when(stepService).cascadeUpdateSteps(anyLong(),anyInt(),anyList());
        doThrow(NotFoundException.class).when(stepService).removeStep(anyLong(), anyLong(), anyLong());

        String request = mapper.writeValueAsString(stepDTOS);
        // By default the action field is hidden in JSON, must added to request manually
        request = request.replace("}]", ",\"action\":\"DELETE\"}]");

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(stepService).cascadeUpdateSteps(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID),eq(stepDTOS));
    }
}
