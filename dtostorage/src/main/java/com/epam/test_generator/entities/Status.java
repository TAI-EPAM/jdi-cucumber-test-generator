package com.epam.test_generator.entities;


/**
 * This enum represents status essence. Status in this case means result of verification (execution of test case's steps)
 * Each {@link Status} is a state of test case's lifecycle. Lifecycle of test case is represented with a Spring State
 * Machine. As {@link Status} is states of test case, than {@link Event} is action that change state of particular test
 * case.
 */
public enum Status {

    NOT_DONE("NOT DONE"),
    NOT_RUN("NOT RUN"),
    PASSED("PASSED"),
    FAILED("FAILED"),
    SKIPPED("SKIPPED");

    private String statusName;

    Status(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}