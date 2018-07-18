package com.epam.test_generator.entities.api;

import com.epam.test_generator.entities.DefaultStepSuggestion;
import com.epam.test_generator.entities.StepSuggestion;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

public interface StepSuggestionProjectTrait {

    Set<StepSuggestion> getStepSuggestions();

    default boolean addStepSuggestion(StepSuggestion stepSuggestion) {
        Set<StepSuggestion> stepSuggestions = getStepSuggestions();
        if (hasStepSuggestion(stepSuggestion)) {
            return false;
        }
        return stepSuggestions.add(stepSuggestion);
    }

    /**
     * Convert and add DefaultStepSuggestions to the Project's StepSuggestions
     *
     * @param defaultStepSuggestions list of {@link DefaultStepSuggestion}
     */
    default void addStepSuggestions(@NotNull List<DefaultStepSuggestion> defaultStepSuggestions) {
        getStepSuggestions().addAll(
            defaultStepSuggestions.stream()
                .map(StepSuggestion::new)
                .collect(Collectors.toList()
                ));
    }

    default boolean removeStepSuggestion(StepSuggestion stepSuggestion) {
        return getStepSuggestions().remove(stepSuggestion);
    }

    default boolean hasStepSuggestion(StepSuggestion stepSuggestion) {
        return getStepSuggestions().stream().anyMatch(s ->
            s.getType().equals(stepSuggestion.getType()) &&
                s.getContent().equals(stepSuggestion.getContent()));
    }
}
