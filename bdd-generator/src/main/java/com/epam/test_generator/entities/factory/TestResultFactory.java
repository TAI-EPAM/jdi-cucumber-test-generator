package com.epam.test_generator.entities.factory;

import com.epam.test_generator.dto.RawCaseResultDTO;
import com.epam.test_generator.dto.RawStepResultDTO;
import com.epam.test_generator.dto.RawSuitResultDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.results.CaseResult;
import com.epam.test_generator.entities.results.StepResult;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.results.SuitResult;
import com.epam.test_generator.entities.results.TestResult;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.ProjectService;
import com.epam.test_generator.services.StepService;
import com.epam.test_generator.services.SuitService;
import java.util.ArrayList;
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

        TestResult testResult = new TestResult();

        testResult.setExecutedBy(executedBy);
        testResult.setProject(projectService.getProjectByProjectId(projectId));
        testResult.setSuitResults(getListOfSuitResultsFrom(projectId, suitResultDTOS));

        return testResult;
    }

    private List<SuitResult> getListOfSuitResultsFrom(Long projectId,
                                                      List<RawSuitResultDTO> suitResultDTOS) {

        List<SuitResult> suitResults = new ArrayList<>();

        for (RawSuitResultDTO rawSuitResultDTO : suitResultDTOS) {
            SuitResult suitResult = createSuitResultFrom(projectId, rawSuitResultDTO);
            suitResults.add(suitResult);
        }
        return suitResults;
    }


    private SuitResult createSuitResultFrom(Long projectId, RawSuitResultDTO rawSuitResultDTO) {

        Long suitId = rawSuitResultDTO.getId();
        Suit suit = suitService.getSuit(projectId, suitId);
        SuitResult suitResult = new SuitResult();

        suitResult.setName(suit.getName());
        suitResult.setCaseResults(getListOfCaseResultsFrom(projectId, rawSuitResultDTO));

        return suitResult;

    }

    private List<CaseResult> getListOfCaseResultsFrom(Long projectId,
                                                      RawSuitResultDTO rawSuitResultDTO) {

        List<CaseResult> caseResults = new ArrayList<>();

        for (RawCaseResultDTO caseResultDTO : rawSuitResultDTO.getCaseResultDTOList()) {

            CaseResult caseResult = createCaseResultFrom(projectId, rawSuitResultDTO.getId(),
                caseResultDTO);
            caseResults.add(caseResult);
        }
        return caseResults;
    }

    private CaseResult createCaseResultFrom(Long projectId, long suitId,
                                            RawCaseResultDTO caseResultDTO) {
        Case aCase = caseService.getCase(projectId, suitId, caseResultDTO.getId());
        CaseResult caseResult = new CaseResult();
        caseResult.setName(aCase.getName());
        caseResult.setComment(aCase.getComment());
        caseResult.setStepResults(getListStepResults(projectId, suitId, caseResultDTO));
        caseResult.setStatus(caseResultDTO.getStatus());
        caseResult.setDuration(caseResultDTO.getDuration());

        return caseResult;
    }

    private StepResult createStepResult(Long projectId, Long suitId,
                                        Long caseId,
                                        RawStepResultDTO stepResultDTO) {
        StepResult stepResult = new StepResult();
        stepResult.setStatus(stepResultDTO.getStatus());
        stepResult.setDescription
            (stepService.getStep(projectId, suitId, caseId, stepResultDTO.getId())
                .getDescription());
        return stepResult;
    }


    private List<StepResult> getListStepResults(Long projectId, Long suitId,
                                                RawCaseResultDTO rawCaseResultDTO) {
        return rawCaseResultDTO.getSteps()
            .stream()
            .map(stepResultDTO -> createStepResult(projectId, suitId,
                rawCaseResultDTO.getId(), stepResultDTO))
            .collect(Collectors.toList());
    }

}
