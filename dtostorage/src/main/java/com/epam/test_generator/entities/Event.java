package com.epam.test_generator.entities;


/**
 * This class represents event's essence, event is an action that controls lifecycle of test cases. Each {@link Case} has
 * a {@link Status} essence, that determine it's current status. {@link Event} is used to change status of the particular
 * test case.
 */
public enum Event {
    CREATE("Create"),
    PASS("Pass"),
    FAIL("Fail"),
    SKIP("Skip"),
    EDIT("Edit");

    private String eventName;

    Event(String eventName) {
        this.eventName = eventName;
    }
}