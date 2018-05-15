package com.epam.test_generator.controllers.step;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.controllers.step.request.StepCreateDTO;
import com.epam.test_generator.controllers.step.request.StepUpdateDTO;
import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.StepService;
import com.epam.test_generator.services.SuitService;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class StepControllerTest {

    private static final long SIMPLE_PROJECT_ID = 0L;
    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 2L;
    private static final long SIMPLE_STEP_ID = 3L;
    @InjectMocks
    private StepController stepController;
    @Mock
    private StepService stepService;
    @Mock
    private CaseService caseService;
    @Mock
    private SuitService suitService;
    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private StepDTO stepDTO;
    private StepCreateDTO stepCreateDTO;
    private StepUpdateDTO stepUpdateDTO;
    private List<StepDTO> stepDTOS;

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
        stepDTO.setDisplayedStatusName(Status.NOT_RUN.getStatusName());
        stepDTOS = new ArrayList<>();
        stepDTOS.add(stepDTO);

        stepCreateDTO = new StepCreateDTO();
        stepCreateDTO.setDescription("description of step");
        stepCreateDTO.setType(StepType.THEN);
        stepCreateDTO.setStatus(Status.NOT_RUN);

        stepUpdateDTO = new StepUpdateDTO();
        stepUpdateDTO.setDescription("Updated description");
        stepUpdateDTO.setComment("new comment");

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setId(SIMPLE_CASE_ID);
        caseDTO.setCreationDate(ZonedDateTime.parse("2017-05-12T00:00Z").toInstant().toEpochMilli());
        caseDTO.setDescription("description of case");
        caseDTO.setSteps(stepDTOS);
        caseDTO.setPriority(2);
        caseDTO.setTags(null);
        caseDTO.setUpdateDate(ZonedDateTime.parse("2017-06-13T00:00Z").toInstant().toEpochMilli());

        List<CaseDTO> caseDTOS = new ArrayList<>();
        Set<TagDTO> tagDTOS = new HashSet<>();
        tagDTOS.add(new TagDTO("tags of suit"));
        caseDTOS.add(caseDTO);

        SuitDTO suitDTO = new SuitDTO();

        suitDTO.setId(SIMPLE_SUIT_ID);
        suitDTO.setCases(caseDTOS);
        suitDTO.setCreationDate(ZonedDateTime.parse("2011-11-11T00:00Z").toInstant().toEpochMilli());
        suitDTO.setDescription("description of suit");
        suitDTO.setName("the name of suit");
        suitDTO.setPriority(1);
        suitDTO.setTags(tagDTOS);
    }

    @Test
    public void getStepsByCaseId_StepDTOs_StatusOk() throws Exception {
        when(stepService.getStepsByCaseId(anyLong(), anyLong(), anyLong())).thenReturn(stepDTOS);

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps"))
            .andExpect(status().isOk());

        verify(stepService).getStepsByCaseId(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void getStepsByCaseId_SuitOrCaseNotExist_StatusNotFound() throws Exception {
        when(stepService.getStepsByCaseId(anyLong(), anyLong(), anyLong())).thenThrow(new NotFoundException());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps"))
            .andExpect(status().isNotFound());

        verify(stepService).getStepsByCaseId(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void getStepsByCaseId_SuitNotContainsCase_StatusBadRequest() throws Exception {
        when(stepService.getStepsByCaseId(anyLong(), anyLong(), anyLong()))
            .thenThrow(new BadRequestException());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps"))
            .andExpect(status().isBadRequest());

        verify(stepService).getStepsByCaseId(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void getStepsByCaseId_RuntimeException_StatusInternalServerError() throws Exception {
        when(stepService.getStepsByCaseId(anyLong(), anyLong(), anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps"))
            .andExpect(status().isInternalServerError());

        verify(stepService).getStepsByCaseId(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void getStepsById_StepDTOs_StatusOk() throws Exception {
        when(stepService.getStep(anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(stepDTO);

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/"
            + SIMPLE_STEP_ID))
            .andExpect(status().isOk());

        verify(stepService).getStep(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void getStepsById_SuitOrCaseOrStepNotExist_StatusNotFound()
        throws Exception {
        when(stepService.getStep(anyLong(), anyLong(), anyLong(), anyLong()))
            .thenThrow(new NotFoundException());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/"
            + SIMPLE_STEP_ID))
            .andExpect(status().isNotFound());

        verify(stepService).getStep(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void getStepsById_SuitNotContainsCaseOrCaseNotContainsStep_StatusBadRequest()
        throws Exception {
        when(stepService.getStep(anyLong(), anyLong(), anyLong(), anyLong()))
            .thenThrow(new BadRequestException());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/"
            + SIMPLE_STEP_ID))
            .andExpect(status().isBadRequest());

        verify(stepService).getStep(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void getStepsById_RuntimeException_StatusInternalServerError() throws Exception {
        when(stepService.getStep(anyLong(), anyLong(), anyLong(), anyLong()))
            .thenThrow(new RuntimeException());

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/"
            + SIMPLE_STEP_ID))
            .andExpect(status().isInternalServerError());

        verify(stepService).getStep(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void addStepToCase_StepDTO_StatusCreated() throws Exception {
        when(stepService.addStepToCase(anyLong(), anyLong(), anyLong(), any(StepCreateDTO.class)))
            .thenReturn(SIMPLE_STEP_ID);

        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepCreateDTO)))
            .andExpect(status().isCreated())
            .andExpect(content().string(String.valueOf(SIMPLE_STEP_ID)));

        verify(stepService)
            .addStepToCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), any(StepCreateDTO.class));

    }

    @Test
    public void addStepToCase_SuitOrCaseNotExist_StatusNotFound() throws Exception {
        when(stepService.addStepToCase(anyLong(), anyLong(), anyLong(), any(StepCreateDTO.class)))
            .thenThrow(new NotFoundException());

        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepCreateDTO)))
            .andExpect(status().isNotFound());

        verify(stepService)
            .addStepToCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), any(StepCreateDTO.class));
    }

    @Test
    public void addStepToCase_SuitNotContainsCase_StatusBadRequest() throws Exception {
        when(stepService.addStepToCase(anyLong(), anyLong(), anyLong(), any(StepCreateDTO.class)))
            .thenThrow(new BadRequestException());

        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepCreateDTO)))
            .andExpect(status().isBadRequest());

        verify(stepService)
            .addStepToCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), any(StepCreateDTO.class));
    }

    @Test
    public void addStepToCase_RuntimeException_StatusInternalServerError() throws Exception {
        when(stepService.addStepToCase(anyLong(), anyLong(), anyLong(), any(StepCreateDTO.class)))
            .thenThrow(new RuntimeException());

        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepCreateDTO)))
            .andExpect(status().isInternalServerError());

        verify(stepService)
            .addStepToCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), any(StepCreateDTO.class));
    }

    @Test
    public void updateStep_StepDTO_StatusOk() throws Exception {
        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/"
            + SIMPLE_STEP_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepUpdateDTO)))
            .andExpect(status().isOk());

        verify(stepService).updateStep(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID),
            any(StepUpdateDTO.class));
    }

    @Test
    public void updateStep_SuitOrCaseOrStepNotExist_StatusNotFound()
        throws Exception {
        doThrow(NotFoundException.class).when(stepService)
            .updateStep(anyLong(), anyLong(), anyLong(), anyLong(), any(StepUpdateDTO.class));

        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/"
            + SIMPLE_STEP_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepUpdateDTO)))
            .andExpect(status().isNotFound());

        verify(stepService).updateStep(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID),
            any(StepUpdateDTO.class));
    }

    @Test
    public void updateStep_SuitNotContainsCaseOrCaseNotContainsStep_StatusBadRequest()
        throws Exception {
        doThrow(BadRequestException.class).when(stepService)
            .updateStep(anyLong(), anyLong(), anyLong(), anyLong(), any(StepUpdateDTO.class));

        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/"
            + SIMPLE_STEP_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepUpdateDTO)))
            .andExpect(status().isBadRequest());

        verify(stepService).updateStep(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID),
            any(StepUpdateDTO.class));
    }

    @Test
    public void updateStep_RuntimeException_StatusInternalServerError() throws Exception {
        doThrow(RuntimeException.class).when(stepService)
            .updateStep(anyLong(), anyLong(), anyLong(), anyLong(), any(StepUpdateDTO.class));

        mockMvc.perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/"
            + SIMPLE_STEP_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(stepUpdateDTO)))
            .andExpect(status().isInternalServerError());

        verify(stepService).updateStep(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID),
            any(StepUpdateDTO.class));
    }

    @Test
    public void removeCase_CaseDTO_StatusOk() throws Exception {
        mockMvc.perform(delete(
            "/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
            .andExpect(status().isOk());

        verify(stepService).removeStep(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void removeCase_SuitOrCaseOrStepNotExist_StatusNotFound()
        throws Exception {
        doThrow(NotFoundException.class).when(stepService)
            .removeStep(anyLong(), anyLong(), anyLong(), anyLong());

        mockMvc.perform(delete(
            "/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
            .andExpect(status().isNotFound());

        verify(stepService).removeStep(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void removeCase_SuitNotContainsCase_BadRequest() throws Exception {
        doThrow(BadRequestException.class).when(stepService)
            .removeStep(anyLong(), anyLong(), anyLong(), anyLong());

        mockMvc.perform(delete(
            "/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
            .andExpect(status().isBadRequest());

        verify(stepService).removeStep(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }

    @Test
    public void removeCase_RuntimeException_StatusInternalServerError() throws Exception {
        doThrow(RuntimeException.class).when(stepService)
            .removeStep(anyLong(), anyLong(), anyLong(), anyLong());

        mockMvc.perform(delete(
            "/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/steps/" + SIMPLE_STEP_ID))
            .andExpect(status().isInternalServerError());

        verify(stepService).removeStep(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_STEP_ID));
    }
}
