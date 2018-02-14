package com.epam.test_generator.entities;


/**
 * This class represents event's essence, event is an action that controls lifecycle of test cases. Each {@Link Case} has
 * a {@Link State} essence, that determine it's current state. {@Link Event} is used to change states of the particular
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