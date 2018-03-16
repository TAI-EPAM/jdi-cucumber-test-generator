package com.epam.test_generator.controllers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.dto.RawSuitResultDTO;
import com.epam.test_generator.dto.TestResultDTO;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.services.TestResultService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class TestResultControllerTest {

    private static final long SIMPLE_PROJECT_ID = 0L;
    private static final Integer FROM = 2;
    private static final Integer TO = 3;
    private static final Integer NEGATIVE = -5;
    private static final SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
    private Date FROM_DATE;
    private Date TO_DATE;

    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private TestResultDTO testResultDTO;
    private List<TestResultDTO> testResultDTOS;
    private List<RawSuitResultDTO> rawSuitResultDTOS;

    @Mock
    private TestResultService testResultService;

    @InjectMocks
    private TestResultController testResultController;

    @Before
    public void setup() throws ParseException {
        mockMvc = MockMvcBuilders.standaloneSetup(testResultController)
            .setControllerAdvice(new GlobalExceptionController()).build();

        testResultDTOS = new ArrayList<>();
        FROM_DATE = format.parse("26/02/2018");
        TO_DATE = format.parse("27/02/2018");

        testResultDTOS = Stream.generate(this::generateSimpleTestResultDTO).limit(4)
            .collect(Collectors.toList());

        testResultDTO = crateTestResultDTOWithDate(FROM_DATE);
        testResultDTOS.add(testResultDTO);

        testResultDTO = crateTestResultDTOWithDate(TO_DATE);
        testResultDTOS.add(testResultDTO);

        rawSuitResultDTOS = Collections.emptyList();
    }

    @Test
    public void runTests_StatusOk() throws Exception {
        mockMvc.perform(post("/projects/" + SIMPLE_PROJECT_ID + "/tests/run")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(rawSuitResultDTOS)))
            .andExpect(status().isOk());

        verify(testResultService).saveResult(eq(SIMPLE_PROJECT_ID), eq(rawSuitResultDTOS), any(
            Authentication.class));
    }

    @Test
    public void getTestRunResults_StatusOk() throws Exception {
        when(testResultService.getTestResults(SIMPLE_PROJECT_ID)).thenReturn(testResultDTOS);

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/tests/results/"))
            .andExpect(status().isOk());

        verify(testResultService).getTestResults(eq(SIMPLE_PROJECT_ID));
    }


    @Test
    public void getLimitedTestRunResults_StatusOk() throws Exception {
        when(testResultService.getTestResults(SIMPLE_PROJECT_ID, 0, TO)).thenReturn(testResultDTOS);

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/tests/results/")
            .param("limit", String.valueOf(TO)))
            .andExpect(status().isOk());

        verify(testResultService).getTestResults(eq(SIMPLE_PROJECT_ID), eq(0), eq(TO));
    }

    @Test
    public void getLimitTestRunResults_NegativeLimit_BadRequest() throws Exception {

        mockMvc.perform(get("/projects/" + SIMPLE_PROJECT_ID + "/tests/results/")
            .param("limit", String.valueOf(NEGATIVE)))
            .andExpect(status().isBadRequest());

        verify(testResultService, times(0)).
            getTestResults(eq(SIMPLE_PROJECT_ID), eq(0), eq(NEGATIVE));
    }

    @Test
    public void getTestRunResultsFromTo_StatusOk() throws Exception {
        when(testResultService.getTestResults(SIMPLE_PROJECT_ID, FROM, TO))
            .thenReturn(testResultDTOS.subList(FROM, TO));

        mockMvc.perform(
            get("/projects/" + SIMPLE_PROJECT_ID + "/tests/results/")
                .param("offset", String.valueOf(FROM))
                .param("limit", String.valueOf(TO)))
            .andExpect(status().isOk());

        verify(testResultService).getTestResults(eq(SIMPLE_PROJECT_ID), eq(FROM), eq(TO));
    }

    @Test
    public void getTestRunResultsFromTo_NegativeRange_BadRequest() throws Exception {

        mockMvc.perform(
            get("/projects/" + SIMPLE_PROJECT_ID + "/tests/results/")
                .param("offset", String.valueOf(NEGATIVE))
                .param("limit", String.valueOf(TO)))
            .andExpect(status().isBadRequest());

        verify(testResultService, times(0)).
            getTestResults(eq(SIMPLE_PROJECT_ID), eq(NEGATIVE), eq(TO));
    }

    @Test
    public void getTestRunResultsFromTo_onlyOffsetIsPresent_BadRequest() throws Exception {

        mockMvc.perform(
            get("/projects/" + SIMPLE_PROJECT_ID + "/tests/results/")
                .param("offset", String.valueOf(FROM)))
            .andExpect(status().isBadRequest());

        verify(testResultService, times(0)).
            getTestResults(eq(SIMPLE_PROJECT_ID), eq(TO), eq(FROM));
    }

    @Test
    public void getTestRunResultsFromToByDate_StatusOk() throws Exception {
        when(testResultService.getTestResults(SIMPLE_PROJECT_ID, FROM_DATE, TO_DATE))
            .thenReturn(testResultDTOS.subList(1, 2));

        mockMvc.perform(
            get("/projects/" + SIMPLE_PROJECT_ID + "/tests/results/dates")
                .param("from", "2018-02-26")
                .param("to", "2018-02-27"))
            .andExpect(status().isOk());

        verify(testResultService)
            .getTestResults(eq(SIMPLE_PROJECT_ID), any(Date.class), any(Date.class));
    }

    @Test
    public void getTestRunResultsFromToByDate_ToBeforeFrom_BadRequest() throws Exception {

        mockMvc.perform(
            get("/projects/" + SIMPLE_PROJECT_ID + "/tests/results/dates")
                .param("from", String.valueOf(TO_DATE))
                .param("to", String.valueOf(FROM_DATE)))
            .andExpect(status().isBadRequest());

        verify(testResultService, times(0))
            .getTestResults(eq(SIMPLE_PROJECT_ID), eq(TO_DATE), eq(FROM_DATE));
    }

    private TestResultDTO generateSimpleTestResultDTO() {
        return crateTestResultDTOWithDate(new Date(1519618194914L));
    }

    private TestResultDTO crateTestResultDTOWithDate(Date date) {
        final TestResultDTO testResultDTO = new TestResultDTO();
        testResultDTO.setAmountOfFailed(0);
        testResultDTO.setAmountOfPassed(1);
        testResultDTO.setAmountOfSkipped(0);
        testResultDTO.setDate(date);
        testResultDTO.setDuration(0);
        testResultDTO.setExecutedBy("User Userovich");
        testResultDTO.setStatus(Status.PASSED);
        testResultDTO.setSuits(Collections.emptyList());
        testResultDTOS.add(this.testResultDTO);
        return testResultDTO;
    }

}