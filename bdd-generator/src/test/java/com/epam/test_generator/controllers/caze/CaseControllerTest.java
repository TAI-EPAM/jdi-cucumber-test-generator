package com.epam.test_generator.controllers.caze;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.controllers.GlobalExceptionController;
import com.epam.test_generator.controllers.caze.request.CaseCreateDTO;
import com.epam.test_generator.controllers.caze.request.CaseEditDTO;
import com.epam.test_generator.controllers.caze.request.CaseUpdateDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.entities.Action;
import com.epam.test_generator.entities.Event;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.SuitService;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
public class CaseControllerTest {

    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private CaseDTO caseDTO;
    private SuitDTO suitDTO;
    private List<CaseDTO> caseDTOList;

    private List<CaseEditDTO> updateCaseDTOList;
    private CaseUpdateDTO caseUpdateDTO;

    private static final long SIMPLE_PROJECT_ID = 0L;
    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 2L;
    private static final Long[] CASE_IDS = {3L, 4L, 5L};

    @Mock
    private CaseService casesService;

    @Mock
    private SuitService suitService;

    @InjectMocks
    private CaseController caseController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(caseController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();
        caseDTO = new CaseDTO();
        caseDTO.setId(SIMPLE_CASE_ID);
        caseDTO.setName("name1");
        caseDTO.setDescription("case1");
        caseDTO.setPriority(1);
        caseDTO.setSteps(new ArrayList<>());
        caseDTO.setDisplayedStatusName(Status.NOT_DONE.getStatusName());

        CaseDTO caseDTO1 = new CaseDTO();
        caseDTO1.setId(CASE_IDS[0]);
        caseDTO1.setDescription("case2");
        caseDTO1.setPriority(2);
        caseDTO1.setSteps(new ArrayList<>());
        caseDTO1.setDisplayedStatusName(Status.NOT_RUN.getStatusName());

        CaseDTO caseDTO2 = new CaseDTO();
        caseDTO2.setId(CASE_IDS[1]);
        caseDTO2.setDescription("case3");
        caseDTO2.setPriority(2);
        caseDTO2.setSteps(new ArrayList<>());
        caseDTO2.setDisplayedStatusName(Status.SKIPPED.getStatusName());

        CaseDTO caseDTO3 = new CaseDTO();
        caseDTO3.setId(CASE_IDS[2]);
        caseDTO3.setDescription("case3");
        caseDTO3.setPriority(2);
        caseDTO3.setSteps(new ArrayList<>());
        caseDTO3.setDisplayedStatusName(Status.PASSED.getStatusName());

        suitDTO = new SuitDTO();
        suitDTO.setId(SIMPLE_SUIT_ID);
        suitDTO.setName("Suit name");
        suitDTO.setPriority(1);
        suitDTO.setDescription("Suit description");

        caseDTOList = new ArrayList<>();
        caseDTOList.add(caseDTO);
        caseDTOList.add(caseDTO1);
        caseDTOList.add(caseDTO2);
        caseDTOList.add(caseDTO3);

        suitDTO.setCases(caseDTOList);

        updateCaseDTOList = new ArrayList<>();

        CaseEditDTO updateCaseDTO1 = new CaseEditDTO(1l,"descr", "name", 1,
            Status.NOT_RUN, Collections.emptyList(), Action.CREATE, "comment");
        updateCaseDTO1.setId(CASE_IDS[0]);
        CaseEditDTO updateCaseDTO2 = new CaseEditDTO(1l,"descr", "name", 1,
            Status.NOT_RUN, Collections.emptyList(), Action.UPDATE, "comment");
        updateCaseDTO2.setId(CASE_IDS[1]);
        CaseEditDTO updateCaseDTO3 = new CaseEditDTO(1l,"descr", "name", 1,
            Status.NOT_RUN, Collections.emptyList(), Action.UPDATE, "comment");
        updateCaseDTO3.setId(CASE_IDS[2]);

        updateCaseDTOList.add(updateCaseDTO1);
        updateCaseDTOList.add(updateCaseDTO2);
        updateCaseDTOList.add(updateCaseDTO3);

        caseUpdateDTO = new CaseUpdateDTO("name", "descr", 1,
                Status.NOT_RUN, "comment");

        when(suitService.getSuitDTO(anyLong(), anyLong())).thenReturn(suitDTO);
    }

    @Test
    public void getCase_CaseDTO_StatusOk() throws Exception {
        when(casesService.getCaseDTO(anyLong(), anyLong(), anyLong())).thenReturn(caseDTO);

        mockMvc.perform(
            get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id", is((int)SIMPLE_CASE_ID)))
            .andExpect(jsonPath("$.name", is(caseDTO.getName())))
            .andExpect(jsonPath("$.displayedStatusName", is(caseDTO.getDisplayedStatusName())));

        verify(casesService,  times(1))
            .getCaseDTO(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
        verifyNoMoreInteractions(casesService);
    }

    @Test
    public void getCase_CaseDTO_NotFound() throws Exception {
        when(casesService.getCaseDTO(anyLong(), anyLong(), anyLong()))
            .thenThrow(new NotFoundException());

        mockMvc.perform(
            get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID))
            .andExpect(status().isNotFound());

        verify(casesService)
            .getCaseDTO(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void getCase_CaseDTO_StatusBadRequest() throws Exception {
        suitDTO.setCases(null);
        when(casesService.getCaseDTO(anyLong(), anyLong(), anyLong()))
            .thenThrow(new BadRequestException());

        mockMvc.perform(
            get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID))
            .andExpect(status().isBadRequest());

        verify(casesService)
            .getCaseDTO(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void getCase_CaseDTO_StatusInternalServerError() throws Exception {
        when(casesService.getCaseDTO(anyLong(), anyLong(), anyLong()))
            .thenThrow(new RuntimeException());

        mockMvc.perform(
            get("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID))
            .andExpect(status().isInternalServerError());

        verify(casesService)
            .getCaseDTO(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void addCase_CaseDTO_Created() throws Exception {
        when(casesService.addCaseToSuit(anyLong(), anyLong(), any(CaseCreateDTO.class)))
            .thenReturn(caseDTO);

        mockMvc
            .perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id", is((int)SIMPLE_CASE_ID)))
            .andExpect(jsonPath("$.name", is(caseDTO.getName())))
            .andExpect(jsonPath("$.displayedStatusName", is(caseDTO.getDisplayedStatusName())));

        verify(casesService, times(1))
            .addCaseToSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), any(CaseCreateDTO.class));
        verifyNoMoreInteractions(casesService);
    }

    @Test
    public void addCase_SuitAndCaseDTO_NotFound() throws Exception {
        caseDTO.setId(null);
        when(casesService.addCaseToSuit(anyLong(), anyLong(), any(CaseCreateDTO.class)))
            .thenThrow(new NotFoundException());

        mockMvc
            .perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
            .andExpect(status().isNotFound());

        verify(casesService)
            .addCaseToSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), any(CaseCreateDTO.class));
    }

    @Test
    public void addCase_NullCaseDTO_StatusBadRequest() throws Exception {
        caseDTO.setId(null);
        caseDTO.setDescription(null);

        mockMvc
            .perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
            .andExpect(status().isBadRequest());
        verify(casesService, times(0))
            .addCaseToSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), any(CaseCreateDTO.class));
    }

    @Test
    public void addCase_EmptyDescription_StatusBadRequest() throws Exception {
        caseDTO.setId(null);
        caseDTO.setDescription("");

        mockMvc
            .perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
            .andExpect(status().isBadRequest());

        verify(casesService, times(0))
            .addCaseToSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), any(CaseCreateDTO.class));
    }

    @Test
    public void addCase_NullPriority_StatusBadRequest() throws Exception {
        caseDTO.setId(null);
        caseDTO.setPriority(null);

        mockMvc
            .perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
            .andExpect(status().isBadRequest());

        verify(casesService, times(0))
            .addCaseToSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), any(CaseCreateDTO.class));
    }

    @Test
    public void addCase_ZeroPriority_StatusBadRequest() throws Exception {
        caseDTO.setId(null);
        caseDTO.setPriority(0);

        mockMvc
            .perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
            .andExpect(status().isBadRequest());

        verify(casesService, times(0))
            .addCaseToSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), any(CaseCreateDTO.class));
    }

    @Test
    public void addCase_CaseWithMoreThanTheRequiredPriority_StatusBadRequest() throws Exception {
        caseDTO.setId(null);
        caseDTO.setPriority(6);

        mockMvc
            .perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
            .andExpect(status().isBadRequest());

        verify(casesService, times(0))
            .addCaseToSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), any(CaseCreateDTO.class));
    }

    @Test
    public void addCase_CaseWithLessThanTheRequiredPriority_StatusBadRequest() throws Exception {
        caseDTO.setId(null);
        caseDTO.setPriority(-4);

        mockMvc
            .perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
            .andExpect(status().isBadRequest());

        verify(casesService, times(0))
            .addCaseToSuit(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), any(CaseCreateDTO.class));
    }

    @Test
    public void addCase_RuntimeException_StatusInternalServerError() throws Exception {
        when(casesService.addCaseToSuit(anyLong(), anyLong(), any(CaseCreateDTO.class)))
            .thenThrow(new RuntimeException());

        mockMvc
            .perform(post("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
            .andExpect(status().isInternalServerError());

        verify(casesService).addCaseToSuit(anyLong(), anyLong(), any(CaseCreateDTO.class));
    }

    @Test
    public void updateCase_UpdateCase_StatusOk() throws Exception {
        CaseDTO expectedDto = caseDTO;
        when(casesService.updateCase(anyLong(), anyLong(), anyLong(), any(CaseUpdateDTO.class))).thenReturn(expectedDto);

        mockMvc.perform(
            put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseUpdateDTO)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id", is((int)SIMPLE_CASE_ID)))
            .andExpect(jsonPath("$.name", is(caseDTO.getName())))
            .andExpect(jsonPath("$.displayedStatusName", is(caseDTO.getDisplayedStatusName())));

        verify(casesService, times(1))
            .updateCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID),
                any(CaseUpdateDTO.class));
        verifyNoMoreInteractions(casesService);
    }

    @Test
    public void updateCase_SuitOrCaseNotExist_NotFound() throws Exception {
        doThrow(NotFoundException.class).when(casesService)
            .updateCase(anyLong(), anyLong(), anyLong(), any(CaseUpdateDTO.class));

        mockMvc.perform(
            put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateCaseDTOList.get(0))))
            .andExpect(status().isNotFound());

        verify(casesService)
            .updateCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID),
                any(CaseUpdateDTO.class));
    }

    @Test
    public void updateCase_SuitNotContainsCase_StatusBadRequest() throws Exception {
        doThrow(BadRequestException.class).when(casesService)
            .updateCase(anyLong(), anyLong(), anyLong(), any(CaseUpdateDTO.class));

        mockMvc.perform(
            put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateCaseDTOList.get(0))))
            .andExpect(status().isBadRequest());

        verify(casesService)
            .updateCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID),
                any(CaseUpdateDTO.class));
    }

    @Test
    public void updateCase_CaseWithZeroPriority_StatusBadRequest() throws Exception {
        caseUpdateDTO.setPriority(0);

        mockMvc.perform(
            put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseUpdateDTO)))
            .andExpect(status().isBadRequest());

        verify(casesService, times(0))
            .updateCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID),
                any(CaseUpdateDTO.class));
    }

    @Test
    public void updateCase_CaseWithMoreThanTheRequiredPriority_StatusBadRequest()
        throws Exception {
        caseUpdateDTO.setPriority(6);

        mockMvc.perform(
            put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseUpdateDTO)))
            .andExpect(status().isBadRequest());

        verify(casesService, times(0))
            .updateCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID),
                any(CaseUpdateDTO.class));
    }

    @Test
    public void updateCase_CaseWithLessThanTheRequiredPriority_StatusBadRequest()
        throws Exception {
        caseUpdateDTO.setPriority(-4);

        mockMvc.perform(
            put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseUpdateDTO)))
            .andExpect(status().isBadRequest());

        verify(casesService, times(0))
            .updateCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID),
                any(CaseUpdateDTO.class));
    }

    @Test
    public void updateCase_CaseWithEmptyDescription_StatusBadRequest() throws Exception {
        caseUpdateDTO.setDescription("");

        mockMvc.perform(
            put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseUpdateDTO)))
            .andExpect(status().isBadRequest());

        verify(casesService, times(0))
            .updateCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID),
                any(CaseUpdateDTO.class));
    }

    @Test
    public void updateCase_RuntimeException_StatusInternalServerError() throws Exception {
        doThrow(RuntimeException.class).when(casesService)
            .updateCase(anyLong(), anyLong(), anyLong(), any(CaseUpdateDTO.class));

        mockMvc.perform(
            put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateCaseDTOList.get(0))))
            .andExpect(status().isInternalServerError());

        verify(casesService)
            .updateCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID),
                any(CaseUpdateDTO.class));
    }

    @Test
    public void removeCase_Case_StatusOk() throws Exception {
        when(casesService.removeCase(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_CASE_ID)).thenReturn(caseDTO);
        mockMvc.perform(delete(
            "/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id", is((int)SIMPLE_CASE_ID)))
            .andExpect(jsonPath("$.name", is(caseDTO.getName())))
            .andExpect(jsonPath("$.displayedStatusName", is(caseDTO.getDisplayedStatusName())));

        verify(casesService)
            .removeCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
        verifyNoMoreInteractions(casesService);
    }

    @Test
    public void removeCase_SuitOrCaseNotExist_NotFound() throws Exception {
        doThrow(NotFoundException.class).when(casesService)
            .removeCase(anyLong(), anyLong(), anyLong());

        mockMvc.perform(delete(
            "/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID))
            .andExpect(status().isNotFound());

        verify(casesService)
            .removeCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void removeCase_SuitNotContainsCase_StatusBadRequest() throws Exception {
        suitDTO.setCases(null);
        doThrow(BadRequestException.class).when(casesService)
            .removeCase(anyLong(), anyLong(), anyLong());

        mockMvc.perform(delete(
            "/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID))
            .andExpect(status().isBadRequest());

        verify(casesService)
            .removeCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void removeCase_RuntimeException_StatusInternalServerError() throws Exception {
        doThrow(RuntimeException.class).when(casesService)
            .removeCase(anyLong(), anyLong(), anyLong());

        mockMvc.perform(delete(
            "/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID))
            .andExpect(status().isInternalServerError());

        verify(casesService)
            .removeCase(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void removeCases_Cases_StatusOk() throws Exception {
        when(casesService.removeCases(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, Arrays.asList(CASE_IDS))).thenReturn(caseDTOList);

        mockMvc.perform(
            delete("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(CASE_IDS)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[0].id", is((int)SIMPLE_CASE_ID)))
            .andExpect(jsonPath("$.[0].name", is(caseDTO.getName())))
            .andExpect(jsonPath("$.[0].displayedStatusName", is(caseDTO.getDisplayedStatusName())))
            .andExpect(jsonPath("$.[1].displayedStatusName", is(caseDTOList.get(1).getDisplayedStatusName())))
            .andExpect(jsonPath("$.[1].id", is(CASE_IDS[0].intValue())))
            .andExpect(jsonPath("$.[2].id", is(CASE_IDS[1].intValue())))
            .andExpect(jsonPath("$.[3].id", is(CASE_IDS[2].intValue())));

        verify(casesService)
            .removeCases(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(Arrays.asList(CASE_IDS)));
        verifyNoMoreInteractions(casesService);
    }

    @Test
    public void removeCases_SuitOrCaseNotExist_NotFound() throws Exception {
        doThrow(NotFoundException.class).when(casesService).removeCases(anyLong(), anyLong(),
            anyList());

        mockMvc.perform(
            delete("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(CASE_IDS)))
            .andExpect(status().isNotFound());

        verify(casesService)
            .removeCases(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(Arrays.asList(CASE_IDS)));
    }

    @Test
    public void removeCases_SuitNotContainsCase_StatusBadRequest() throws Exception {
        doThrow(BadRequestException.class).when(casesService)
            .removeCases(anyLong(), anyLong(), anyList());

        mockMvc.perform(
            delete("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(CASE_IDS)))
            .andExpect(status().isBadRequest());

        verify(casesService)
            .removeCases(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(Arrays.asList(CASE_IDS)));
    }

    @Test
    public void removeCases_RuntimeException_StatusInternalServerError() throws Exception {
        doThrow(RuntimeException.class).when(casesService)
            .removeCases(anyLong(), anyLong(), anyList());

        mockMvc.perform(
            delete("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(CASE_IDS)))
            .andExpect(status().isInternalServerError());

        verify(casesService)
            .removeCases(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(Arrays.asList(CASE_IDS)));
    }

    @Test
    public void removeCases_SuitWithoutCases_StatusBadRequest() throws Exception {

        Long[] invalidCaseIds = {6L, 8L};

        mockMvc.perform(
            delete("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidCaseIds)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void removeCases_DuplicatedCaseIds_StatusOk() throws Exception {
        List<CaseDTO> expectedRemovedCasesDTO = caseDTOList;
        List<Integer> expectedIds= Arrays.asList(2, 3, 4, 5);
        Long[] invalidCaseIds = {2L, 3L, 4L, 5L, 2L};

        when(casesService.removeCases(SIMPLE_PROJECT_ID,SIMPLE_SUIT_ID, Arrays.asList(invalidCaseIds)))
            .thenReturn(expectedRemovedCasesDTO);

        mockMvc.perform(
            delete("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidCaseIds)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id", is(expectedIds)));

        verify(casesService).removeCases(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID),
            eq(Arrays.asList(invalidCaseIds)));
        verifyNoMoreInteractions(casesService);
    }

    @Test
    public void performEvent_CreateCase_StatusOk() throws Exception {
        when(casesService.performEvent(anyLong(), anyLong(), anyLong(), any(Event.class))).thenReturn(Status.NOT_DONE);

        mockMvc
            .perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID + "/events/CREATE")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", is(Status.NOT_DONE.toString())))
            .andExpect(status().isOk());

        verify(casesService)
            .performEvent(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID),
                eq(Event.CREATE));
        verifyNoMoreInteractions(casesService);
    }

    @Test
    public void performEvent_WrongEventName_StatusBadRequest() throws Exception {

        mockMvc
            .perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID + "/events/WRONG")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void performEvent_EventCantBePerformed_StatusBadRequest() throws Exception {

        doThrow(BadRequestException.class).when(casesService)
            .performEvent(anyLong(), anyLong(), anyLong(), eq(Event.PASS));

        mockMvc
            .perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases/"
                + SIMPLE_CASE_ID + "/events/PASS")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCases_Cases_StatusOk() throws Exception {
        mockMvc
            .perform(put("/projects/" + SIMPLE_PROJECT_ID + "/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateCaseDTOList)))
            .andExpect(status().isOk());

        verify(casesService)
            .updateCases(eq(SIMPLE_PROJECT_ID), eq(SIMPLE_SUIT_ID), eq(updateCaseDTOList));
    }
}