package com.epam.test_generator.entities;

public enum Status {

    NOT_DONE("Not done"),
    NOT_RUN("Not run"),
    PASSED("Passed"),
    FAILED("Failed"),
    SKIPPED("Skipped");

    private String statusName;

    Status(String statusName) {
        this.statusName = statusName;
    }
}