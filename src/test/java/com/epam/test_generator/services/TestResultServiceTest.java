package com.epam.test_generator.services;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.config.security.AuthenticatedUser;
import com.epam.test_generator.dao.interfaces.TestResultDAO;
import com.epam.test_generator.dto.RawCaseResultDTO;
import com.epam.test_generator.dto.RawStepResultDTO;
import com.epam.test_generator.dto.RawSuitResultDTO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.TestResultDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.TestResult;
import com.epam.test_generator.entities.factory.TestResultFactory;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.transformers.TestResultTransformer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

@RunWith(MockitoJUnitRunner.class)
public class TestResultServiceTest {


    private static final String EXECUTED_BY = "test@test.com";
    private static final long PROJECT_ID = 1L;
    private static final long SUIT_ID = 2L;
    private static final long CASE_ID = 3L;
    private static final long STEP_ID = 4L;
    private static final int OFFSET = 3;
    private static final int LIMIT = 2;

    @Mock
    private TestResultDAO testResultDAO;

    @Mock
    private TestResultTransformer testResultTransformer;

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private TestResultFactory testResultFactory;

    @InjectMocks
    private TestResultService testResultService;

    private List<RawSuitResultDTO> rawSuitResults;
    private List<TestResult> testResults;
    private Project project;
    private Suit suit;
    private Case caze;
    private StepDTO step;
    private TestResult testResult;
    private TestResultDTO testResultDTO;


    @Before
    public void setUp() {
        final RawStepResultDTO rawStepResult = new RawStepResultDTO(STEP_ID, Status.PASSED);
        final RawCaseResultDTO rawCaseResult = new RawCaseResultDTO(CASE_ID, 10L, Status.PASSED,
            Collections.singletonList(rawStepResult));
        final RawSuitResultDTO rawSuitResult = new RawSuitResultDTO(SUIT_ID,
            Collections.singletonList(rawCaseResult));
        rawSuitResults = Collections.singletonList(rawSuitResult);

        project = new Project();
        project.setId(PROJECT_ID);

        suit = new Suit();
        suit.setId(SUIT_ID);
        suit.setName("SUIT_NAME");

        caze = new Case();
        caze.setId(CASE_ID);
        caze.setName("CASE_NAME");
        caze.setComment("CASE_COMMENT");

        step = new StepDTO();
        step.setId(SUIT_ID);
        step.setDescription("STEP_DESCRIPTION");

        testResult = generateTestResult(LocalDate.now(), 12);

        testResults = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            testResults.add(generateTestResult(LocalDate.now(), i));
        }
        testResultDTO = new TestResultDTO();
        testResultDTO.setAmountOfSkipped(0);
        testResultDTO.setAmountOfFailed(0);
        testResultDTO.setAmountOfPassed(1);
        testResultDTO.setStatus(Status.PASSED);
        testResultDTO.setExecutedBy(EXECUTED_BY);
        testResultDTO.setDuration(0);
        testResultDTO.setSuits(Collections.emptyList());

    }

    @Test
    public void saveResult() {
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);
        when(authenticatedUser.getEmail()).thenReturn(EXECUTED_BY);
        when(testResultFactory.createTestResultFrom(PROJECT_ID, EXECUTED_BY, rawSuitResults))
            .thenReturn(testResult);

        testResultService.saveResult(PROJECT_ID, rawSuitResults, authentication);

        verify(testResultDAO).save(any(TestResult.class));

    }

    @Test
    public void getTestResults_SimpleTestResultBy_StatusOk() {
        testResultService.getTestResults(PROJECT_ID);

        verify(testResultDAO).findAllByProjectIdOrderByDateDesc(eq(PROJECT_ID));
    }

    @Test
    public void getTestResults_SimpleTestResult_StatusOk() {

        when(testResultDAO.findAllByProjectIdOrderByDateDesc(PROJECT_ID)).thenReturn(testResults);
        final List<TestResultDTO> testResults = testResultService
            .getTestResults(PROJECT_ID, OFFSET, LIMIT);
        assertThat(testResults, is(notNullValue()));
        assertThat(testResults.size(), is(2));
    }

    @Test(expected = BadRequestException.class)
    public void getTestResults_NullDatesRange_BadRequest() {
        testResultService.getTestResults(PROJECT_ID, null, null);
    }

    @Test
    public void getTestResults() {
        final LocalDate now = LocalDate.now();
        final Date from = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());
        final Date to = Date.from(now.plusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant());
        testResultService.getTestResults(PROJECT_ID, from, to);
        verify(testResultDAO).findAllByProjectIdAndDateAfterAndDateBefore(eq(PROJECT_ID), eq(from), eq(to));
    }

    private TestResult generateTestResult(LocalDate localDate, Integer day) {
        final TestResult testResult = new TestResult();
        final Date date = Date
            .from(localDate.plusDays(day).atStartOfDay(ZoneId.systemDefault()).toInstant());
        testResult.setAmountOfPassed(1);
        testResult.setAmountOfFailed(0);
        testResult.setAmountOfSkipped(3);
        testResult.setStatus(Status.PASSED);
        testResult.setDate(date);
        testResult.setProject(project);
        return testResult;
    }
}