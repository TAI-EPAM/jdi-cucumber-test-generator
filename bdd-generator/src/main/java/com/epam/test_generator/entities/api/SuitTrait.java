package com.epam.test_generator.entities.api;

import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Status;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public interface SuitTrait {

    List<Case> getCases();

    void setStatus(Status status);

    Status getStatus();

    void setCases(List<Case> objects);

    default boolean hasCase(Case aCase) {
         return getCases().contains(aCase);
    }

    /**
     * Appends the specified {@link Case} to this {@link com.epam.test_generator.entities.Suit},
     * and update {@link Status}.
     *
     * @param caze {@link Case} to be appended to this {@link com.epam.test_generator.entities.Suit}
     */
    default void addCase(Case caze) {
        if (getCases() == null) {
            setCases(new ArrayList<>());
        }
        caze.setRowNumber(getCases()
            .stream()
            .map(Case::getRowNumber)
            .max(Comparator.naturalOrder())
            .orElse(0)
            + 1);
        getCases().add(caze);
        updateStatus();
    }

    /**
     * Removes {@link Case} from {@link com.epam.test_generator.entities.Suit}
     * if it is present, and set {@link Status} NOT_DONE
     * if after removing {@link com.epam.test_generator.entities.Suit}
     * won't contain steps in cases.
     *
     * @param aCase {@link Case} to be removed from this
     *        {@link com.epam.test_generator.entities.Suit}, if present
     * @return <tt>true</tt> if this {@link com.epam.test_generator.entities.Suit}
     *         contained the specified {@link Case}
     */
    default boolean removeCase(Case aCase){
        boolean isRemoved = getCases().remove(aCase);
        updateStatus();
        return isRemoved;
    }

    /**
     * Returns <tt>true</tt> if this {@link com.epam.test_generator.entities.Suit}
     * contains at least one {@link com.epam.test_generator.entities.Step}
     * in each case.
     *
     * @return <tt>true</tt> if this suit contained at least one
     *         {@link com.epam.test_generator.entities.Step} in cases
     */
    default boolean hasStepsInAllCases(){
        return getCases().stream().map(Case::getSteps).noneMatch(List::isEmpty);
    }

    /**
     * Set {@link Status} NOT_RUN if all cases contains at least one
     * {@link com.epam.test_generator.entities.Step}, otherwise set NOT_DONE.
     *
     */
    default void updateStatus(){
        setStatus(hasStepsInAllCases() ? Status.NOT_RUN : Status.NOT_DONE);
    }
}
