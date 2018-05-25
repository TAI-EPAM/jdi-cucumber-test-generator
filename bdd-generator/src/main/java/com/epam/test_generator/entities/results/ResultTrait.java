package com.epam.test_generator.entities.results;

import com.epam.test_generator.entities.Status;

import java.util.List;
import java.util.stream.Collectors;

public interface ResultTrait {
    /**
     * Evaluate summarized status from collection of statuses
     *
     * @param results collection of {@link Status}
     * @return results evaluated by given rules
     */
    default Status calculateStatus(List<? extends AbstractResult> results) {

        List<Status> collect = results
            .stream()
            .map(AbstractResult::getStatus)
            .collect(Collectors.toList());

        if (collect.stream().anyMatch(Status.FAILED::equals)) {
            return Status.FAILED;
        } else if (collect.stream().anyMatch(Status.PASSED::equals)) {
            return Status.PASSED;
        } else {
            return Status.SKIPPED;
        }
    }

    default long calculateDuration(List<? extends AbstractResult> results) {
        return results
            .stream()
            .mapToLong(AbstractResult::getDuration)
            .sum();
    }
}
