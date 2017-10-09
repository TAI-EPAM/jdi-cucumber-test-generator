package com.epam.test_generator.controllers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.SuitService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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

	private static final long SIMPLE_SUIT_ID = 1L;
	private static final long SIMPLE_CASE_ID = 2L;

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
        caseDTO.setDescription("case1");
        caseDTO.setPriority(1);
        caseDTO.setSteps(new ArrayList<>());

        suitDTO = new SuitDTO();
        suitDTO.setId(SIMPLE_SUIT_ID);
        suitDTO.setName("Suit name");
        suitDTO.setPriority(1);
        suitDTO.setDescription("Suit description");

        caseDTOList = new ArrayList<>();
        caseDTOList.add(caseDTO);

        suitDTO.setCases(caseDTOList);
    }

    @Test
    public void testGetCases_return200whenGetCases() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
    }

    @Test
    public void testGetCases_return404whenSuitNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(null);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
    }

    @Test
    public void testGetCase_return200whenGetCase() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(casesService.getCase(anyLong())).thenReturn(caseDTO);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(caseDTO)));

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService).getCase(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetCase_return404whenSuitNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(null);
        when(casesService.getCase(anyLong())).thenReturn(caseDTO);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService, times(0)).getCase(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetCase_return404whenCaseNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(casesService.getCase(anyLong())).thenReturn(null);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService).getCase(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetCase_return400whenSuitNotContainsCase() throws Exception {
        suitDTO.setCases(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(casesService.getCase(anyLong())).thenReturn(caseDTO);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService).getCase(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetCase_return500whenRuntimeException() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(casesService.getCase(anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService).getCase(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testAddCase_return201whenAddNewCase() throws Exception {
        caseDTO.setId(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(casesService.addCaseToSuit(anyLong(), any(CaseDTO.class))).thenReturn(SIMPLE_CASE_ID);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(SIMPLE_CASE_ID)));

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService).addCaseToSuit(eq(SIMPLE_SUIT_ID), any(CaseDTO.class));
    }

    @Test
    public void testAddCase_return404whenSuitNotExist() throws Exception {
        caseDTO.setId(null);
        when(suitService.getSuit(anyLong())).thenReturn(null);
        when(casesService.addCaseToSuit(anyLong(), any(CaseDTO.class))).thenReturn(SIMPLE_CASE_ID);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService, times(0)).addCaseToSuit(eq(SIMPLE_SUIT_ID), any(CaseDTO.class));
    }

    //TODO create validation (description - null)
    @Ignore
    public void testAddCase_return422whenAddCaseWithNullDescription() throws Exception {
        caseDTO.setId(null);
        caseDTO.setDescription(null);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andExpect(status().isUnprocessableEntity());

        verify(casesService, times(0)).addCaseToSuit(eq(SIMPLE_SUIT_ID), any(CaseDTO.class));
    }

    //TODO create validation (description - "")
    @Ignore
    public void testAddCase_return422whenAddCaseWithEmptyDescription() throws Exception {
        caseDTO.setId(null);
        caseDTO.setDescription("");

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andExpect(status().isUnprocessableEntity());

        verify(casesService, times(0)).addCaseToSuit(eq(SIMPLE_SUIT_ID), any(CaseDTO.class));
    }

    //TODO create validation (priority - null)
    @Ignore
    public void testAddCase_return422whenAddCaseWithNullPriority() throws Exception {
        caseDTO.setId(null);
        caseDTO.setPriority(null);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andExpect(status().isUnprocessableEntity());

        verify(casesService, times(0)).addCaseToSuit(eq(SIMPLE_SUIT_ID), any(CaseDTO.class));
    }

    //TODO create validation (priority - 0)
    @Ignore
    public void testAddCase_return422whenAddCaseWithZeroPriority() throws Exception {
        caseDTO.setId(null);
        caseDTO.setPriority(0);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andExpect(status().isUnprocessableEntity());

        verify(casesService, times(0)).addCaseToSuit(eq(SIMPLE_SUIT_ID), any(CaseDTO.class));
    }

    //TODO create validation (priority - 6)
    @Ignore
    public void testAddCase_return422whenAddCaseWithMoreThanTheRequiredPriority() throws Exception {
        caseDTO.setId(null);
        caseDTO.setPriority(6);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andExpect(status().isUnprocessableEntity());

        verify(casesService, times(0)).addCaseToSuit(eq(SIMPLE_SUIT_ID), any(CaseDTO.class));
    }

    //TODO create validation (priority - negative)
    @Ignore
    public void testAddCase_return422whenAddCaseWithLessThanTheRequiredPriority() throws Exception {
        caseDTO.setId(null);
        caseDTO.setPriority(-4);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andExpect(status().isUnprocessableEntity());

        verify(casesService, times(0)).addCaseToSuit(eq(SIMPLE_SUIT_ID), any(CaseDTO.class));
    }

    @Test
    public void testAddCase_return500whenRuntimeException() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(casesService.addCaseToSuit(anyLong(), any(CaseDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(suitService).getSuit(anyLong());
        verify(casesService).addCaseToSuit(anyLong(), any(CaseDTO.class));
    }

    @Test
    public void testRemoveCase_return200whenRemoveCase() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(casesService.getCase(anyLong())).thenReturn(caseDTO);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID))
                .andDo(print())
                .andExpect(status().isOk());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService).getCase(eq(SIMPLE_CASE_ID));
        verify(casesService).removeCase(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testRemoveCase_return404whenSuitNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(null);
        when(casesService.getCase(anyLong())).thenReturn(caseDTO);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService, times(0)).getCase(eq(SIMPLE_CASE_ID));
        verify(casesService, times(0)).removeCase(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testRemoveCase_return404whenCaseNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(casesService.getCase(anyLong())).thenReturn(null);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService).getCase(eq(SIMPLE_CASE_ID));
        verify(casesService, times(0)).removeCase(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testRemoveCase_return400whenSuitNotContainsCase() throws Exception {
        suitDTO.setCases(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(casesService.getCase(anyLong())).thenReturn(caseDTO);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService).getCase(eq(SIMPLE_CASE_ID));
        verify(casesService, times(0)).removeCase(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testRemoveCase_return500whenRuntimeException() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(casesService.getCase(anyLong())).thenReturn(caseDTO);
        doThrow(RuntimeException.class).when(casesService).removeCase(anyLong(), anyLong());

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService).getCase(eq(SIMPLE_CASE_ID));
        verify(casesService).removeCase(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testUpdateCase_return200whenUpdateCase() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(casesService.getCase(anyLong())).thenReturn(caseDTO);

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService).getCase(eq(SIMPLE_CASE_ID));
        verify(casesService).updateCase(eq(SIMPLE_CASE_ID), any(CaseDTO.class));
    }

    @Test
    public void testUpdateCase_return404whenSuitNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(null);
        when(casesService.getCase(anyLong())).thenReturn(caseDTO);

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService,times(0)).getCase(eq(SIMPLE_CASE_ID));
        verify(casesService, times(0)).updateCase(eq(SIMPLE_CASE_ID), any(CaseDTO.class));
    }

    @Test
    public void testUpdateCase_return201whenCaseNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(casesService.getCase(anyLong())).thenReturn(null);

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService).getCase(eq(SIMPLE_CASE_ID));
        verify(casesService, times(0)).updateCase(eq(SIMPLE_CASE_ID), any(CaseDTO.class));
    }

    @Test
    public void testUpdateCase_return201whenSuitNotContainsCase() throws Exception {
        suitDTO.setCases(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(casesService.getCase(anyLong())).thenReturn(caseDTO);

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService).getCase(eq(SIMPLE_CASE_ID));
        verify(casesService, times(0)).updateCase(eq(SIMPLE_CASE_ID), any(CaseDTO.class));
    }

    //TODO create validation (priority - null)
    @Ignore
    public void testUpdateCase_return422whenUpdateCaseWithNullPriority() throws Exception {
        caseDTO.setPriority(null);

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andExpect(status().isUnprocessableEntity());

        verify(casesService, times(0)).updateCase(eq(SIMPLE_CASE_ID), any(CaseDTO.class));
    }

    //TODO create validation (priority - 0)
    @Ignore
    public void testUpdateCase_return422whenUpdateCaseWithZeroPriority() throws Exception {
        caseDTO.setPriority(0);

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andExpect(status().isUnprocessableEntity());

        verify(casesService, times(0)).updateCase(eq(SIMPLE_CASE_ID), any(CaseDTO.class));
    }

    //TODO create validation (priority - 6)
    @Ignore
    public void testUpdateCase_return422whenUpdateCaseWithMoreThanTheRequiredPriority() throws Exception {
        caseDTO.setPriority(6);

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andExpect(status().isUnprocessableEntity());

        verify(casesService, times(0)).updateCase(eq(SIMPLE_CASE_ID), any(CaseDTO.class));
    }

    //TODO create validation (priority - negative)
    @Ignore
    public void testUpdateCase_return422whenUpdateCaseWithLessThanTheRequiredPriority() throws Exception {
        caseDTO.setPriority(-4);

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andExpect(status().isUnprocessableEntity());


        verify(casesService, times(0)).updateCase(eq(SIMPLE_CASE_ID), any(CaseDTO.class));
    }

    //TODO create validation (description - null)
    @Ignore
    public void testUpdateCase_return422whenUpdateCaseWithNullDescription() throws Exception {
        caseDTO.setDescription(null);

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andExpect(status().isUnprocessableEntity());

        verify(casesService, times(0)).updateCase(eq(SIMPLE_CASE_ID), any(CaseDTO.class));
    }

    //TODO create validation (description - "")
    @Ignore
    public void testUpdateCase_return422whenUpdateCaseWithEmptyDescription() throws Exception {
        caseDTO.setDescription("");

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andExpect(status().isUnprocessableEntity());

        verify(casesService, times(0)).updateCase(eq(SIMPLE_CASE_ID), any(CaseDTO.class));
    }

    @Test
    public void testUpdateCase_return500whenRuntimeException() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(casesService.getCase(anyLong())).thenReturn(caseDTO);
        doThrow(RuntimeException.class).when(casesService).updateCase(anyLong(), any(CaseDTO.class));

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(caseDTO)))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(casesService).getCase(eq(SIMPLE_CASE_ID));
        verify(casesService).updateCase(eq(SIMPLE_CASE_ID), any(CaseDTO.class));
    }
}