package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;
import static com.epam.test_generator.services.utils.UtilsService.verifyVersion;

import com.epam.test_generator.dao.interfaces.StepSuggestionDAO;
import com.epam.test_generator.dto.StepSuggestionCreateDTO;
import com.epam.test_generator.dto.StepSuggestionDTO;
import com.epam.test_generator.dto.StepSuggestionUpdateDTO;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.transformers.StepSuggestionTransformer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@DependsOn("liquibase")
public class StepSuggestionService {

    @Autowired
    private StepSuggestionTransformer stepSuggestionTransformer;
    @Autowired
    private StepSuggestionDAO stepSuggestionDAO;

    /**
     * Method to get step suggestions by pages with set parameters
     * @param stepType get only this type step suggestions
     * @param pageNumber number of page (from 1)
     * @param pageSize number of step suggestions on one page
     * @return realisation of getStepsSuggestions method depending on which parameters available
     */
    public List<StepSuggestionDTO> getStepsSuggestions(StepType stepType, Integer pageNumber,
                                                       Integer pageSize) {
        if (isPageNumberAndPageSizeAndStepTypeNotNull(stepType, pageNumber, pageSize)) {
            return getStepsSuggestionsByType(stepType, pageNumber, pageSize);
        }
        if (isPageNumberAndPageSizeNotNull(pageNumber, pageSize)) {
            return getStepsSuggestions(pageNumber, pageSize);
        }
        if (stepType != null) {
            return getStepsSuggestionsByType(stepType);
        }
        return getStepsSuggestions();
    }

    private List<StepSuggestionDTO> getStepsSuggestions() {
        return stepSuggestionTransformer.toDtoList(stepSuggestionDAO.findAll());
    }

    private List<StepSuggestionDTO> getStepsSuggestions(int pageNumber, int pageSize) {
        Pageable request = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "id");
        return stepSuggestionTransformer.toDtoList(stepSuggestionDAO.findAll(request).getContent());
    }

    private boolean isPageNumberAndPageSizeNotNull(Integer pageNumber, Integer pageSize) {
        return pageNumber != null && pageSize != null;
    }

    private boolean isPageNumberAndPageSizeAndStepTypeNotNull(StepType stepType, Integer pageNumber,
                                                              Integer pageSize) {
        return isPageNumberAndPageSizeNotNull(pageNumber, pageSize) && stepType != null;
    }

    public StepSuggestionDTO getStepsSuggestion(long stepSuggestionId) {
        StepSuggestion stepSuggestion = stepSuggestionDAO.findOne(stepSuggestionId);
        checkNotNull(stepSuggestion);

        return stepSuggestionTransformer.toDto(stepSuggestion);
    }

    private List<StepSuggestionDTO> getStepsSuggestionsByType(StepType stepType, int pageNumber, int pageSize) {
        Pageable request = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "id");

        return stepSuggestionTransformer.toDtoList(
                stepSuggestionDAO.findAll(request).getContent().stream()
                        .filter(s -> s.getType() == stepType)
                        .collect(Collectors.toList()));
    }

    public List<StepSuggestionDTO> getStepsSuggestionsByType(StepType stepType) {
        return stepSuggestionTransformer.toDtoList(
                stepSuggestionDAO.findAll().stream()
                        .filter(s -> s.getType() == stepType)
                        .collect(Collectors.toList()));
    }

    /**
     * Adds step suggestion specified in stepSuggestionDTO
     *
     * @param stepSuggestionCreateDTO
     * @return id of step suggestion
     */
    public Long addStepSuggestion(StepSuggestionCreateDTO stepSuggestionCreateDTO) {

        StepSuggestion stepSuggestion = new StepSuggestion(stepSuggestionCreateDTO.getContent(),
                stepSuggestionCreateDTO.getType(), 0L);
        stepSuggestion.setId(0L);
        stepSuggestion = stepSuggestionDAO
                .save(stepSuggestion);

        return stepSuggestion.getId();
    }

    /**
     * Updates step suggestion specified in stepSuggestionDTO by id
     *
     * @param stepSuggestionId        id of step suggestion to update
     * @param stepSuggestionUpdateDTO info to update
     */
    public void updateStepSuggestion(Long stepSuggestionId,
                                     StepSuggestionUpdateDTO stepSuggestionUpdateDTO) {
        StepSuggestion stepSuggestion = stepSuggestionDAO.findOne(stepSuggestionId);
        checkNotNull(stepSuggestion);
        verifyVersion(stepSuggestionUpdateDTO.getVersion(), stepSuggestion);
        if (stepSuggestionUpdateDTO.getContent() != null) {
            stepSuggestion.setContent(stepSuggestionUpdateDTO.getContent());
        }

        if (stepSuggestionUpdateDTO.getType() != null) {
            stepSuggestion.setType(stepSuggestionUpdateDTO.getType());
        }

        stepSuggestionDAO.save(stepSuggestion);
    }

    /**
     * Deletes step suggestion from database by id
     *
     * @param stepSuggestionId id of step suggestion to delete
     */
    public void removeStepSuggestion(Long stepSuggestionId) {
        StepSuggestion stepSuggestion = stepSuggestionDAO.findOne(stepSuggestionId);
        checkNotNull(stepSuggestion);

        stepSuggestionDAO.delete(stepSuggestionId);
    }

    /**
     * Find all steps suggestions by string ignoring case
     *
     * @param searchString - string for search
     * @param limit - count of results
     * @return list with found steps suggestions
     */
    public List<StepSuggestionDTO> findStepsSuggestions(String searchString, int limit) {
        if(searchString.isEmpty()) {
            throw new BadRequestException("Text must not be empty!");
        }

        if(limit < 1){
            throw new BadRequestException("Limit must not be less than one!");
        }
        Pageable numberOfReturnedResults = new PageRequest(0, limit);
        List<StepSuggestion> foundStepsSuggestions = stepSuggestionDAO
            .findByContentIgnoreCaseContaining(searchString, numberOfReturnedResults);

        List<StepSuggestionDTO> stepsSuggestionsDTO = stepSuggestionTransformer
            .toDtoList(foundStepsSuggestions);
        return stepsSuggestionsDTO;
    }
}
