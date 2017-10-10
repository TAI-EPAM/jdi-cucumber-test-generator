package com.epam.test_generator.controllers;


import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.StepService;
import com.epam.test_generator.services.SuitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
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

    @InjectMocks
    StepController stepController;

    @Mock
    StepService stepService;

    @Mock
    CaseService caseService;

    @Mock
    SuitService suitService;


    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(stepController)
                .setControllerAdvice(new GlobalExceptionController())
                .build();
        stepDTO = new StepDTO();
        stepDTO.setId(SIMPLE_STEP_ID);
        stepDTO.setDescription("description of step");
        stepDTO.setRowNumber(1);
        stepDTO.setType(2);

        List<StepDTO> stepDTOS = new ArrayList<>();
        stepDTOS.add(stepDTO);

        caseDTO = new CaseDTO();
        caseDTO.setId(SIMPLE_CASE_ID);
        caseDTO.setCreationDate("12.05.2017");
        caseDTO.setDescription("description of case");
        caseDTO.setSteps(stepDTOS);
        caseDTO.setPriority(2);
        caseDTO.setTags(null);
        caseDTO.setUpdateDate("13.06.2017");

        List<CaseDTO> caseDTOS = new ArrayList<>();
        caseDTOS.add(caseDTO);

        suitDTO = new SuitDTO();

        suitDTO.setId(SIMPLE_SUIT_ID);
        suitDTO.setCases(caseDTOS);
        suitDTO.setCreationDate("11.11.2011");
        suitDTO.setDescription("description of suit");
        suitDTO.setName("the name of suit");
        suitDTO.setPriority(1);
        suitDTO.setTags("tags of suit");
    }

    @Test
     public void testGetStepsByCaseId_return200whenGetSteps() throws Exception{
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps"))
                .andDo(print())
                     .andExpect(status()
                             .isOk());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetStepsByCaseId_return404whenSuitNotExist() throws Exception{
        when(suitService.getSuit(anyLong())).thenReturn(null);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps"))
                .andDo(print())
                    .andExpect(status()
                        .isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetStepsByCaseId_return404whenCaseNotExist() throws Exception{
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(null);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps"))
                .andDo(print())
                .andExpect(status()
                        .isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetStepsByCaseId_return400whenSuitNotContainsCase() throws Exception{
        suitDTO.setCases(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps"))
                .andDo(print())
                .andExpect(status()
                        .isBadRequest());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetStepsById_return200whenGetSteps() throws Exception{
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.getStep(anyLong())).thenReturn(stepDTO);

        mockMvc.perform(get("/suits/" +SIMPLE_SUIT_ID+ "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
            .andDo(print())
                 .andExpect(status()
                      .isOk());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService).getStep(eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testGetStepsById_return404whenSuitNotExist() throws Exception{
        when(suitService.getSuit(anyLong())).thenReturn(null);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.getStep(anyLong())).thenReturn(stepDTO);

        mockMvc.perform(get("/suits/" +SIMPLE_SUIT_ID+ "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status()
                        .isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService).getStep(eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testGetStepsById_return404whenCaseNotExist() throws Exception{
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(null);
        when(stepService.getStep(anyLong())).thenReturn(stepDTO);

        mockMvc.perform(get("/suits/" +SIMPLE_SUIT_ID+ "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status()
                        .isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService).getStep(eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testGetStepsById_return404whenStepNotExist() throws Exception{
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.getStep(anyLong())).thenReturn(null);

        mockMvc.perform(get("/suits/" +SIMPLE_SUIT_ID+ "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status()
                        .isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService).getStep(eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testGetStepsById_return400whenSuitNotContainsCase() throws Exception{
        suitDTO.setCases(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.getStep(anyLong())).thenReturn(stepDTO);


        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status()
                        .isBadRequest());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetStepsById_return400whenCaseNotContainsStep() throws Exception{
        caseDTO.setSteps(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.getStep(anyLong())).thenReturn(stepDTO);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                     .andExpect(status()
                             .isBadRequest());

        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService).getStep(eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testAddStepToCase_return201() throws Exception {
        stepDTO.setId(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.addStepToCase(anyLong(),any(StepDTO.class))).thenReturn(SIMPLE_STEP_ID);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(SIMPLE_STEP_ID)));

        verify(suitService).getSuit(SIMPLE_SUIT_ID);
        verify(caseService).getCase(SIMPLE_CASE_ID);
        verify(stepService).addStepToCase(eq(SIMPLE_CASE_ID), any(StepDTO.class));

    }

    @Test
    public void testAddStepToCase_return404whenSuitNotExist() throws Exception {
        stepDTO.setId(null);
        when(suitService.getSuit(anyLong())).thenReturn(null);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.addStepToCase(anyLong(),any(StepDTO.class))).thenReturn(SIMPLE_STEP_ID);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(SIMPLE_SUIT_ID);
        verify(caseService, times(0)).getCase(SIMPLE_CASE_ID);
        verify(stepService, times(0)).addStepToCase(eq(SIMPLE_CASE_ID), any(StepDTO.class));

    }

    @Test
    public void testAddStepToCase_return404whenCaseNotExist() throws Exception {
        stepDTO.setId(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(null);
        when(stepService.addStepToCase(anyLong(),any(StepDTO.class))).thenReturn(SIMPLE_STEP_ID);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(SIMPLE_SUIT_ID);
        verify(caseService).getCase(SIMPLE_CASE_ID);
        verify(stepService, times(0)).addStepToCase(eq(SIMPLE_CASE_ID), any(StepDTO.class));

    }

    @Test
    public void testAddStepToCase_return400whenSuitNotContainsCase() throws Exception {
        stepDTO.setId(null);
        suitDTO.setCases(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.addStepToCase(anyLong(),any(StepDTO.class))).thenReturn(SIMPLE_STEP_ID);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(suitService).getSuit(SIMPLE_SUIT_ID);
        verify(caseService).getCase(SIMPLE_CASE_ID);
        verify(stepService, times(0)).addStepToCase(eq(SIMPLE_CASE_ID), any(StepDTO.class));

    }
    @Test
    public void testUpdateStep_return200whenUpdateStep() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.getStep(anyLong())).thenReturn(stepDTO);

        mockMvc.perform(put(  "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID          )
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService).getStep(eq(SIMPLE_STEP_ID));
        verify(stepService).updateStep(eq(SIMPLE_STEP_ID), any(StepDTO.class));
    }

    @Test
    public void testUpdateStep_return404whenSuitNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(null);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.getStep(anyLong())).thenReturn(stepDTO);

        mockMvc.perform(put(  "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID          )
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService,times(0)).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService,times(0)).getStep(eq(SIMPLE_STEP_ID));
        verify(stepService,times(0)).updateStep(eq(SIMPLE_STEP_ID), any(StepDTO.class));
    }

    @Test
    public void testUpdateStep_return404whenCaseNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(null);
        when(stepService.getStep(anyLong())).thenReturn(stepDTO);

        mockMvc.perform(put(  "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID          )
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService,times(0)).getStep(eq(SIMPLE_STEP_ID));
        verify(stepService,times(0)).updateStep(eq(SIMPLE_STEP_ID), any(StepDTO.class));
    }

    @Test
    public void testUpdateStep_return400whenSuitNotContainsCase() throws Exception {
        suitDTO.setCases(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.getStep(anyLong())).thenReturn(stepDTO);

        mockMvc.perform(put(  "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID          )
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService,times(0)).getStep(eq(SIMPLE_STEP_ID));
        verify(stepService,times(0)).updateStep(eq(SIMPLE_STEP_ID), any(StepDTO.class));
    }

    @Test
    public void testUpdateStep_return404whenStepNotExist() throws Exception {

        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.getStep(anyLong())).thenReturn(null);

        mockMvc.perform(put(  "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID          )
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService).getStep(eq(SIMPLE_STEP_ID));
        verify(stepService,times(0)).updateStep(eq(SIMPLE_STEP_ID), any(StepDTO.class));
    }

    @Test
    public void testUpdateStep_return400whenCaseNotContainsStep() throws Exception {
        caseDTO.setSteps(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.getStep(anyLong())).thenReturn(stepDTO);

        mockMvc.perform(put(  "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID          )
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stepDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService).getStep(eq(SIMPLE_STEP_ID));
        verify(stepService,times(0)).updateStep(eq(SIMPLE_STEP_ID), any(StepDTO.class));
    }

    @Test
    public void testRemoveCase_return200whenRemoveCase() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.getStep(anyLong())).thenReturn(stepDTO);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status().isOk());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService).getStep(eq(SIMPLE_STEP_ID));
        verify(stepService).removeStep(eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testRemoveCase_return404whenSuitNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(null);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.getStep(anyLong())).thenReturn(stepDTO);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService,times(0)).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService,times(0)).getStep(eq(SIMPLE_STEP_ID));
        verify(stepService,times(0)).removeStep(eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testRemoveCase_return404whenCaseNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(null);
        when(stepService.getStep(anyLong())).thenReturn(stepDTO);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService,times(0)).getStep(eq(SIMPLE_STEP_ID));
        verify(stepService,times(0)).removeStep(eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testRemoveCase_return400whenSuitNotContainsCase() throws Exception {
        suitDTO.setCases(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.getStep(anyLong())).thenReturn(stepDTO);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService,times(0)).getStep(eq(SIMPLE_STEP_ID));
        verify(stepService,times(0)).removeStep(eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testRemoveCase_return404whenStepNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.getStep(anyLong())).thenReturn(null);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService).getStep(eq(SIMPLE_STEP_ID));
        verify(stepService,times(0)).removeStep(eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void testRemoveCase_return400whenCaseNotContainsStep() throws Exception {
        caseDTO.setSteps(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(stepService.getStep(anyLong())).thenReturn(stepDTO);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(stepService).getStep(eq(SIMPLE_STEP_ID));
        verify(stepService,times(0)).removeStep(eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

}
