package com.epam.test_generator.services;

import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.EditCaseDTO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.entities.Action;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.services.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class CascadeUpdateService {

    @Autowired
    private CaseService caseService;

    @Autowired
    private StepService stepService;

    public List<Long> cascadeSuitCasesUpdate(long projectId, long suitId, SuitDTO suitDTO)
        throws MethodArgumentNotValidException {

        if (suitDTO.getCases() == null) {
            return Collections.emptyList();
        }
        final List<EditCaseDTO> casesForUpdate = getCasesForUpdate(suitDTO);

        final List<Long> distinctedListOfCasesIds = casesForUpdate.stream().map(EditCaseDTO::getId)
            .distinct().collect(Collectors.toList());

        if (distinctedListOfCasesIds.size() != casesForUpdate.size()) {
            throw new BadRequestException("There are duplicate ids in list of cases");
        }

        final List<CaseDTO> casesForCreate = getCasesForCreate(suitDTO);
        final Map<Long, List<StepDTO>> stepsOfCaseForUpdate = getMapOfCaseIdAndItsListOfSteps(
            casesForUpdate);

        stepsOfCaseForUpdate.forEach((caseId, listOfSteps) -> stepService
            .cascadeUpdateSteps(projectId, suitId, caseId, listOfSteps));
        caseService.updateCases(projectId, suitId, casesForUpdate);
        addCasesToSuit(projectId, suitId, casesForCreate);

        return stepsOfCaseForUpdate.values().stream()
            .flatMap(Collection::stream)
            .filter(s -> s.getStatus().equals(Status.FAILED))
            .map(StepDTO::getId).
                collect(Collectors.toList());
    }

    public List<Long> cascadeCaseStepsUpdate(Long projectId, Long suitId, Long caseId,
                                             EditCaseDTO editCaseDTO) {
        if (editCaseDTO.getAction().equals(Action.UPDATE)) {
            if (editCaseDTO.getSteps() == null) {
                return Collections.emptyList();
            }
            stepService.cascadeUpdateSteps(projectId, suitId, caseId, editCaseDTO.getSteps());
            return stepService.getStepsByCaseId(projectId, suitId, caseId).stream()
                .filter(s -> s.getStatus().equals(Status.FAILED))
                .map(StepDTO::getId).collect(Collectors.toList());
        } else {
            throw new BadRequestException("Update action in case is expected!");
        }
    }

    private void addCasesToSuit(long projectId, long suitId, List<CaseDTO> casesForCreate) {
            for (CaseDTO caseDTO : casesForCreate) {
                if (caseDTO.getSteps() != null){
                    final Optional<StepDTO> existedCase = caseDTO.getSteps().stream()
                        .filter(s -> s.getId() != 0)
                        .findAny();
                    if (existedCase.isPresent()) {
                        throw new BadRequestException("Non-existed step is expected!");
                    }
                }
            }
            for (CaseDTO caseDTO : casesForCreate) {
                caseService.addCaseToSuit(projectId, suitId, caseDTO);
            }
    }

    private List<EditCaseDTO> getCasesForUpdate(SuitDTO suitDTO) {

        return suitDTO.getCases().stream()
            .filter(c -> c.getId() != 0)
            .map(c -> new EditCaseDTO(
                c.getId(), c.getDescription(), c.getName(), c.getPriority(), c.getStatus(),
                c.getSteps(), Action.UPDATE, c.getComment()))
            .collect(Collectors.toList());

    }

    private List<CaseDTO> getCasesForCreate(SuitDTO suitDTO) {
        return suitDTO.getCases().stream()
            .filter(c -> c.getId() == 0)
            .collect(Collectors.toList());
    }

    private Map<Long, List<StepDTO>> getMapOfCaseIdAndItsListOfSteps(List<EditCaseDTO> cases) {

        final Map<Long, List<StepDTO>> stepsOfCase = new HashMap<>();

        for (EditCaseDTO aCase : cases) {
            if (aCase.getSteps() == null) {
                aCase.setSteps(Collections.emptyList());
            }
            for (StepDTO stepDTO : aCase.getSteps()) {
                if (stepDTO.getId() == 0) {
                    stepDTO.setAction(Action.CREATE);
                } else {
                    stepDTO.setAction(Action.UPDATE);
                }
            }
        }

        extractStepsFromCaseToMap(cases, stepsOfCase);
        return stepsOfCase;
    }


    private void extractStepsFromCaseToMap(List<EditCaseDTO> cases,
                                           Map<Long, List<StepDTO>> stepsOfCase) {
        for (EditCaseDTO caseDTO : cases) {
            stepsOfCase.put(caseDTO.getId(), caseDTO.getSteps());
            caseDTO.setSteps(Collections.emptyList());
        }
    }
}