package com.epam.test_generator.entities.api;

import com.epam.test_generator.entities.StepSuggestion;
import java.util.Set;

public interface StepSuggestionProjectTrait {

    Set<StepSuggestion> getStepSuggestions();

    default boolean addStepSuggestion(StepSuggestion stepSuggestion) {
        Set<StepSuggestion> stepSuggestions = getStepSuggestions();
        if (hasStepSuggestion(stepSuggestion)) {
            return false;
        }
        return stepSuggestions.add(stepSuggestion);
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
