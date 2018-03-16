package com.epam.test_generator.entities.factory;

import com.epam.test_generator.dto.RawCaseResultDTO;
import com.epam.test_generator.dto.RawStepResultDTO;
import com.epam.test_generator.dto.RawSuitResultDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.CaseResult;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.StepResult;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.SuitResult;
import com.epam.test_generator.entities.TestResult;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.ProjectService;
import com.epam.test_generator.services.StepService;
import com.epam.test_generator.services.SuitService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestResultFactory {


    @Autowired
    private SuitService suitService;

    @Autowired
    private CaseService caseService;

    @Autowired
    private StepService stepService;

    @Autowired
    private ProjectService projectService;


    /**
     * Return testResult, that store all crucial information about tests execution.
     *
     * @param projectId {@link com.epam.test_generator.entities.Project}
     * @param executedBy email of executed Author.
     * @param suitResultDTOS list {@link RawSuitResultDTO}
     * @return {@link TestResult}
     */
    public TestResult createTestResultFrom(Long projectId, String executedBy,
                                           List<RawSuitResultDTO> suitResultDTOS) {

        final TestResult testResult = new TestResult();

        testResult.setDate(Calendar.getInstance().getTime());
        testResult.setExecutedBy(executedBy);
        testResult.setProject(projectService.getProjectByProjectId(projectId));
        testResult.setSuits(getListOfSuitResultsFrom(projectId, suitResultDTOS));
        testResult.setDuration(summarizeDuration(testResult.getSuits()));
        testResult.setStatus(evaluateStatusFrom(getStatuses(testResult)));

        countingOfTestRunStatisticsFor(testResult);

        return testResult;
    }


    private List<Status> getStatuses(TestResult testResult) {
        return testResult.getSuits()
            .stream()
            .map(SuitResult::getStatus)
            .collect(Collectors.toList());
    }

    /**
     * Summarize amount of Passed, Skipped and Failed tests of Tests executions.
     *
     * @param testResult {@link TestResult}
     */
    private void countingOfTestRunStatisticsFor(TestResult testResult) {

        int amountOfPassed = 0;
        int amountOfSkipped = 0;
        int amountOfFailed = 0;

        for (SuitResult suitResult : testResult.getSuits()) {
            switch (suitResult.getStatus()) {
                case PASSED:
                    amountOfPassed++;
                    break;
                case SKIPPED:
                    amountOfSkipped++;
                    break;
                case FAILED:
                    amountOfFailed++;
                    break;
            }
        }

        testResult.setAmountOfPassed(amountOfPassed);
        testResult.setAmountOfFailed(amountOfFailed);
        testResult.setAmountOfSkipped(amountOfSkipped);
    }


    private List<SuitResult> getListOfSuitResultsFrom(Long projectId,
                                                      List<RawSuitResultDTO> suitResultDTOS) {

        final List<SuitResult> suitResults = new ArrayList<>();

        for (RawSuitResultDTO rawSuitResultDTO : suitResultDTOS) {
            final SuitResult suitResult = createSuitResultFrom(projectId, rawSuitResultDTO);
            suitResults.add(suitResult);
        }
        return suitResults;
    }


    private SuitResult createSuitResultFrom(Long projectId, RawSuitResultDTO rawSuitResultDTO) {

        final Long suitId = rawSuitResultDTO.getId();
        final Suit suit = suitService.getSuit(projectId, suitId);
        final SuitResult suitResult = new SuitResult();

        suitResult.setName(suit.getName());
        suitResult.setCaseResults(getListOfCaseResultsFrom(projectId, rawSuitResultDTO));
        suitResult.setDuration(summarizeDurations(suitResult.getCaseResults()));
        suitResult.setStatus(evaluateStatusFrom(getStatuses(suitResult)));

        return suitResult;

    }

    private List<Status> getStatuses(SuitResult suitResult) {
        return suitResult.getCaseResults()
            .stream()
            .map(CaseResult::getStatus)
            .collect(Collectors.toList());
    }


    private long summarizeDurations(List<CaseResult> caseResults) {
        return caseResults
            .stream()
            .mapToLong(CaseResult::getDuration)
            .sum();
    }


    private long summarizeDuration(List<SuitResult> suitResults) {
        return suitResults
            .stream()
            .mapToLong(SuitResult::getDuration)
            .sum();
    }


    private List<CaseResult> getListOfCaseResultsFrom(Long projectId,
                                                      RawSuitResultDTO rawSuitResultDTO) {

        final List<CaseResult> caseResults = new ArrayList<>();

        for (RawCaseResultDTO caseResultDTO : rawSuitResultDTO.getCaseResultDTOList()) {

            final CaseResult caseResult = createCaseResultFrom(projectId, rawSuitResultDTO.getId(),
                caseResultDTO);
            caseResults.add(caseResult);
        }
        return caseResults;
    }


    private CaseResult createCaseResultFrom(Long projectId, long suitId,
                                            RawCaseResultDTO caseResultDTO) {
        final Case aCase = caseService.getCase(projectId, suitId, caseResultDTO.getId());
        final CaseResult caseResult = new CaseResult();
        caseResult.setName(aCase.getName());
        caseResult.setComment(aCase.getComment());
        caseResult.setDuration(caseResultDTO.getDuration());
        caseResult.setStatus(getCaseResultStatusFrom(caseResultDTO));
        caseResult.setSteps(getListStepResults(projectId, suitId, caseResultDTO));

        return caseResult;
    }

    private Status getCaseResultStatusFrom(RawCaseResultDTO caseResultDTO) {
        if (!Status.SKIPPED.equals(caseResultDTO.getStatus())) {

            final List<Status> stepsStatuses = caseResultDTO.getSteps()
                .stream()
                .map(RawStepResultDTO::getStatus)
                .collect(Collectors.toList());

            return evaluateStatusFrom(stepsStatuses);
        } else {
            return Status.SKIPPED;
        }
    }


    private List<StepResult> getListStepResults(Long projectId, Long suitId,
                                                RawCaseResultDTO rawCaseResultDTO) {
        return rawCaseResultDTO.getSteps()
            .stream()
            .map(stepResultDTO -> createStepResult(projectId, suitId,
                rawCaseResultDTO.getId(), stepResultDTO))
            .collect(Collectors.toList());
    }


    private StepResult createStepResult(Long projectId, Long suitId,
                                        Long caseId,
                                        RawStepResultDTO stepResultDTO) {
        final StepResult stepResult = new StepResult();
        stepResult.setStatus(stepResultDTO.getStatus());
        stepResult.setDescription
            (stepService.getStep(projectId, suitId, caseId, stepResultDTO.getId())
                .getDescription());
        return stepResult;
    }

    /**
     * Evaluate summarized status from collection of statuses
     *
     * @param statuses collection of {@link Status}
     * @return result evaluated by given rules
     */
    private Status evaluateStatusFrom(Collection<Status> statuses) {
        if (statuses.stream().anyMatch(Status.FAILED::equals)) {
            return Status.FAILED;
        }
        if (statuses.stream().anyMatch(Status.PASSED::equals)) {
            return Status.PASSED;
        }
        return Status.SKIPPED;
    }
}
