package com.epam.test_generator.entities.api;

import com.epam.test_generator.entities.Step;
import java.util.List;

public interface CaseTrait {

    List<Step> getSteps();

    default boolean hasStep(Step step) {
        return getSteps().contains(step);
    }

    default boolean removeStep(Step step) {
        return getSteps().remove(step);
    }

}
