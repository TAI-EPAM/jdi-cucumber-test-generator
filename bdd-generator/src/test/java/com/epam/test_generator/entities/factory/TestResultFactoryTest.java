package com.epam.test_generator.entities.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dto.RawCaseResultDTO;
import com.epam.test_generator.dto.RawStepResultDTO;
import com.epam.test_generator.dto.RawSuitResultDTO;
import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.results.TestResult;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.ProjectService;
import com.epam.test_generator.services.StepService;
import com.epam.test_generator.services.SuitService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class TestResultFactoryTest {


    private static final String EXECUTED_BY = "test@test.com";
    private static final long PROJECT_ID = 1L;
    private static final long SUIT_ID = 2L;
    private static final long CASE_ID = 3L;
    private static final long STEP_ID = 4L;


    @Mock
    private ProjectService projectService;

    @Mock
    private SuitService suitService;

    @Mock
    private CaseService caseService;

    @Mock
    private StepService stepService;

    @InjectMocks
    private TestResultFactory testResultFactory;

    private List<RawSuitResultDTO> rawSuitResults;

    private Project project;
    private Suit suit;
    private Case aCase;
    private StepDTO step;


    @Before
    public void setUp() {

        rawSuitResults = new ArrayList<>();
        List<RawStepResultDTO> rawStepResultDTOS = new ArrayList<>();
        List<RawCaseResultDTO> rawCaseResultDTOS = new ArrayList<>();

        IntStream.range(0, 11)
            .forEach(i -> rawStepResultDTOS
                .add(new RawStepResultDTO(i, Status.PASSED)));
        IntStream.range(11, 21)
            .forEach(i -> rawCaseResultDTOS
                .add(new RawCaseResultDTO(i, 5, Status.PASSED, rawStepResultDTOS)));
        IntStream.range(21, 31).forEach(i -> rawSuitResults
            .add(new RawSuitResultDTO(i, rawCaseResultDTOS)));

        project = new Project();
        project.setId(PROJECT_ID);

        suit = new Suit();
        suit.setId(SUIT_ID);
        suit.setName("SUIT_NAME");

        aCase = new Case();
        aCase.setId(CASE_ID);
        aCase.setName("CASE_NAME");
        aCase.setComment("CASE_COMMENT");

        step = new StepDTO();
        step.setId(STEP_ID);
        step.setDescription("STEP_DESCRIPTION");

    }


    @Test
    public void createTestResultFrom_SimpleTestResult_Success() {

        when(projectService.getProjectByProjectId(anyLong())).thenReturn(project);
        when(suitService.getSuit(anyLong(), anyLong())).thenReturn(suit);
        when(caseService.getCase(anyLong(), anyLong(), anyLong())).thenReturn(aCase);
        when(stepService.getStep(anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(step);

        TestResult testResult = testResultFactory
            .createTestResultFrom(anyLong(), EXECUTED_BY, rawSuitResults);

        assertThat(testResult.getStatus(), is(equalTo(Status.PASSED)));
        assertThat(testResult.getAmountOfPassed(), is(equalTo(10)));
        assertThat(testResult.getAmountOfFailed(), is(equalTo(0)));
        assertThat(testResult.getAmountOfSkipped(), is(equalTo(0)));
        assertThat(testResult.getDuration(), is(equalTo(500L)));
    }


}