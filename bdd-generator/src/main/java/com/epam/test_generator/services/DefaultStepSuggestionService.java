package com.epam.test_generator.services;

import com.epam.test_generator.controllers.stepsuggestion.DefaultStepSuggestionTransformer;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.dao.interfaces.DefaultStepSuggestionDAO;
import com.epam.test_generator.entities.DefaultStepSuggestion;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.services.exceptions.NotFoundException;
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
public class DefaultStepSuggestionService {

    @Autowired
    private DefaultStepSuggestionTransformer defaultStepSuggestionTransformer;

    @Autowired
    private DefaultStepSuggestionDAO defaultStepSuggestionDAO;

    public List<StepSuggestionDTO> getStepsSuggestions() {
        return defaultStepSuggestionTransformer.toDtoList(defaultStepSuggestionDAO.findAll());
    }

    public StepSuggestionDTO getStepSuggestion(long stepSuggestionId) {
        DefaultStepSuggestion defaultStepSuggestion = defaultStepSuggestionDAO
            .findById(stepSuggestionId).orElseThrow(NotFoundException::new);

        return defaultStepSuggestionTransformer.toDto(defaultStepSuggestion);
    }

    /**
     * Find all default step suggestions of particular type on a particular page of particular size.
     * @param stepType type of default step suggestion. If it is StepType.ANY, then default step
     *                   suggestions of all types are returned
     * @param pageNumber page with this number will be returned
     * @param pageSize pages will have this size
     * @return list of {@link StepSuggestionDTO}
     */
    public List<StepSuggestionDTO> getStepsSuggestionsByTypeAndPage(StepType stepType, int pageNumber, int pageSize) {
        Pageable request = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.ASC, "id");

        if (stepType == StepType.ANY) {
            return defaultStepSuggestionDAO.findAll(request).getContent().stream()
                    .map(defaultStepSuggestionTransformer::toDto)
                    .collect(Collectors.toList());
        }
        return defaultStepSuggestionDAO.findByType(stepType, request).getContent().stream()
                    .filter(s -> s.getType() == stepType)
                    .map(defaultStepSuggestionTransformer::toDto)
                    .collect(Collectors.toList());
    }

    /**
     * Find all default step suggestions of particular type.
     * @param stepType type of default step suggestion. If it is StepType.ANY, then default step
     *                   suggestions of all types are returned
     * @return list of {@link StepSuggestionDTO}
     */
    public List<StepSuggestionDTO> getStepsSuggestionsByType(StepType stepType) {

        if (stepType == StepType.ANY) {
            return getStepsSuggestions();
        }

        return defaultStepSuggestionDAO.findAll().stream()
                    .filter(s -> s.getType() == stepType)
                    .map(defaultStepSuggestionTransformer::toDto)
                    .collect(Collectors.toList());
    }

    /**
     * Find all steps suggestions by string ignoring case
     *
     * @param searchString - string for search
     * @param limit - count of results
     * @return list with found steps suggestions
     */
    public List<StepSuggestionDTO> findStepsSuggestions(String searchString, int limit) {
        Pageable numberOfReturnedResults = new PageRequest(0, limit);
        List<DefaultStepSuggestion> foundStepsSuggestions = defaultStepSuggestionDAO
            .findByContentIgnoreCaseContaining(searchString, numberOfReturnedResults);

        List<StepSuggestionDTO> stepsSuggestionsDTO = defaultStepSuggestionTransformer
            .toDtoList(foundStepsSuggestions);
        return stepsSuggestionsDTO;
    }

}
