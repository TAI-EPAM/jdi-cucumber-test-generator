package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.StepDAO;
import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.transformers.StepTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class StepService {

    @Autowired
    private StepDAO stepDAO;

    @Autowired
    private CaseDAO caseDAO;

    @Autowired
    private StepTransformer stepTransformer;

    public List<StepDTO> getStepsByCaseId(Long caseId) {

        return stepTransformer.toDtoList(caseDAO.findOne(caseId).getSteps());
    }

    public StepDTO getStep(Long stepId) {
        Step step = stepDAO.findOne(stepId);
        if (step == null) {
            return null;
        }

        return stepTransformer.toDto(step);
    }

    public Long addStepToCase(Long caseId, StepDTO stepDTO) {
        Case caze = caseDAO.findOne(caseId);
        Step step = stepTransformer.fromDto(stepDTO);

        step = stepDAO.save(step);
        caze.getSteps().add(step);//????

        return step.getId();
    }

    public void removeStep(Long caseId, Long stepId) {
        Case caze = caseDAO.findOne(caseId);
        Step step = stepDAO.findOne(stepId);

        caze.getSteps().remove(step);
        stepDAO.delete(stepId);
    }

    public void updateStep(Long stepId, StepDTO stepDTO) {
        Step step = stepDAO.findOne(stepId);
        stepTransformer.mapDTOToEntity(stepDTO, step);

        stepDAO.save(step);
    }
}
