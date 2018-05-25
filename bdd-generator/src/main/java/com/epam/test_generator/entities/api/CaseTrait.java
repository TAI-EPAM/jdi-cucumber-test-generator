package com.epam.test_generator.entities.api;

import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Step;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public interface CaseTrait {

    List<Step> getSteps();

    void setSteps(List<Step> objects);

    void setStatus(Status status);

    default boolean hasStep(Step step) {
        return getSteps().contains(step);
    }

    /**
     * Removes {@link Step} from {@link com.epam.test_generator.entities.Case}
     * if it is present, and set {@link Status} NOT_DONE
     * if after removing {@link com.epam.test_generator.entities.Case}
     * won't contain steps.
     *
     * @param step {@link Step} to be removed from this
     *        {@link com.epam.test_generator.entities.Case}, if present
     * @return <tt>true</tt> if this {@link com.epam.test_generator.entities.Case}
     *         contained the specified {@link Step}
     */
    default boolean removeStep(Step step) {
        boolean isRemoved = getSteps().remove(step);
        setStatus(getSteps().isEmpty() ? Status.NOT_DONE : Status.NOT_RUN);
        return isRemoved;
    }

    /**
     * Appends the specified {@link Step} to this {@link com.epam.test_generator.entities.Case},
     * and set {@link Status} NOT_RUN.
     *
     * @param step {@link Step} to be appended to this {@link com.epam.test_generator.entities.Case}
     */
    default void addStep(Step step) {
        if (getSteps() == null) {
            setSteps(new ArrayList<>());
        }
        step.setRowNumber(getSteps()
            .stream()
            .map(Step::getRowNumber)
            .max(Comparator.naturalOrder())
            .orElse(0)
            + 1
        );
        getSteps().add(step);
        setStatus(Status.NOT_RUN);
    }


}
